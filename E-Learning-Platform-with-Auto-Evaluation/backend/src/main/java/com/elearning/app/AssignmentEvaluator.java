package com.elearning.app;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class AssignmentEvaluator {

   
    public EvaluationResult evaluateWithAnswerKey(File file, String fileName, Assignment assignment) {
        try {
            String content = extractTextFromFile(file, fileName);
            if (content == null || content.trim().isEmpty()) {
                return new EvaluationResult(0, "Unable to extract content from file");
            }

            int totalScore = 0;
            int totalPossiblePoints = assignment.getTotalPoints();
            StringBuilder detailedFeedback = new StringBuilder();

            detailedFeedback.append("Question-by-Question Evaluation:\n\n");

            for (Question question : assignment.getQuestions()) {
                int questionScore = evaluateQuestion(content, question);
                totalScore += questionScore;

                detailedFeedback.append(String.format("Q%s (%d/%d points): ",
                    question.getQuestionId(), questionScore, question.getPoints()));

                if (questionScore == question.getPoints()) {
                    detailedFeedback.append("Excellent! ");
                } else if (questionScore >= question.getPoints() * 0.7) {
                    detailedFeedback.append("Good! ");
                } else if (questionScore >= question.getPoints() * 0.4) {
                    detailedFeedback.append("Partial credit. ");
                } else {
                    detailedFeedback.append("Needs improvement. ");
                }
                detailedFeedback.append("\n");
            }

            // Calculate percentage
            int percentageScore = (int) ((totalScore * 100.0) / totalPossiblePoints);

            detailedFeedback.append(String.format("\nTotal Score: %d/%d (%d%%)",
                totalScore, totalPossiblePoints, percentageScore));

            return new EvaluationResult(percentageScore, detailedFeedback.toString());

        } catch (Exception e) {
            return new EvaluationResult(0, "Error processing file: " + e.getMessage());
        }
    }

    /**
     * Evaluate a single question by comparing student answer with correct answer
     */
    private int evaluateQuestion(String studentContent, Question question) {
        String lowerContent = studentContent.toLowerCase();
        String lowerCorrectAnswer = question.getCorrectAnswer().toLowerCase();

        int earnedPoints = 0;
        int maxPoints = question.getPoints();

        if (containsSimilarText(lowerContent, lowerCorrectAnswer, 0.8)) {
            return maxPoints;
        }

        String[] keywords = question.getKeywords();
        if (keywords != null && keywords.length > 0) {
            int matchedKeywords = 0;
            for (String keyword : keywords) {
                if (lowerContent.contains(keyword.toLowerCase())) {
                    matchedKeywords++;
                }
            }

            double keywordRatio = (double) matchedKeywords / keywords.length;
            earnedPoints = (int) (maxPoints * keywordRatio);
        }

        if (earnedPoints == 0) {
            String[] correctWords = lowerCorrectAnswer.split("\\s+");
            int matchedWords = 0;

            for (String word : correctWords) {
                if (word.length() > 3 && lowerContent.contains(word)) {
                    matchedWords++;
                }
            }

            if (correctWords.length > 0) {
                double wordRatio = (double) matchedWords / correctWords.length;
                earnedPoints = (int) (maxPoints * wordRatio * 0.6);
            }
        }

        return Math.min(earnedPoints, maxPoints);
    }

    /**
     * Check if student content contains text similar to the expected answer
     */
    private boolean containsSimilarText(String content, String expected, double threshold) {
        String[] expectedWords = expected.split("\\s+");
        int matchCount = 0;

        for (String word : expectedWords) {
            if (word.length() > 3 && content.contains(word)) {
                matchCount++;
            }
        }

        return expectedWords.length > 0 &&
               ((double) matchCount / expectedWords.length) >= threshold;
    }

    /**
     * Legacy evaluation without answer key (fallback)
     */
    public EvaluationResult evaluateAssignment(File file, String fileName) {
        try {
            String content = extractTextFromFile(file, fileName);
            if (content == null || content.trim().isEmpty()) {
                return new EvaluationResult(0, "Unable to extract content from file");
            }

            return new EvaluationResult(50, "Basic evaluation (no answer key provided). Please ensure assignment has questions with correct answers.");

        } catch (Exception e) {
            return new EvaluationResult(0, "Error processing file: " + e.getMessage());
        }
    }

    public String extractTextFromFile(File file, String fileName) throws IOException {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        switch (extension) {
            case "txt":
                return extractFromTextFile(file);
            case "pdf":
                return extractFromPDF(file);
            case "doc":
                return extractFromDocFile(file);
            case "docx":
                return extractFromDocxFile(file);
            case "zip":
                return "ZIP file detected - contains multiple files";
            default:
                return null;
        }
    }

    private String extractFromTextFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private String extractFromPDF(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractFromDocFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             HWPFDocument document = new HWPFDocument(fis);
             WordExtractor extractor = new WordExtractor(document)) {
            return extractor.getText();
        }
    }

    private String extractFromDocxFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        }
    }
}