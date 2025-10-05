package com.elearning.app;

public class Question {
    private String questionId;
    private String questionText;
    private String correctAnswer;
    private int points;
    private String[] keywords; // Optional keywords for partial matching

    public Question(String questionId, String questionText, String correctAnswer, int points, String[] keywords) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.points = points;
        this.keywords = keywords;
    }

    // Getters
    public String getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public int getPoints() {
        return points;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"questionId\": \"").append(questionId).append("\",");
        json.append("\"questionText\": \"").append(escapeJson(questionText)).append("\",");
        json.append("\"correctAnswer\": \"").append(escapeJson(correctAnswer)).append("\",");
        json.append("\"points\": ").append(points).append(",");
        json.append("\"keywords\": [");
        if (keywords != null) {
            for (int i = 0; i < keywords.length; i++) {
                if (i > 0) json.append(",");
                json.append("\"").append(escapeJson(keywords[i])).append("\"");
            }
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
