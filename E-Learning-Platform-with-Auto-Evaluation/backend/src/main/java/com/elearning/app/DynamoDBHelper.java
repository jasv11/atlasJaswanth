package com.elearning.app;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import io.github.cdimascio.dotenv.Dotenv;

public class DynamoDBHelper {

    private AmazonDynamoDB client;
    private DynamoDB dynamoDB;
    private Table scoresTable;
    private Table logsTable;

    public DynamoDBHelper() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            String accessKey = dotenv.get("AWS_ACCESS_KEY_ID");
            String secretKey = dotenv.get("AWS_SECRET_ACCESS_KEY");
            String region = dotenv.get("AWS_REGION", "us-east-1");
            String scoresTableName = dotenv.get("DYNAMODB_SCORES_TABLE", "StudentScores");
            String logsTableName = dotenv.get("DYNAMODB_LOGS_TABLE", "SystemLogs");

            BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

            this.client = AmazonDynamoDBClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withRegion(Regions.fromName(region))
                    .build();

            this.dynamoDB = new DynamoDB(client);
            this.scoresTable = dynamoDB.getTable(scoresTableName);
            this.logsTable = dynamoDB.getTable(logsTableName);

            System.out.println(" DynamoDB connection initialized successfully");
            System.out.println("   Region: " + region);
            System.out.println("   Tables: " + scoresTableName + ", " + logsTableName);

        } catch (Exception e) {
            System.out.println(" Error initializing DynamoDB: " + e.getMessage());
            e.printStackTrace();
            this.client = null;
            this.dynamoDB = null;
        }
    }

    public boolean isConnected() {
        return client != null && dynamoDB != null;
    }

    public Table getTable(String tableName) {
        if (!isConnected()) {
            return null;
        }
        return dynamoDB.getTable(tableName);
    }

    public void saveScore(StudentScore score) {
        if (!isConnected()) {
            System.out.println("DynamoDB not connected - skipping score save");
            return;
        }

        try {
            Item item = new Item()
                    .withPrimaryKey("assignmentId", score.getAssignmentId())
                    .withString("studentId", score.getStudentId())
                    .withString("studentName", score.getStudentName())
                    .withNumber("score", score.getScore())
                    .withString("feedback", score.getFeedback())
                    .withString("fileName", score.getFileName())
                    .withString("submissionTime", score.getSubmissionTime())
                    .withString("s3Url", score.getS3Url() != null ? score.getS3Url() : "");

            scoresTable.putItem(item);
            System.out.println("Score saved to DynamoDB: " + score.getAssignmentId());

        } catch (Exception e) {
            System.out.println("Error saving score to DynamoDB: " + e.getMessage());
        }
    }

    public StudentScore getScore(String assignmentId) {
        if (!isConnected()) {
            System.out.println("DynamoDB not connected - cannot retrieve score");
            return null;
        }

        try {
            Item item = scoresTable.getItem("assignmentId", assignmentId);
            if (item != null) {
                String s3Url = item.isPresent("s3Url") ? item.getString("s3Url") : null;
                return new StudentScore(
                        item.getString("assignmentId"),
                        item.getString("studentId"),
                        item.getString("studentName"),
                        item.getInt("score"),
                        item.getString("feedback"),
                        item.getString("fileName"),
                        s3Url
                );
            }
        } catch (Exception e) {
            System.out.println("Error retrieving score from DynamoDB: " + e.getMessage());
        }
        return null;
    }

    public String getAllScores() {
        if (!isConnected()) {
            return "{\"error\": \"DynamoDB not connected\"}";
        }

        try {
            StringBuilder jsonResult = new StringBuilder();
            jsonResult.append("{\"scores\": [");

            ScanSpec scanSpec = new ScanSpec();
            ItemCollection<ScanOutcome> items = scoresTable.scan(scanSpec);

            boolean first = true;
            for (Item item : items) {
                if (!first) {
                    jsonResult.append(",");
                }
                first = false;

                String s3Url = item.isPresent("s3Url") ? item.getString("s3Url") : "";
                jsonResult.append("{")
                        .append("\"assignmentId\":\"").append(item.getString("assignmentId")).append("\",")
                        .append("\"studentId\":\"").append(item.getString("studentId")).append("\",")
                        .append("\"studentName\":\"").append(item.getString("studentName")).append("\",")
                        .append("\"score\":").append(item.getInt("score")).append(",")
                        .append("\"feedback\":\"").append(item.getString("feedback")).append("\",")
                        .append("\"fileName\":\"").append(item.getString("fileName")).append("\",")
                        .append("\"submissionTime\":\"").append(item.getString("submissionTime")).append("\",")
                        .append("\"s3Url\":\"").append(s3Url).append("\"")
                        .append("}");
            }

            jsonResult.append("]}");
            return jsonResult.toString();

        } catch (Exception e) {
            System.out.println("Error retrieving all scores: " + e.getMessage());
            return "{\"error\": \"Failed to retrieve scores\"}";
        }
    }

    public String getScoresByStudentId(String studentId) {
        if (!isConnected()) {
            return "{\"success\": true, \"scores\": [], \"count\": 0}";
        }

        try {
            StringBuilder jsonResult = new StringBuilder();
            jsonResult.append("{\"success\": true, \"scores\": [");

            ScanSpec scanSpec = new ScanSpec();
            ItemCollection<ScanOutcome> items = scoresTable.scan(scanSpec);

            boolean first = true;
            int count = 0;
            for (Item item : items) {
                String itemStudentId = item.getString("studentId");
                if (studentId.equals(itemStudentId)) {
                    if (!first) {
                        jsonResult.append(",");
                    }
                    first = false;
                    count++;

                    String s3Url = item.isPresent("s3Url") ? item.getString("s3Url") : "";
                    jsonResult.append("{")
                            .append("\"assignmentId\":\"").append(escapeJson(item.getString("assignmentId"))).append("\",")
                            .append("\"studentId\":\"").append(escapeJson(itemStudentId)).append("\",")
                            .append("\"studentName\":\"").append(escapeJson(item.getString("studentName"))).append("\",")
                            .append("\"score\":").append(item.getInt("score")).append(",")
                            .append("\"feedback\":\"").append(escapeJson(item.getString("feedback"))).append("\",")
                            .append("\"fileName\":\"").append(escapeJson(item.getString("fileName"))).append("\",")
                            .append("\"submissionTime\":\"").append(escapeJson(item.getString("submissionTime"))).append("\",")
                            .append("\"s3Url\":\"").append(escapeJson(s3Url)).append("\"")
                            .append("}");
                }
            }

            jsonResult.append("], \"count\": ").append(count).append("}");
            return jsonResult.toString();

        } catch (Exception e) {
            System.out.println("Error retrieving scores for student " + studentId + ": " + e.getMessage());
            return "{\"success\": false, \"scores\": [], \"count\": 0, \"error\": \"" + e.getMessage() + "\"}";
        }
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    public void saveLog(String level, String message, String studentId, String assignmentId, String details) {
        if (!isConnected()) {
            System.out.println("DynamoDB not connected - skipping log save");
            return;
        }

        try {
            String logId = System.currentTimeMillis() + "_" + Math.random();
            String timestamp = java.time.LocalDateTime.now().toString();

            Item item = new Item()
                    .withPrimaryKey("logId", logId)
                    .withString("timestamp", timestamp)
                    .withString("level", level)
                    .withString("message", message)
                    .withString("studentId", studentId != null ? studentId : "")
                    .withString("assignmentId", assignmentId != null ? assignmentId : "")
                    .withString("details", details != null ? details : "");

            logsTable.putItem(item);
            System.out.println("Log saved to DynamoDB: " + level + " - " + message);

        } catch (Exception e) {
            System.out.println("Error saving log to DynamoDB: " + e.getMessage());
        }
    }

    public String getAllLogs() {
        if (!isConnected()) {
            return "{\"error\": \"DynamoDB not connected\"}";
        }

        try {
            StringBuilder jsonResult = new StringBuilder();
            jsonResult.append("{\"logs\": [");

            ScanSpec scanSpec = new ScanSpec();
            ItemCollection<ScanOutcome> items = logsTable.scan(scanSpec);

            boolean first = true;
            for (Item item : items) {
                if (!first) {
                    jsonResult.append(",");
                }
                first = false;

                jsonResult.append("{")
                        .append("\"logId\":\"").append(item.getString("logId")).append("\",")
                        .append("\"timestamp\":\"").append(item.getString("timestamp")).append("\",")
                        .append("\"level\":\"").append(item.getString("level")).append("\",")
                        .append("\"message\":\"").append(item.getString("message")).append("\",")
                        .append("\"studentId\":\"").append(item.getString("studentId")).append("\",")
                        .append("\"assignmentId\":\"").append(item.getString("assignmentId")).append("\",")
                        .append("\"details\":\"").append(item.getString("details")).append("\"")
                        .append("}");
            }

            jsonResult.append("]}");
            return jsonResult.toString();

        } catch (Exception e) {
            System.out.println("Error retrieving logs: " + e.getMessage());
            return "{\"error\": \"Failed to retrieve logs\"}";
        }
    }

    public void shutdown() {
        if (client != null) {
            client.shutdown();
        }
    }
}