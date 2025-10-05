package com.elearning.app;

public class EvaluationResult {
    private int score;
    private String feedback;

    public EvaluationResult(int score, String feedback) {
        this.score = score;
        this.feedback = feedback;
    }

    public int getScore() { return score; }
    public String getFeedback() { return feedback; }

    public void setScore(int score) { this.score = score; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}