package com.elearning.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;
import java.util.ArrayList;

public class LogManager {

    private static LogManager instance;
    private ConcurrentLinkedQueue<LogEntry> logCache;
    private DynamoDBHelper dynamoDBHelper;
    private static final int MAX_CACHE_SIZE = 1000;

    private LogManager() {
        this.logCache = new ConcurrentLinkedQueue<>();
        this.dynamoDBHelper = new DynamoDBHelper();
        System.out.println("LogManager initialized with in-memory cache");
    }

    public static synchronized LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager();
        }
        return instance;
    }

    public void logInfo(String message, String studentId, String assignmentId, String details) {
        addLog("INFO", message, studentId, assignmentId, details);
    }

    public void logError(String message, String details) {
        addLog("ERROR", message, null, null, details);
    }

    public void logWarning(String message, String studentId, String assignmentId, String details) {
        addLog("WARNING", message, studentId, assignmentId, details);
    }

    private void addLog(String level, String message, String studentId, String assignmentId, String details) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LogEntry logEntry = new LogEntry(timestamp, level, message, studentId, assignmentId, details);

            logCache.add(logEntry);

            if (logCache.size() > MAX_CACHE_SIZE) {
                logCache.poll(); // Remove oldest entry
            }

            System.out.println(String.format("[%s] %s: %s - Student: %s, Assignment: %s",
                    timestamp, level, message,
                    studentId != null ? studentId : "N/A",
                    assignmentId != null ? assignmentId : "N/A"));

            dynamoDBHelper.saveLog(level, message, studentId, assignmentId, details);

        } catch (Exception e) {
            System.out.println("Error adding log: " + e.getMessage());
        }
    }

    public List<LogEntry> getAllLogsFromCache() {
        return new ArrayList<>(logCache);
    }

    public String getAllLogsAsJson() {
        try {
            List<LogEntry> logs = getAllLogsFromCache();

            // If cache has logs, return them
            if (!logs.isEmpty()) {
                StringBuilder jsonResult = new StringBuilder();
                jsonResult.append("{\"logs\": [");

                for (int i = 0; i < logs.size(); i++) {
                    if (i > 0) {
                        jsonResult.append(",");
                    }
                    jsonResult.append(logs.get(i).toJson());
                }

                jsonResult.append("], \"count\": ").append(logs.size()).append("}");
                return jsonResult.toString();
            }

            try {
                return dynamoDBHelper.getAllLogs();
            } catch (Exception dbException) {
                System.out.println("Error retrieving logs: " + dbException.getMessage());
                return "{\"logs\": [], \"count\": 0, \"message\": \"No logs available\"}";
            }

        } catch (Exception e) {
            System.out.println("Error getting all logs: " + e.getMessage());
            return "{\"logs\": [], \"count\": 0, \"error\": \"" + e.getMessage() + "\"}";
        }
    }

    public void clearCache() {
        logCache.clear();
        System.out.println("Log cache cleared");
    }

    public int getCacheSize() {
        return logCache.size();
    }

    public String getLogStats() {
        return String.format("{\"cacheSize\": %d, \"maxCacheSize\": %d, \"dynamoConnected\": %s}",
                getCacheSize(), MAX_CACHE_SIZE, dynamoDBHelper.isConnected());
    }

    public static class LogEntry {
        private String timestamp;
        private String level;
        private String message;
        private String studentId;
        private String assignmentId;
        private String details;

        public LogEntry(String timestamp, String level, String message,
                       String studentId, String assignmentId, String details) {
            this.timestamp = timestamp;
            this.level = level;
            this.message = message;
            this.studentId = studentId != null ? studentId : "";
            this.assignmentId = assignmentId != null ? assignmentId : "";
            this.details = details != null ? details : "";
        }

        // Getters
        public String getTimestamp() { return timestamp; }
        public String getLevel() { return level; }
        public String getMessage() { return message; }
        public String getStudentId() { return studentId; }
        public String getAssignmentId() { return assignmentId; }
        public String getDetails() { return details; }

        public String toJson() {
            return String.format(
                "{\"timestamp\":\"%s\",\"level\":\"%s\",\"message\":\"%s\"," +
                "\"studentId\":\"%s\",\"assignmentId\":\"%s\",\"details\":\"%s\"}",
                timestamp, level, message, studentId, assignmentId, details
            );
        }
    }
}