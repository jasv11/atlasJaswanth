package com.elearning.app;

import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;

public class ScoreManager {

    private static ScoreManager instance;
    private ConcurrentHashMap<String, StudentScore> scoreCache;
    private DynamoDBHelper dynamoDBHelper;

    private ScoreManager() {
        this.scoreCache = new ConcurrentHashMap<>();
        this.dynamoDBHelper = new DynamoDBHelper();
        System.out.println("ScoreManager initialized with in-memory cache");
    }

    public static synchronized ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    public void saveScore(StudentScore score) {
        try {
            scoreCache.put(score.getAssignmentId(), score);
            System.out.println("Score cached in memory: " + score.getAssignmentId());

            dynamoDBHelper.saveScore(score);

        } catch (Exception e) {
            System.out.println("Error saving score: " + e.getMessage());
        }
    }

    public StudentScore getScore(String assignmentId) {
        try {
            StudentScore cachedScore = scoreCache.get(assignmentId);
            if (cachedScore != null) {
                System.out.println("Score retrieved from cache: " + assignmentId);
                return cachedScore;
            }

            StudentScore dbScore = dynamoDBHelper.getScore(assignmentId);
            if (dbScore != null) {
                scoreCache.put(assignmentId, dbScore);
                System.out.println("Score retrieved from DynamoDB and cached: " + assignmentId);
                return dbScore;
            }

        } catch (Exception e) {
            System.out.println("Error retrieving score: " + e.getMessage());
        }

        return null;
    }

    public List<StudentScore> getAllScoresFromCache() {
        return new ArrayList<>(scoreCache.values());
    }

    public String getAllScoresAsJson() {
        try {
            List<StudentScore> scores = getAllScoresFromCache();

            if (!scores.isEmpty()) {
                StringBuilder jsonResult = new StringBuilder();
                jsonResult.append("{\"scores\": [");

                for (int i = 0; i < scores.size(); i++) {
                    if (i > 0) {
                        jsonResult.append(",");
                    }
                    jsonResult.append(scores.get(i).toJson());
                }

                jsonResult.append("], \"count\": ").append(scores.size()).append("}");
                return jsonResult.toString();
            }

            try {
                return dynamoDBHelper.getAllScores();
            } catch (Exception dbException) {
                System.out.println("Error retrieving all scores: " + dbException.getMessage());
                return "{\"scores\": [], \"count\": 0, \"message\": \"No scores available\"}";
            }

        } catch (Exception e) {
            System.out.println("Error getting all scores: " + e.getMessage());
            return "{\"scores\": [], \"count\": 0, \"error\": \"" + e.getMessage() + "\"}";
        }
    }

    public String getScoresByStudentId(String studentId) {
        try {
            List<StudentScore> allScores = getAllScoresFromCache();
            List<StudentScore> studentScores = new ArrayList<>();

            for (StudentScore score : allScores) {
                if (score.getStudentId().equals(studentId)) {
                    studentScores.add(score);
                }
            }

            if (!studentScores.isEmpty()) {
                StringBuilder jsonResult = new StringBuilder();
                jsonResult.append("{\"success\": true, \"scores\": [");

                for (int i = 0; i < studentScores.size(); i++) {
                    if (i > 0) {
                        jsonResult.append(",");
                    }
                    jsonResult.append(studentScores.get(i).toJson());
                }

                jsonResult.append("], \"count\": ").append(studentScores.size()).append("}");
                return jsonResult.toString();
            }

            if (dynamoDBHelper.isConnected()) {
                System.out.println("Cache empty or no scores found - querying DynamoDB for student: " + studentId);
                return dynamoDBHelper.getScoresByStudentId(studentId);
            }

            return "{\"success\": true, \"scores\": [], \"count\": 0}";

        } catch (Exception e) {
            System.out.println("Error getting scores for student " + studentId + ": " + e.getMessage());
            return "{\"success\": false, \"scores\": [], \"count\": 0, \"error\": \"" + e.getMessage() + "\"}";
        }
    }

    public void clearCache() {
        scoreCache.clear();
        System.out.println("Score cache cleared");
    }

    public int getCacheSize() {
        return scoreCache.size();
    }

    public String getCacheStats() {
        return String.format("{\"cacheSize\": %d, \"dynamoConnected\": %s}",
                getCacheSize(), dynamoDBHelper.isConnected());
    }
}