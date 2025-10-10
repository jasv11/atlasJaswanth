package com.elearning.app;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;

public class AssignmentManager {

    private static AssignmentManager instance;
    private ConcurrentHashMap<String, Assignment> assignmentCache;
    private DynamoDBHelper dynamoDBHelper;
    private Table assignmentsTable;
    private static final String ASSIGNMENTS_TABLE_NAME = "Assignments";

    private AssignmentManager() {
        this.assignmentCache = new ConcurrentHashMap<>();
        this.dynamoDBHelper = new DynamoDBHelper();

        if (dynamoDBHelper.isConnected()) {
            try {
                this.assignmentsTable = dynamoDBHelper.getTable(ASSIGNMENTS_TABLE_NAME);
                System.out.println("AssignmentManager connected to DynamoDB Assignments table");

                loadAssignmentsFromDynamoDB();
            } catch (Exception e) {
                System.out.println("Warning: Could not get Assignments table: " + e.getMessage());
            }
        } else {
            System.out.println("AssignmentManager initialized with in-memory cache only");
        }
    }

    public static synchronized AssignmentManager getInstance() {
        if (instance == null) {
            instance = new AssignmentManager();
        }
        return instance;
    }

    public void saveAssignment(Assignment assignment) {
        assignmentCache.put(assignment.getAssignmentId(), assignment);
        System.out.println("Assignment cached: " + assignment.getAssignmentId() + " - " + assignment.getTitle());

        if (assignmentsTable != null) {
            try {
                Item item = new Item()
                        .withPrimaryKey("assignmentId", assignment.getAssignmentId())
                        .withString("title", assignment.getTitle())
                        .withString("description", assignment.getDescription())
                        .withString("teacherId", assignment.getTeacherId())
                        .withString("teacherName", assignment.getTeacherName())
                        .withString("assignmentType", assignment.getAssignmentType())
                        .withNumber("totalPoints", assignment.getTotalPoints())
                        .withString("createdAt", assignment.getCreatedAt())

                        .withString("questionsFileS3Url", assignment.getS3Url() != null ? assignment.getS3Url() : "")
                        .withString("testCasesFileS3Url", assignment.getTestFileS3Url() != null ? assignment.getTestFileS3Url() : "")

                        .withNumber("testCaseCount", assignment.getTestCases().size())
                        .withNumber("questionCount", assignment.getQuestions().size());

                assignmentsTable.putItem(item);
                System.out.println("Assignment metadata saved to DynamoDB: " + assignment.getAssignmentId());
                System.out.println("   - Questions file S3: " + (assignment.getS3Url() != null ? assignment.getS3Url() : "N/A"));
                System.out.println("   - Test cases file S3: " + (assignment.getTestFileS3Url() != null ? assignment.getTestFileS3Url() : "N/A"));
            } catch (Exception e) {
                System.out.println("Warning: Failed to save assignment to DynamoDB: " + e.getMessage());
            }
        }
    }

    public Assignment getAssignment(String assignmentId) {
        Assignment assignment = assignmentCache.get(assignmentId);
        if (assignment != null) {
            System.out.println("✓ Assignment retrieved from cache: " + assignmentId);
            return assignment;
        }

        if (assignmentsTable != null) {
            try {
                System.out.println("⚠️  Assignment not in cache, fetching from DynamoDB: " + assignmentId);
                Item item = assignmentsTable.getItem("assignmentId", assignmentId);

                if (item != null) {
                    assignment = reconstructAssignmentFromMetadata(item);

                    if (assignment != null) {
                        assignmentCache.put(assignmentId, assignment);
                        System.out.println("✓ Assignment metadata loaded from DynamoDB and cached: " + assignmentId);
                        System.out.println("   Type: " + assignment.getAssignmentType() +
                                         " | Questions: " + assignment.getQuestions().size() +
                                         " | Test Cases: " + assignment.getTestCases().size());
                        return assignment;
                    }
                }
            } catch (Exception e) {
                System.out.println("✗ Error loading assignment from DynamoDB: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("✗ Assignment not found: " + assignmentId);
        return null;
    }

    /**
     * Reconstruct Assignment from DynamoDB metadata (new approach)
     * Questions and test cases will be loaded from S3 when needed
     */
    private Assignment reconstructAssignmentFromMetadata(Item item) {
        try {
            String assignmentId = item.getString("assignmentId");
            String title = item.getString("title");
            String description = item.getString("description");
            String teacherId = item.getString("teacherId");
            String teacherName = item.getString("teacherName");
            String assignmentType = item.getString("assignmentType");
            String questionsFileS3Url = item.getString("questionsFileS3Url");
            String testCasesFileS3Url = item.getString("testCasesFileS3Url");

            Assignment assignment = new Assignment(assignmentId, title, description, teacherId, teacherName);
            assignment.setAssignmentType(assignmentType);
            assignment.setS3Url(questionsFileS3Url);
            assignment.setTestFileS3Url(testCasesFileS3Url);

            S3Helper s3Helper = new S3Helper();

            if ("CODE".equalsIgnoreCase(assignmentType) && testCasesFileS3Url != null && !testCasesFileS3Url.isEmpty()) {
                System.out.println("   Loading test cases from S3: " + testCasesFileS3Url);

                String s3Key = extractS3KeyFromUrl(testCasesFileS3Url);
                String testCasesContent = s3Helper.downloadFileAsString(s3Key);

                if (testCasesContent != null) {
                    List<TestCaseInfo> testCases = parseTestCasesFromContent(testCasesContent);
                    for (TestCaseInfo tc : testCases) {
                        assignment.addTestCase(tc);
                    }
                    System.out.println("   ✓ Loaded " + testCases.size() + " test cases from S3");
                }
            }

            if ("TEXT".equalsIgnoreCase(assignmentType) && questionsFileS3Url != null && !questionsFileS3Url.isEmpty()) {
                System.out.println("Loading questions from S3: " + questionsFileS3Url);

                String s3Key = extractS3KeyFromUrl(questionsFileS3Url);
                String questionsContent = s3Helper.downloadFileAsString(s3Key);

                if (questionsContent != null) {
                    List<Question> questions = parseQuestionsFromContent(questionsContent);
                    for (Question q : questions) {
                        assignment.addQuestion(q);
                    }
                    System.out.println("Loaded " + questions.size() + " questions from S3");
                }
            }

            return assignment;

        } catch (Exception e) {
            System.out.println("Error reconstructing assignment from metadata: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    
    private String extractS3KeyFromUrl(String s3Url) {
        if (s3Url == null) return null;

        if (!s3Url.startsWith("http")) {
            return s3Url;
        }

        try {
           
            int keyStart = s3Url.indexOf(".com/");
            if (keyStart > 0) {
                return s3Url.substring(keyStart + 5); // Skip ".com/"
            }

        
            keyStart = s3Url.indexOf("elearning-assignments/");
            if (keyStart > 0) {
                return s3Url.substring(keyStart + "elearning-assignments/".length());
            }
        } catch (Exception e) {
            System.out.println("Error extracting S3 key from URL: " + e.getMessage());
        }

        return s3Url; 
    }

    /**
     * Parse test cases from file content
     */
    private List<TestCaseInfo> parseTestCasesFromContent(String content) {
        List<TestCaseInfo> testCases = new ArrayList<>();

        String[] sections = content.split("Test \\d+:");
        for (int i = 1; i < sections.length; i++) {
            try {
                String section = sections[i].trim();
                String[] lines = section.split("\n");

                String testName = lines[0].trim();
                String input = "";
                String expectedOutput = "";
                int points = 10;

                for (int j = 1; j < lines.length; j++) {
                    String line = lines[j].trim();
                    if (line.startsWith("Input:")) {
                        StringBuilder inputBuilder = new StringBuilder();
                        for (int k = j + 1; k < lines.length && !lines[k].trim().startsWith("Expected Output:"); k++) {
                            if (inputBuilder.length() > 0) inputBuilder.append("\\n");
                            inputBuilder.append(lines[k].trim());
                        }
                        input = inputBuilder.toString();
                    } else if (line.startsWith("Expected Output:")) {
                        expectedOutput = lines[j].substring("Expected Output:".length()).trim();
                    } else if (line.startsWith("Points:")) {
                        points = Integer.parseInt(line.substring("Points:".length()).trim());
                    }
                }

                testCases.add(new TestCaseInfo(testName, input, expectedOutput, points));
            } catch (Exception e) {
                System.out.println("Error parsing test case section: " + e.getMessage());
            }
        }

        return testCases;
    }

    /**
     * Parse questions from file content
     */
    private List<Question> parseQuestionsFromContent(String content) {
        List<Question> questions = new ArrayList<>();

        String[] sections = content.split("Question \\d+:");
        for (int i = 1; i < sections.length; i++) {
            try {
                String section = sections[i].trim();
                String[] lines = section.split("\n");

                String questionText = lines[0].trim();
                String answer = "";
                int points = 10;

                for (int j = 1; j < lines.length; j++) {
                    String line = lines[j].trim();
                    if (line.startsWith("Answer:")) {
                        answer = line.substring("Answer:".length()).trim();
                    } else if (line.startsWith("Points:")) {
                        points = Integer.parseInt(line.substring("Points:".length()).trim());
                    }
                }

                questions.add(new Question("Q" + i, questionText, answer, points, new String[0]));
            } catch (Exception e) {
                System.out.println("Error parsing question section: " + e.getMessage());
            }
        }

        return questions;
    }

    private Assignment parseAssignmentFromJson(String json) {
        try {
            String assignmentId = extractJsonValue(json, "assignmentId");
            String title = extractJsonValue(json, "title");
            String description = extractJsonValue(json, "description");
            String teacherId = extractJsonValue(json, "teacherId");
            String teacherName = extractJsonValue(json, "teacherName");
            String assignmentType = extractJsonValue(json, "assignmentType");
            String s3Url = extractJsonValue(json, "s3Url");
            String testFileS3Url = extractJsonValue(json, "testFileS3Url");

            Assignment assignment = new Assignment(assignmentId, title, description, teacherId, teacherName);

            if (assignmentType != null && !assignmentType.isEmpty()) {
                assignment.setAssignmentType(assignmentType);
            }
            if (s3Url != null && !s3Url.isEmpty()) {
                assignment.setS3Url(s3Url);
            }
            if (testFileS3Url != null && !testFileS3Url.isEmpty()) {
                assignment.setTestFileS3Url(testFileS3Url);
            }

            String testCasesJson = extractJsonArray(json, "testCases");
            if (testCasesJson != null && !testCasesJson.trim().isEmpty()) {
                String[] testCaseObjects = splitJsonArray(testCasesJson);
                for (String tcJson : testCaseObjects) {
                    TestCaseInfo testCase = parseTestCase(tcJson);
                    if (testCase != null) {
                        assignment.addTestCase(testCase);
                    }
                }
                System.out.println("✓ Parsed " + testCaseObjects.length + " test cases from JSON");
            }

            return assignment;

        } catch (Exception e) {
            System.out.println("Error parsing assignment JSON: " + e.getMessage());
            return null;
        }
    }

    private String extractJsonValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int startIndex = json.indexOf(searchKey);
            if (startIndex == -1) return null;

            startIndex += searchKey.length();
            while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
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
        } catch (Exception e) {
            return null;
        }
    }

    private String extractJsonArray(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int startIndex = json.indexOf(searchKey);
            if (startIndex == -1) return null;

            startIndex = json.indexOf('[', startIndex);
            if (startIndex == -1) return null;

            int endIndex = json.indexOf(']', startIndex);
            if (endIndex == -1) return null;

            return json.substring(startIndex + 1, endIndex);
        } catch (Exception e) {
            return null;
        }
    }

    private String[] splitJsonArray(String arrayContent) {
        List<String> objects = new ArrayList<>();
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

    private TestCaseInfo parseTestCase(String json) {
        try {
            String testName = extractJsonValue(json, "testName");
            String input = extractJsonValue(json, "input");
            String expectedOutput = extractJsonValue(json, "expectedOutput");
            String pointsStr = extractJsonValue(json, "points");
            int points = pointsStr != null ? Integer.parseInt(pointsStr) : 10;

            return new TestCaseInfo(testName, input, expectedOutput, points);
        } catch (Exception e) {
            System.out.println("Error parsing test case: " + e.getMessage());
            return null;
        }
    }

    public List<Assignment> getAllAssignments() {
        return new ArrayList<>(assignmentCache.values());
    }

    public String getAllAssignmentsAsJson() {
        try {
            List<Assignment> assignments = getAllAssignments();

            if (assignments.isEmpty() && assignmentsTable != null) {
                System.out.println("Cache empty - loading assignments from DynamoDB");
                return getAssignmentsFromDynamoDB();
            }

            StringBuilder jsonResult = new StringBuilder();
            jsonResult.append("{\"assignments\": [");

            for (int i = 0; i < assignments.size(); i++) {
                if (i > 0) {
                    jsonResult.append(",");
                }
                jsonResult.append(assignments.get(i).toJson());
            }

            jsonResult.append("], \"count\": ").append(assignments.size()).append("}");
            return jsonResult.toString();

        } catch (Exception e) {
            System.out.println("Error getting all assignments: " + e.getMessage());
            return "{\"assignments\": [], \"count\": 0, \"error\": \"" + e.getMessage() + "\"}";
        }
    }

    private String getAssignmentsFromDynamoDB() {
        try {
            StringBuilder jsonResult = new StringBuilder();
            jsonResult.append("{\"assignments\": [");

            ScanSpec scanSpec = new ScanSpec();
            ItemCollection<ScanOutcome> items = assignmentsTable.scan(scanSpec);

            boolean first = true;
            for (Item item : items) {
                if (!first) {
                    jsonResult.append(",");
                }
                first = false;

                jsonResult.append("{");
                jsonResult.append("\"assignmentId\":\"").append(item.getString("assignmentId")).append("\",");
                jsonResult.append("\"title\":\"").append(escapeJson(item.getString("title"))).append("\",");
                jsonResult.append("\"description\":\"").append(escapeJson(item.getString("description"))).append("\",");
                jsonResult.append("\"teacherId\":\"").append(item.getString("teacherId")).append("\",");
                jsonResult.append("\"teacherName\":\"").append(escapeJson(item.getString("teacherName"))).append("\",");
                jsonResult.append("\"assignmentType\":\"").append(item.getString("assignmentType")).append("\",");
                jsonResult.append("\"totalPoints\":").append(item.getNumber("totalPoints")).append(",");
                jsonResult.append("\"createdAt\":\"").append(item.getString("createdAt")).append("\",");
                jsonResult.append("\"questionsFileS3Url\":\"").append(escapeJson(item.getString("questionsFileS3Url"))).append("\",");
                jsonResult.append("\"testCasesFileS3Url\":\"").append(escapeJson(item.getString("testCasesFileS3Url"))).append("\",");
                jsonResult.append("\"testCaseCount\":").append(item.getNumber("testCaseCount")).append(",");
                jsonResult.append("\"questionCount\":").append(item.getNumber("questionCount"));
                jsonResult.append("}");
            }

            jsonResult.append("], \"count\": ").append(assignmentsTable.scan(scanSpec).getAccumulatedItemCount()).append("}");
            return jsonResult.toString();

        } catch (Exception e) {
            System.out.println("Error getting assignments from DynamoDB: " + e.getMessage());
            return "{\"assignments\": [], \"count\": 0, \"error\": \"" + e.getMessage() + "\"}";
        }
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }

    public void deleteAssignment(String assignmentId) {
        assignmentCache.remove(assignmentId);
        System.out.println("Assignment deleted: " + assignmentId);
    }

    public void clearCache() {
        assignmentCache.clear();
        System.out.println("Assignment cache cleared");
    }

    public int getCacheSize() {
        return assignmentCache.size();
    }

    public String getCacheStats() {
        return "{\"cacheSize\": " + getCacheSize() + ", \"status\": \"active\"}";
    }

    private void loadAssignmentsFromDynamoDB() {
        if (assignmentsTable == null) {
            return;
        }

        try {
            System.out.println("Loading assignments metadata from DynamoDB...");
            ScanSpec scanSpec = new ScanSpec();
            ItemCollection<ScanOutcome> items = assignmentsTable.scan(scanSpec);

            int count = 0;
            for (Item item : items) {
                try {
                    String assignmentId = item.getString("assignmentId");
                    System.out.println("  Found assignment: " + assignmentId);
                    count++;
                } catch (Exception e) {
                    System.out.println("Error loading assignment metadata: " + e.getMessage());
                }
            }

            System.out.println("✓ Loaded " + count + " assignment metadata entries from DynamoDB");
            System.out.println("  (Content will be loaded from S3 on first access)");
        } catch (Exception e) {
            System.out.println("Error loading assignments from DynamoDB: " + e.getMessage());
        }
    }
}
