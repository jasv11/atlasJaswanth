package com.elearning.app;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ScoreServlet extends HttpServlet {

    private ScoreManager scoreManager;

    public ScoreServlet() {
        this.scoreManager = ScoreManager.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        setCORSHeaders(response);

        try {
            String assignmentId = request.getParameter("assignmentId");
            String studentId = request.getParameter("studentId");

            if (assignmentId != null && !assignmentId.trim().isEmpty()) {
                StudentScore score = scoreManager.getScore(assignmentId);
                if (score != null) {
                    response.setStatus(200);
                    response.getWriter().write(score.toJson());
                } else {
                    response.setStatus(404);
                    response.getWriter().write("{\"error\": \"Score not found for assignment ID: " + assignmentId + "\"}");
                }
            } else if (studentId != null && !studentId.trim().isEmpty()) {
                String studentScores = scoreManager.getScoresByStudentId(studentId);
                response.setStatus(200);
                response.getWriter().write(studentScores);
            } else {
                String allScores = scoreManager.getAllScoresAsJson();
                response.setStatus(200);
                response.getWriter().write(allScores);
            }

        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Internal server error: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        setCORSHeaders(response);

        String action = request.getParameter("action");

        try {
            if ("clearCache".equals(action)) {
                scoreManager.clearCache();
                response.setStatus(200);
                response.getWriter().write("{\"success\": true, \"message\": \"Score cache cleared\"}");

            } else if ("stats".equals(action)) {
                String stats = scoreManager.getCacheStats();
                response.setStatus(200);
                response.getWriter().write(stats);

            } else {
                response.setStatus(400);
                response.getWriter().write("{\"error\": \"Invalid action. Use 'clearCache' or 'stats'\"}");
            }

        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Internal server error: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORSHeaders(response);
        response.setStatus(200);
    }

    private void setCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}
