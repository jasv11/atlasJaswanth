package com.elearning.app;

public class EvaluationResult {
    private int score;
    private String feedback;
    private String strengths;
    private String improvements;

    public EvaluationResult(int score, String feedback) {
        this.score = score;
        this.feedback = feedback;
        this.strengths = null;
        this.improvements = null;
    }

    public EvaluationResult(int score, String feedback, String strengths, String improvements) {
        this.score = score;
        this.feedback = feedback;
        this.strengths = strengths;
        this.improvements = improvements;
    }

    public int getScore() { return score; }
    public String getFeedback() { return feedback; }
    public String getStrengths() { return strengths; }
    public String getImprovements() { return improvements; }

    public void setScore(int score) { this.score = score; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public void setStrengths(String strengths) { this.strengths = strengths; }
    public void setImprovements(String improvements) { this.improvements = improvements; }
}