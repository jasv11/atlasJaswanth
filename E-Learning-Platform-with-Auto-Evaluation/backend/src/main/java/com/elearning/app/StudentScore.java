package com.elearning.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StudentScore {
    private String assignmentId;
    private String studentId;
    private String studentName;
    private int score;
    private String feedback;
    private String fileName;
    private String submissionTime;
    private String s3Url;  

    public StudentScore(String assignmentId, String studentId, String studentName,
                       int score, String feedback, String fileName) {
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.score = score;
        this.feedback = feedback;
        this.fileName = fileName;
        this.submissionTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.s3Url = null;
    }

    public StudentScore(String assignmentId, String studentId, String studentName,
                       int score, String feedback, String fileName, String s3Url) {
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.score = score;
        this.feedback = feedback;
        this.fileName = fileName;
        this.submissionTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.s3Url = s3Url;
    }

    // Getters
    public String getAssignmentId() { return assignmentId; }
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public int getScore() { return score; }
    public String getFeedback() { return feedback; }
    public String getFileName() { return fileName; }
    public String getSubmissionTime() { return submissionTime; }
    public String getS3Url() { return s3Url; }

    // Setters
    public void setScore(int score) { this.score = score; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public void setS3Url(String s3Url) { this.s3Url = s3Url; }

    public String toJson() {
        return String.format(
            "{\"assignmentId\":\"%s\",\"studentId\":\"%s\",\"studentName\":\"%s\"," +
            "\"score\":%d,\"feedback\":\"%s\",\"fileName\":\"%s\",\"submissionTime\":\"%s\"," +
            "\"s3Url\":\"%s\"}",
            escapeJson(assignmentId), escapeJson(studentId), escapeJson(studentName),
            score, escapeJson(feedback), escapeJson(fileName), escapeJson(submissionTime),
            escapeJson(s3Url != null ? s3Url : "")
        );
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}