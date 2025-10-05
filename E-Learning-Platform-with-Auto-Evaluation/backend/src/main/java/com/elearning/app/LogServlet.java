package com.elearning.app;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogServlet extends HttpServlet {

    private LogManager logManager;

    public LogServlet() {
        this.logManager = LogManager.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String allLogs = logManager.getAllLogsAsJson();
            response.setStatus(200);
            response.getWriter().write(allLogs);

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

        String action = request.getParameter("action");

        try {
            if ("clearCache".equals(action)) {
                logManager.clearCache();
                response.setStatus(200);
                response.getWriter().write("{\"success\": true, \"message\": \"Log cache cleared\"}");

            } else if ("stats".equals(action)) {
                String stats = logManager.getLogStats();
                response.setStatus(200);
                response.getWriter().write(stats);

            } else if ("testLog".equals(action)) {
                String level = request.getParameter("level");
                String message = request.getParameter("message");

                if (level == null) level = "INFO";
                if (message == null) message = "Test log entry";

                if ("ERROR".equals(level)) {
                    logManager.logError(message, "Test error from LogServlet");
                } else if ("WARNING".equals(level)) {
                    logManager.logWarning(message, "TEST_STUDENT", "TEST_ASSIGNMENT", "Test warning");
                } else {
                    logManager.logInfo(message, "TEST_STUDENT", "TEST_ASSIGNMENT", "Test info log");
                }

                response.setStatus(200);
                response.getWriter().write("{\"success\": true, \"message\": \"Test log added\"}");

            } else {
                response.setStatus(400);
                response.getWriter().write("{\"error\": \"Invalid action. Use 'clearCache', 'stats', or 'testLog'\"}");
            }

        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Internal server error: " + e.getMessage() + "\"}");
        }
    }

}