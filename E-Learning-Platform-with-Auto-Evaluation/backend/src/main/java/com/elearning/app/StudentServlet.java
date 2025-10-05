package com.elearning.app;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class StudentServlet extends HttpServlet {

    private static final ConcurrentHashMap<String, StudentData> studentsCache = new ConcurrentHashMap<>();
    private DynamoDBHelper dynamoDBHelper;
    private Table studentsTable;
    private LogManager logManager;
    private static final Random random = new Random();
    private static final String STUDENTS_TABLE_NAME = "Students";

    static class StudentData {
        String studentId;
        String name;
        String createdAt;

        public StudentData(String studentId, String name, String createdAt) {
            this.studentId = studentId;
            this.name = name;
            this.createdAt = createdAt;
        }
    }

    public StudentServlet() {
        this.dynamoDBHelper = new DynamoDBHelper();
        this.logManager = LogManager.getInstance();

        if (dynamoDBHelper.isConnected()) {
            try {
                this.studentsTable = dynamoDBHelper.getTable(STUDENTS_TABLE_NAME);
                System.out.println("StudentServlet connected to DynamoDB Students table");
            } catch (Exception e) {
                System.out.println("Warning: Could not get Students table: " + e.getMessage());
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        setCORSHeaders(response);

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            sendErrorResponse(response, 400, "Student ID is required");
            return;
        }

        String studentId = pathInfo.substring(1); 

        try {
            StudentData student = studentsCache.get(studentId);

            if (student == null && studentsTable != null) {
                try {
                    Item item = studentsTable.getItem("studentId", studentId);
                    if (item != null) {
                        student = new StudentData(
                            item.getString("studentId"),
                            item.getString("name"),
                            item.getString("createdAt")
                        );
                        studentsCache.put(studentId, student);
                        System.out.println("Student loaded from DynamoDB: " + studentId);
                    }
                } catch (Exception e) {
                    System.out.println("Error reading from DynamoDB: " + e.getMessage());
                }
            }

            if (student != null) {
                String jsonResponse = String.format(
                    "{\"success\":true,\"studentId\":\"%s\",\"name\":\"%s\",\"createdAt\":\"%s\"}",
                    student.studentId, student.name, student.createdAt
                );

                response.setStatus(200);
                response.getWriter().write(jsonResponse);

                logManager.logInfo("Student found: " + studentId, studentId, null, "Login successful");
            } else {
                sendErrorResponse(response, 404, "Student not found");
                logManager.logWarning("Student not found: " + studentId, studentId, null, "Login attempt failed");
            }
        } catch (Exception e) {
            logManager.logError("Error fetching student: " + e.getMessage(), e.toString());
            sendErrorResponse(response, 500, "Error fetching student data");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        setCORSHeaders(response);

        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/register")) {
            handleRegistration(request, response);
        } else {
            sendErrorResponse(response, 400, "Invalid endpoint. Use /api/student/register");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORSHeaders(response);
        response.setStatus(200);
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            String jsonBody = sb.toString();
            System.out.println("Registration request body: " + jsonBody);

            String name = extractJsonValue(jsonBody, "name");

            if (name == null || name.trim().isEmpty()) {
                sendErrorResponse(response, 400, "Name is required");
                return;
            }

            String studentId = generateStudentId();
            String createdAt = new java.util.Date().toString();

            StudentData student = new StudentData(studentId, name.trim(), createdAt);

            if (studentsTable != null) {
                try {
                    Item item = new Item()
                        .withPrimaryKey("studentId", studentId)
                        .withString("name", name.trim())
                        .withString("createdAt", createdAt);

                    studentsTable.putItem(item);
                    System.out.println("Student saved to DynamoDB: " + studentId);
                } catch (Exception e) {
                    System.out.println("Warning: Failed to save to DynamoDB: " + e.getMessage());
                }
            }

            studentsCache.put(studentId, student);

            System.out.println("New student registered: " + studentId + " - " + name);
            logManager.logInfo("New student registered: " + studentId + " (" + name + ")", studentId, null, "Registration successful");

            String jsonResponse = String.format(
                "{\"success\":true,\"studentId\":\"%s\",\"name\":\"%s\",\"createdAt\":\"%s\"}",
                studentId, name.trim(), createdAt
            );

            response.setStatus(200);
            response.getWriter().write(jsonResponse);

        } catch (Exception e) {
            logManager.logError("Registration failed: " + e.getMessage(), e.toString());
            sendErrorResponse(response, 500, "Registration failed: " + e.getMessage());
        }
    }

    private String generateStudentId() {
        String studentId;
        int attempts = 0;

        do {
            studentId = String.format("STU%05d", random.nextInt(100000));
            attempts++;

            if (studentsCache.containsKey(studentId)) {
                continue;
            }

            if (studentsTable != null) {
                try {
                    Item existing = studentsTable.getItem("studentId", studentId);
                    if (existing == null) {
                        break; 
                    }
                } catch (Exception e) {
                    break;
                }
            } else {
                break;
            }

            if (attempts > 10) {
                studentId = "STU" + (System.currentTimeMillis() % 100000);
                break;
            }

        } while (attempts < 20);

        return studentId;
    }

    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);

        if (keyIndex == -1) return null;

        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex == -1) return null;

        int startQuote = json.indexOf("\"", colonIndex);
        if (startQuote == -1) return null;

        int endQuote = json.indexOf("\"", startQuote + 1);
        if (endQuote == -1) return null;

        return json.substring(startQuote + 1, endQuote);
    }

    private void setCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        String errorJson = String.format("{\"success\":false,\"error\":\"%s\"}", message);
        response.getWriter().write(errorJson);
    }
}
