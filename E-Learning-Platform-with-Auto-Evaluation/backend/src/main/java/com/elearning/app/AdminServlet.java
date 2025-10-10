package com.elearning.app;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

@MultipartConfig(
    location = "/tmp",
    maxFileSize = 10485760,      // 10 MB
    maxRequestSize = 10485760,   // 10 MB
    fileSizeThreshold = 262144   // 256 KB
)
public class AdminServlet extends HttpServlet {

    private AssignmentManager assignmentManager;
    private LogManager logManager;
    private S3Helper s3Helper;

    public AdminServlet() {
        this.assignmentManager = AssignmentManager.getInstance();
        this.logManager = LogManager.getInstance();
        this.s3Helper = new S3Helper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Enable CORS
        setCORSHeaders(response);

        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");

        if (pathInfo != null && pathInfo.equals("/assignments")) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(assignmentManager.getAllAssignmentsAsJson());
            return;
        }

        if ("list".equals(action)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(assignmentManager.getAllAssignmentsAsJson());
        } else if ("view".equals(action)) {
            String assignmentId = request.getParameter("assignmentId");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Assignment assignment = assignmentManager.getAssignment(assignmentId);
            if (assignment != null) {
                response.getWriter().write(assignment.toJson());
            } else {
                response.setStatus(404);
                response.getWriter().write("{\"error\": \"Assignment not found\"}");
            }
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"status\":\"API Ready\",\"endpoint\":\"/admin\",\"message\":\"Use React frontend for admin interface\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setCORSHeaders(response);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String contentType = request.getContentType();
            Assignment assignment = null;

            if (contentType != null && contentType.startsWith("multipart/form-data")) {
                String assignmentType = getMultipartValue(request, "assignmentType");

                if ("CODE".equalsIgnoreCase(assignmentType)) {
                    assignment = parseCodeAssignmentFromFile(request);
                } else {
                    assignment = parseAssignmentFromFile(request);
                }
            } else {
                BufferedReader reader = request.getReader();
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                String jsonData = jsonBuilder.toString();
                assignment = parseAssignmentFromJson(jsonData);
            }

            if (assignment == null) {
                response.setStatus(400);
                response.getWriter().write("{\"error\": \"Invalid assignment data\"}");
                return;
            }

            assignmentManager.saveAssignment(assignment);

            // Log creation
            logManager.logInfo("Assignment created", assignment.getTeacherId(),
                             assignment.getAssignmentId(),
                             "Title: " + assignment.getTitle());

            response.setStatus(201);
            String s3UrlMsg = assignment.getS3Url() != null ?
                             ", \"s3Url\": \"" + assignment.getS3Url() + "\", \"fileStored\": true" :
                             ", \"fileStored\": false";
            String jsonResponse = String.format(
                "{\"success\": true, \"assignmentId\": \"%s\", \"message\": \"Assignment created successfully\", \"questionsCount\": %d%s}",
                assignment.getAssignmentId(), assignment.getQuestions().size(), s3UrlMsg
            );
            response.getWriter().write(jsonResponse);

        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String assignmentId = request.getParameter("assignmentId");

        if (assignmentId == null || assignmentId.trim().isEmpty()) {
            response.setStatus(400);
            response.getWriter().write("{\"error\": \"Assignment ID is required\"}");
            return;
        }

        assignmentManager.deleteAssignment(assignmentId);
        response.getWriter().write("{\"success\": true, \"message\": \"Assignment deleted\"}");
    }

    private Assignment parseAssignmentFromFile(HttpServletRequest request) throws Exception {
        String teacherId = getMultipartValue(request, "teacherId");
        String teacherName = getMultipartValue(request, "teacherName");
        String title = getMultipartValue(request, "title");
        String description = getMultipartValue(request, "description");

        if (teacherId == null || teacherId.isEmpty()) {
            teacherId = teacherName != null ? teacherName : "TEACHER-" + System.currentTimeMillis();
        }

        Part filePart = request.getPart("assignmentFile");
        if (filePart == null || filePart.getSize() == 0) {
            filePart = request.getPart("questionFile");
        }
        if (filePart == null || filePart.getSize() == 0) {
            throw new Exception("No file uploaded");
        }

        String assignmentId = "ASSIGN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String fileName = filePart.getSubmittedFileName();

        File tempFile = File.createTempFile("teacher_assignment_", "_" + fileName);
        try (InputStream input = filePart.getInputStream();
             FileOutputStream output = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }

        String s3Key = String.format("teacher-assignments/%s/%s/%s",
                                    assignmentId, teacherId, fileName);
        String s3Url = s3Helper.uploadFileWithMetadata(tempFile, s3Key, teacherId, assignmentId);

        if (s3Url != null) {
            System.out.println("Teacher assignment file uploaded to S3: " + s3Url);
            logManager.logInfo("Teacher assignment uploaded to S3", teacherId, assignmentId, "S3 URL: " + s3Url);
        } else {
            System.out.println("S3 upload failed for teacher assignment");
            logManager.logWarning("Teacher S3 upload failed", teacherId, assignmentId, "File will not be stored in S3");
        }

        // Determine file type and extract text content
        String fileContent;
        String fileExtension = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();

        if (fileExtension.equals(".docx")) {
            fileContent = extractTextFromDocx(tempFile);
        } else if (fileExtension.equals(".doc")) {
            fileContent = extractTextFromDoc(tempFile);
        } else {
            // Plain text file
            BufferedReader textReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(tempFile), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String textLine;
            while ((textLine = textReader.readLine()) != null) {
                sb.append(textLine).append("\n");
            }
            textReader.close();
            fileContent = sb.toString();
        }

        // Now process the text content line by line
        String[] lines = fileContent.split("\n");

        if (title == null || title.isEmpty()) {
            title = fileName != null ? fileName.replaceAll("\\..+$", "") : "Untitled Assignment";
        }

        if (description == null || description.isEmpty()) {
            description = "Assignment created by " + (teacherName != null ? teacherName : "Teacher");
        }

        Assignment assignment = new Assignment(assignmentId, title, description, teacherId, teacherName);
        assignment.setS3Url(s3Url); // Store S3 URL in assignment

        String currentQuestionId = null;
        String currentQuestionText = null;
        String currentCorrectAnswer = null;
        List<String> currentKeywords = new ArrayList<>();
        int currentPoints = 0;

        boolean readingQuestion = false;
        boolean readingAnswer = false;
        boolean readingKeywords = false;

        StringBuilder questionBuffer = new StringBuilder();
        StringBuilder answerBuffer = new StringBuilder();

        for (String line : lines) {
            line = line.trim();

            if (line.isEmpty() || line.startsWith("#") || line.startsWith("===")) {
                continue;
            }

            if (line.startsWith("Question ") && line.contains(":")) {
                if (currentQuestionId != null && currentQuestionText != null && currentCorrectAnswer != null) {
                    Question q = new Question(currentQuestionId, currentQuestionText.trim(),
                                            currentCorrectAnswer.trim(), currentPoints,
                                            currentKeywords.toArray(new String[0]));
                    assignment.addQuestion(q);
                }

                currentQuestionId = "Q" + line.substring(9, line.indexOf(":")).trim();
                currentQuestionText = line.substring(line.indexOf(":") + 1).trim();
                currentCorrectAnswer = null;
                currentKeywords = new ArrayList<>();
                currentPoints = 10; // default
                readingQuestion = false;
                readingAnswer = false;
                questionBuffer = new StringBuilder();
                answerBuffer = new StringBuilder();
            }
            else if (line.startsWith("Correct Answer:")) {
                readingAnswer = true;
                readingQuestion = false;
                readingKeywords = false;
                String answerStart = line.substring("Correct Answer:".length()).trim();
                if (!answerStart.isEmpty()) {
                    answerBuffer.append(answerStart);
                }
            }
            else if (line.startsWith("Keywords:")) {
                readingKeywords = true;
                readingAnswer = false;
                readingQuestion = false;
                if (answerBuffer.length() > 0) {
                    currentCorrectAnswer = answerBuffer.toString();
                }
                String keywordLine = line.substring("Keywords:".length()).trim();
                String[] keywords = keywordLine.split(",");
                for (String kw : keywords) {
                    String keyword = kw.trim();
                    if (!keyword.isEmpty()) {
                        currentKeywords.add(keyword);
                    }
                }
            }
            else if (line.startsWith("Points:")) {
                readingKeywords = false;
                readingAnswer = false;
                readingQuestion = false;
                try {
                    currentPoints = Integer.parseInt(line.substring("Points:".length()).trim());
                } catch (Exception e) {
                    currentPoints = 10;
                }
            }
            else if (readingAnswer) {
                answerBuffer.append(" ").append(line);
            }
            else if (readingQuestion) {
                questionBuffer.append(" ").append(line);
            }
        }

        if (currentQuestionId != null && currentQuestionText != null && currentCorrectAnswer != null) {
            Question q = new Question(currentQuestionId, currentQuestionText.trim(),
                                    currentCorrectAnswer.trim(), currentPoints,
                                    currentKeywords.toArray(new String[0]));
            assignment.addQuestion(q);
        }

        tempFile.delete();

        if (assignment.getQuestions().isEmpty()) {
            throw new Exception("No valid questions found in file");
        }

        return assignment;
    }

    private Assignment parseCodeAssignmentFromFile(HttpServletRequest request) throws Exception {
        // Get form parameters
        String teacherId = getMultipartValue(request, "teacherId");
        String teacherName = getMultipartValue(request, "teacherName");
        String title = getMultipartValue(request, "title");
        String description = getMultipartValue(request, "description");

        if (teacherId == null || teacherId.isEmpty()) {
            teacherId = teacherName != null ? teacherName : "TEACHER-" + System.currentTimeMillis();
        }

        String assignmentId = "ASSIGN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Part testFilePart = request.getPart("testFile");
        if (testFilePart == null || testFilePart.getSize() == 0) {
            throw new Exception("Test file is required for code assignments");
        }

        String testFileName = testFilePart.getSubmittedFileName();

        File tempTestFile = File.createTempFile("test_file_", "_" + testFileName);
        try (InputStream input = testFilePart.getInputStream();
             FileOutputStream output = new FileOutputStream(tempTestFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }

        String testFileS3Key = String.format("teacher-tests/%s/%s/%s",
                                            assignmentId, teacherId, testFileName);
        String testFileS3Url = s3Helper.uploadFileWithMetadata(tempTestFile, testFileS3Key, teacherId, assignmentId);

        if (testFileS3Url != null) {
            System.out.println("Test file uploaded to S3: " + testFileS3Url);
            logManager.logInfo("Test file uploaded to S3", teacherId, assignmentId, "S3 URL: " + testFileS3Url);
        }

        List<TestCaseInfo> testCases = parseTestCases(tempTestFile);
        tempTestFile.delete();

        if (title == null || title.isEmpty()) {
            title = "Code Assignment - " + testFileName.replaceAll("\\..+$", "");
        }
        if (description == null || description.isEmpty()) {
            description = "Code assignment with " + testCases.size() + " test cases";
        }

        Assignment assignment = new Assignment(assignmentId, title, description, teacherId, teacherName);
        assignment.setAssignmentType("CODE");
        assignment.setTestFileS3Url(testFileS3Url);

        int totalPoints = 0;
        for (TestCaseInfo testCase : testCases) {
            assignment.addTestCase(testCase);
            totalPoints += testCase.getPoints();
        }

        System.out.println(" Code assignment created with " + testCases.size() + " test cases, " + totalPoints + " points");

        return assignment;
    }

    private List<TestCaseInfo> parseTestCases(File testFile) throws IOException {
        List<TestCaseInfo> testCases = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(testFile), "UTF-8"));

        String line;
        String currentTestName = null;
        StringBuilder inputBuffer = new StringBuilder();
        StringBuilder outputBuffer = new StringBuilder();
        int currentPoints = 10;

        boolean readingInput = false;
        boolean readingOutput = false;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty() || line.startsWith("#") || line.startsWith("===")) {
                continue;
            }

            if (line.startsWith("Test ") && line.contains(":")) {
                if (currentTestName != null) {
                    testCases.add(new TestCaseInfo(currentTestName,
                        inputBuffer.toString().trim(),
                        outputBuffer.toString().trim(),
                        currentPoints));
                }

                currentTestName = line.substring(line.indexOf(":") + 1).trim();
                inputBuffer = new StringBuilder();
                outputBuffer = new StringBuilder();
                currentPoints = 10;
                readingInput = false;
                readingOutput = false;
            }
            else if (line.startsWith("Input:")) {
                readingInput = true;
                readingOutput = false;
                String inputStart = line.substring("Input:".length()).trim();
                if (!inputStart.isEmpty()) {
                    inputBuffer.append(inputStart);
                }
            }
            else if (line.startsWith("Expected Output:") || line.startsWith("Output:")) {
                readingInput = false;
                readingOutput = true;
                String outputStart = line.substring(line.indexOf(":") + 1).trim();
                if (!outputStart.isEmpty()) {
                    outputBuffer.append(outputStart);
                }
            }
            else if (line.startsWith("Points:")) {
                readingInput = false;
                readingOutput = false;
                try {
                    currentPoints = Integer.parseInt(line.substring("Points:".length()).trim());
                } catch (Exception e) {
                    currentPoints = 10;
                }
            }
            else if (readingInput) {
                inputBuffer.append("\n").append(line);
            }
            else if (readingOutput) {
                outputBuffer.append("\n").append(line);
            }
        }

        if (currentTestName != null) {
            testCases.add(new TestCaseInfo(currentTestName,
                inputBuffer.toString().trim(),
                outputBuffer.toString().trim(),
                currentPoints));
        }

        reader.close();

        if (testCases.isEmpty()) {
            throw new IOException("No valid test cases found in file");
        }

        return testCases;
    }

    private String getMultipartValue(HttpServletRequest request, String partName) throws Exception {
        Part part = request.getPart(partName);
        if (part == null) return "";

        BufferedReader reader = new BufferedReader(
            new InputStreamReader(part.getInputStream(), "UTF-8"));
        StringBuilder value = new StringBuilder();
        char[] buffer = new char[1024];
        int read;
        while ((read = reader.read(buffer)) != -1) {
            value.append(buffer, 0, read);
        }
        return value.toString().trim();
    }

    private Assignment parseAssignmentFromJson(String json) {
        try {
            String assignmentId = extractJsonValue(json, "assignmentId");
            if (assignmentId == null || assignmentId.isEmpty()) {
                assignmentId = "ASSIGN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            }

            String title = extractJsonValue(json, "title");
            String description = extractJsonValue(json, "description");
            String teacherId = extractJsonValue(json, "teacherId");
            String teacherName = extractJsonValue(json, "teacherName");

            Assignment assignment = new Assignment(assignmentId, title, description, teacherId, teacherName);

            String questionsJson = extractJsonArray(json, "questions");
            if (questionsJson != null) {
                String[] questionObjects = splitJsonArray(questionsJson);
                for (String qJson : questionObjects) {
                    Question question = parseQuestion(qJson);
                    if (question != null) {
                        assignment.addQuestion(question);
                    }
                }
            }

            return assignment;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Question parseQuestion(String json) {
        try {
            String questionId = extractJsonValue(json, "questionId");
            String questionText = extractJsonValue(json, "questionText");
            String correctAnswer = extractJsonValue(json, "correctAnswer");
            String pointsStr = extractJsonValue(json, "points");
            int points = Integer.parseInt(pointsStr);

            String keywordsArray = extractJsonArray(json, "keywords");
            String[] keywords = null;
            if (keywordsArray != null && !keywordsArray.trim().isEmpty()) {
                keywords = keywordsArray.split(",");
                for (int i = 0; i < keywords.length; i++) {
                    keywords[i] = keywords[i].trim().replace("\"", "");
                }
            }

            return new Question(questionId, questionText, correctAnswer, points, keywords);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) return null;

        startIndex += searchKey.length();
        while (startIndex < json.length() && (json.charAt(startIndex) == ' ' || json.charAt(startIndex) == '\t')) {
            startIndex++;
        }

        if (json.charAt(startIndex) == '"') {
            startIndex++;
            int endIndex = json.indexOf('"', startIndex);
            return json.substring(startIndex, endIndex);
        } else {
            int endIndex = startIndex;
            while (endIndex < json.length() &&
                   json.charAt(endIndex) != ',' &&
                   json.charAt(endIndex) != '}' &&
                   json.charAt(endIndex) != ']') {
                endIndex++;
            }
            return json.substring(startIndex, endIndex).trim();
        }
    }

    private String extractJsonArray(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) return null;

        startIndex = json.indexOf('[', startIndex);
        if (startIndex == -1) return null;

        int endIndex = json.indexOf(']', startIndex);
        if (endIndex == -1) return null;

        return json.substring(startIndex + 1, endIndex);
    }

    private String[] splitJsonArray(String arrayContent) {
        java.util.List<String> objects = new java.util.ArrayList<>();
        int braceCount = 0;
        int start = 0;

        for (int i = 0; i < arrayContent.length(); i++) {
            char c = arrayContent.charAt(i);
            if (c == '{') {
                if (braceCount == 0) start = i;
                braceCount++;
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0) {
                    objects.add(arrayContent.substring(start, i + 1));
                }
            }
        }

        return objects.toArray(new String[0]);
    }


    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORSHeaders(response);
        response.setStatus(200);
    }

    private void setCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    /**
     * Extract text content from .docx file
     */
    private String extractTextFromDocx(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        }
    }

    /**
     * Extract text content from .doc file
     */
    private String extractTextFromDoc(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             HWPFDocument document = new HWPFDocument(fis);
             WordExtractor extractor = new WordExtractor(document)) {
            return extractor.getText();
        }
    }
}
