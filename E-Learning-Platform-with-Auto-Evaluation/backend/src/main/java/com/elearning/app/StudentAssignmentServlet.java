package com.elearning.app;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.UUID;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@MultipartConfig(
    location = "/tmp",
    maxFileSize = 10485760,      // 10 MB
    maxRequestSize = 10485760,   // 10 MB
    fileSizeThreshold = 262144   // 256 KB
)
public class StudentAssignmentServlet extends HttpServlet {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("pdf", "doc", "docx", "txt", "zip", "java");

    private FileUploadHelper fileUploadHelper;
    private AssignmentEvaluator assignmentEvaluator;
    private AIEvaluator aiEvaluator;
    private ScoreManager scoreManager;
    private LogManager logManager;
    private AssignmentManager assignmentManager;
    private S3Helper s3Helper;
    private CodeEvaluator codeEvaluator;

    public StudentAssignmentServlet() {
        this.fileUploadHelper = new FileUploadHelper();
        this.assignmentEvaluator = new AssignmentEvaluator();
        this.aiEvaluator = new AIEvaluator();
        this.scoreManager = ScoreManager.getInstance();
        this.logManager = LogManager.getInstance();
        this.assignmentManager = AssignmentManager.getInstance();
        this.s3Helper = new S3Helper();
        this.codeEvaluator = new CodeEvaluator();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setCORSHeaders(response);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String studentName = getPartValue(request, "studentName");
            String studentId = getPartValue(request, "studentId");
            String assignmentId = getPartValue(request, "assignmentId");
            String evaluationMode = getPartValue(request, "evaluationMode"); 

            System.out.println("Student Name: " + studentName);
            System.out.println("Student ID: " + studentId);
            System.out.println("Assignment ID: " + assignmentId);
            System.out.println("Evaluation Mode (Student Choice): " + evaluationMode);

            if (studentName == null || studentName.trim().isEmpty() ||
                studentId == null || studentId.trim().isEmpty()) {
                sendErrorResponse(response, 400, "Student name and ID are required");
                return;
            }

            Assignment assignment = null;
            if (assignmentId != null && !assignmentId.trim().isEmpty()) {
                assignment = assignmentManager.getAssignment(assignmentId);
                if (assignment == null) {
                    System.out.println("Assignment ID provided but not found: " + assignmentId);
                    logManager.logWarning("Assignment not found", studentId, assignmentId,
                                        "Student provided invalid assignment ID");
                }
            } else {
                System.out.println("ℹNo Assignment ID provided - will use blind evaluation");
            }

            Part filePart = request.getPart("assignmentFile");

            if (filePart == null || filePart.getSize() == 0) {
                sendErrorResponse(response, 400, "No file uploaded");
                return;
            }

            String fileName = filePart.getSubmittedFileName();
            if (!isValidFile(fileName, filePart.getSize())) {
                sendErrorResponse(response, 400, "Invalid file type or size exceeded 10MB");
                return;
            }

            String submissionId = UUID.randomUUID().toString();

            logManager.logInfo("File upload started", studentId, submissionId,
                             "Assignment: " + assignmentId + ", File: " + fileName);

            File tempUploadFile = fileUploadHelper.saveTemporaryFile(filePart, submissionId);

            String s3Key = String.format("submissions/%s/%s/%s/%s",
                                        assignmentId != null ? assignmentId : "no-assignment",
                                        studentId, submissionId, fileName);
            String s3Url = s3Helper.uploadFileWithMetadata(tempUploadFile, s3Key, studentId,
                                                          assignmentId != null ? assignmentId : submissionId);

            tempUploadFile.delete();

            if (s3Url == null) {
                System.out.println("S3 upload failed - cannot proceed with evaluation");
                logManager.logError("S3 upload failed", "Student submission cannot be processed");
                sendErrorResponse(response, 500, "File upload failed. Please try again.");
                return;
            }

            System.out.println("File uploaded to S3: " + s3Url);
            logManager.logInfo("File uploaded to S3", studentId, submissionId, "S3 Key: " + s3Key);

            File evaluationFile = s3Helper.downloadFileToTemp(s3Key, submissionId + "_" + fileName);

            if (evaluationFile == null) {
                System.out.println("Failed to download file from S3 for evaluation");
                logManager.logError("S3 download failed", "Cannot evaluate submission");
                sendErrorResponse(response, 500, "File download failed. Please try again.");
                return;
            }

            System.out.println("File downloaded from S3 for evaluation: " + evaluationFile.getName());

            EvaluationResult result;

            System.out.println("=== EVALUATION START ===");
            System.out.println("Assignment ID: " + assignmentId);
            System.out.println("Assignment found: " + (assignment != null));
            if (assignment != null) {
                System.out.println("Assignment Type: " + assignment.getAssignmentType());
                System.out.println("Test Cases Count: " + assignment.getTestCases().size());
            }
            System.out.println("======================");

            if (assignment != null && "CODE".equalsIgnoreCase(assignment.getAssignmentType())) {
                boolean useAI = "AI".equalsIgnoreCase(evaluationMode);

                System.out.println("Code assignment detected - Compiling and testing");
                logManager.logInfo("Code evaluation started", studentId, submissionId, "Assignment: " + assignmentId);

                List<CodeEvaluator.TestCase> testCases = new ArrayList<>();
                for (TestCaseInfo testInfo : assignment.getTestCases()) {
                    testCases.add(new CodeEvaluator.TestCase(
                        testInfo.getTestName(),
                        testInfo.getInput(),
                        testInfo.getExpectedOutput(),
                        testInfo.getPoints()
                    ));
                }

                CodeEvaluator.CodeEvaluationResult codeResult = codeEvaluator.evaluateCode(
                    evaluationFile, testCases, studentId, submissionId);

                if (useAI) {
                    System.out.println("Enhancing code evaluation with AI feedback");
                    logManager.logInfo("AI code evaluation started", studentId, submissionId, "Enhancing test results with AI analysis");

                    String studentCode = new String(java.nio.file.Files.readAllBytes(evaluationFile.toPath()));

                    result = aiEvaluator.evaluateCodeWithAI(studentCode, codeResult, assignment);
                } else {
                    result = new EvaluationResult(codeResult.score, codeResult.feedback);
                }

            } else if (assignment == null && fileName.endsWith(".java")) {
                boolean useAI = "AI".equalsIgnoreCase(evaluationMode);

                if (useAI) {
                    System.out.println("Code submission without assignment - Using blind AI evaluation");
                    logManager.logInfo("Blind AI code evaluation", studentId, submissionId, "No test cases available");

                    String studentCode = new String(java.nio.file.Files.readAllBytes(evaluationFile.toPath()));

                    result = aiEvaluator.evaluateCodeBlindWithAI(studentCode, fileName);

                    String cautionNote = "\n\nCAUTION: This code was evaluated by AI without test cases. " +
                                       "The score is based on code quality, logic, and best practices. " +
                                       "Final scores should be confirmed by your instructor.";
                    result = new EvaluationResult(result.getScore(), result.getFeedback() + cautionNote);
                } else {
                    System.out.println("Code submission without assignment and manual mode - Cannot evaluate");
                    result = new EvaluationResult(0, "Code assignments require test cases for manual evaluation. " +
                                                    "Please provide correct Assignment ID or use AI evaluation mode.");
                }

            } else {
                boolean useAI = "AI".equalsIgnoreCase(evaluationMode);

                if (useAI && assignment != null) {
                System.out.println("Student chose AI evaluation with Claude 3.5 Sonnet v2");
                logManager.logInfo("AI evaluation started (student choice)", studentId, submissionId, "Using AWS Bedrock with teacher reference");

                String studentAnswerText = assignmentEvaluator.extractTextFromFile(evaluationFile, fileName);

                result = aiEvaluator.evaluateWithAI(studentAnswerText, assignment);

            } else if (useAI && assignment == null) {
                System.out.println("Student chose AI evaluation - No teacher assignment found");
                logManager.logInfo("Blind AI evaluation (student choice)", studentId, submissionId, "No teacher reference available");

                String studentAnswerText = assignmentEvaluator.extractTextFromFile(evaluationFile, fileName);
                result = aiEvaluator.evaluateWithoutReference(studentAnswerText);

                String cautionNote = "\n\nCAUTION: This assignment was evaluated by AI without teacher's reference assignment. " +
                                   "The score is generated based on AI's assessment of quality, clarity, and understanding. " +
                                   "Final scores should be confirmed by your instructor.";
                result = new EvaluationResult(result.getScore(), result.getFeedback() + cautionNote);

            } else if (!useAI && assignment != null) {
                System.out.println("Student chose manual evaluation with keyword matching");
                result = assignmentEvaluator.evaluateWithAnswerKey(evaluationFile, fileName, assignment);

            } else {
                System.out.println("Student chose manual evaluation but no assignment found");
                String studentAnswerText = assignmentEvaluator.extractTextFromFile(evaluationFile, fileName);

                int score = Math.min(100, studentAnswerText.length() / 50);
                String feedback = "Assignment evaluated without teacher reference. Score based on length and structure. " +
                                "Please provide correct Assignment ID for accurate evaluation.";
                result = new EvaluationResult(score, feedback);
                }
            }

            StudentScore score = new StudentScore(submissionId, studentId, studentName,
                                                 result.getScore(), result.getFeedback(), fileName, s3Url);
            scoreManager.saveScore(score);

            if (evaluationFile != null && evaluationFile.exists()) {
                evaluationFile.delete();
                System.out.println("✓ Cleaned up evaluation temp file");
            }

            logManager.logInfo("Assignment evaluated successfully", studentId, assignmentId,
                              "Score: " + result.getScore());

            StringBuilder jsonResponse = new StringBuilder();
            jsonResponse.append("{\"success\": true, ");
            jsonResponse.append("\"assignmentId\": \"").append(escapeJson(assignmentId)).append("\", ");
            jsonResponse.append("\"submissionId\": \"").append(escapeJson(submissionId)).append("\", ");
            jsonResponse.append("\"score\": ").append(result.getScore()).append(", ");
            jsonResponse.append("\"feedback\": \"").append(escapeJson(result.getFeedback())).append("\"");

            if (result.getStrengths() != null) {
                jsonResponse.append(", \"strengths\": \"").append(escapeJson(result.getStrengths())).append("\"");
            }

            if (result.getImprovements() != null) {
                jsonResponse.append(", \"improvements\": \"").append(escapeJson(result.getImprovements())).append("\"");
            }

            jsonResponse.append(", \"s3Url\": \"").append(escapeJson(s3Url != null ? s3Url : "")).append("\"}");

            response.setStatus(200);
            response.getWriter().write(jsonResponse.toString());

        } catch (Exception e) {
            logManager.logError("File upload failed", e.getMessage());
            sendErrorResponse(response, 500, "Internal server error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"status\":\"API Ready\",\"endpoint\":\"/upload\",\"message\":\"Use POST to upload assignments\"}");
    }

    private String getPartValue(HttpServletRequest request, String partName) throws IOException, ServletException {
        Part part = request.getPart(partName);
        if (part == null) {
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
        StringBuilder value = new StringBuilder();
        char[] buffer = new char[1024];
        int read;
        while ((read = reader.read(buffer)) != -1) {
            value.append(buffer, 0, read);
        }
        return value.toString();
    }

    private boolean isValidFile(String fileName, long fileSize) {
        if (fileName == null || fileSize > MAX_FILE_SIZE) {
            return false;
        }

        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        String jsonError = String.format("{\"success\": false, \"error\": \"%s\"}", escapeJson(message));
        response.getWriter().write(jsonError);
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }


    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORSHeaders(response);
        response.setStatus(200);
    }

    private void setCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}