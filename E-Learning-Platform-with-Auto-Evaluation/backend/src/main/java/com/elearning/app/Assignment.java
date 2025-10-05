package com.elearning.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Assignment {
    private String assignmentId;
    private String title;
    private String description;
    private String teacherId;
    private String teacherName;
    private List<Question> questions;
    private String createdAt;
    private int totalPoints;
    private String s3Url;
    private String assignmentType; // "TEXT" or "CODE"
    private List<TestCaseInfo> testCases;
    private String testFileS3Url;

    public Assignment(String assignmentId, String title, String description,
                     String teacherId, String teacherName) {
        this.assignmentId = assignmentId;
        this.title = title;
        this.description = description;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.questions = new ArrayList<>();
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.totalPoints = 0;
        this.s3Url = null;
        this.assignmentType = "TEXT"; // Default
        this.testCases = new ArrayList<>();
        this.testFileS3Url = null;
    }

    public void addQuestion(Question question) {
        questions.add(question);
        totalPoints += question.getPoints();
    }

    // Getters
    public String getAssignmentId() {
        return assignmentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public String getS3Url() {
        return s3Url;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public String getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(String assignmentType) {
        this.assignmentType = assignmentType;
    }

    public List<TestCaseInfo> getTestCases() {
        return testCases;
    }

    public void addTestCase(TestCaseInfo testCase) {
        this.testCases.add(testCase);
    }

    public String getTestFileS3Url() {
        return testFileS3Url;
    }

    public void setTestFileS3Url(String testFileS3Url) {
        this.testFileS3Url = testFileS3Url;
    }

    // Convert to JSON
    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"assignmentId\": \"").append(assignmentId).append("\",");
        json.append("\"title\": \"").append(escapeJson(title)).append("\",");
        json.append("\"description\": \"").append(escapeJson(description)).append("\",");
        json.append("\"teacherId\": \"").append(teacherId).append("\",");
        json.append("\"teacherName\": \"").append(escapeJson(teacherName)).append("\",");
        json.append("\"totalPoints\": ").append(totalPoints).append(",");
        json.append("\"createdAt\": \"").append(createdAt).append("\",");
        json.append("\"s3Url\": \"").append(s3Url != null ? escapeJson(s3Url) : "").append("\",");
        json.append("\"assignmentType\": \"").append(assignmentType).append("\",");
        json.append("\"testFileS3Url\": \"").append(testFileS3Url != null ? escapeJson(testFileS3Url) : "").append("\",");
        json.append("\"testCaseCount\": ").append(testCases.size()).append(",");

        json.append("\"testCases\": [");
        for (int i = 0; i < testCases.size(); i++) {
            if (i > 0) json.append(",");
            json.append(testCases.get(i).toJson());
        }
        json.append("],");

        json.append("\"questions\": [");
        for (int i = 0; i < questions.size(); i++) {
            if (i > 0) json.append(",");
            json.append(questions.get(i).toJson());
        }

        json.append("]}");
        return json.toString();
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
