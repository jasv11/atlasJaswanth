package com.elearning.app;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Server-Sent Events (SSE) servlet for streaming evaluation progress
 */
public class EvaluationStreamServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String submissionId = request.getParameter("submissionId");

        if (submissionId == null || submissionId.trim().isEmpty()) {
            response.setStatus(400);
            response.getWriter().write("{\"error\": \"submissionId is required\"}");
            return;
        }

        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Access-Control-Allow-Origin", "*");

        PrintWriter writer = response.getWriter();

        EvaluationProgress progress = EvaluationProgress.get(submissionId);

        if (progress == null) {
            sendSSE(writer, "waiting", "{\"stage\":\"waiting\",\"message\":\"Waiting for evaluation to start...\",\"progress\":0}");
            writer.flush();

            int waitCount = 0;
            while (progress == null && waitCount < 60) {
                try {
                    Thread.sleep(500);
                    progress = EvaluationProgress.get(submissionId);
                    waitCount++;
                } catch (InterruptedException e) {
                    break;
                }
            }

            if (progress == null) {
                sendSSE(writer, "error", "{\"error\":\"Evaluation not found or timed out\"}");
                writer.flush();
                return;
            }
        }

        final EvaluationProgress finalProgress = progress;
        final boolean[] done = {false};

        EvaluationProgress.ProgressListener listener = new EvaluationProgress.ProgressListener() {
            @Override
            public void onProgress(EvaluationProgress.ProgressUpdate update) {
                try {
                    sendSSE(writer, "progress", update.toJson());
                    writer.flush();

                    // Check if client disconnected after sending
                    if (writer.checkError()) {
                        System.err.println("Client disconnected during progress update");
                    }
                } catch (Exception e) {
                    System.err.println("Error sending progress update: " + e.getMessage());
                }
            }

            @Override
            public void onComplete(EvaluationResult result) {
                try {
                    String resultJson = String.format(
                        "{\"stage\":\"complete\",\"message\":\"Evaluation completed\",\"progress\":100," +
                        "\"score\":%d,\"feedback\":\"%s\"%s%s}",
                        result.getScore(),
                        escapeJson(result.getFeedback()),
                        result.getStrengths() != null ? ",\"strengths\":\"" + escapeJson(result.getStrengths()) + "\"" : "",
                        result.getImprovements() != null ? ",\"improvements\":\"" + escapeJson(result.getImprovements()) + "\"" : ""
                    );
                    sendSSE(writer, "complete", resultJson);
                    writer.flush();
                    done[0] = true;
                } catch (Exception e) {
                    System.err.println("Error sending completion: " + e.getMessage());
                    done[0] = true;
                }
            }

            @Override
            public void onError(String error) {
                try {
                    String errorJson = String.format("{\"error\":\"%s\"}", escapeJson(error));
                    sendSSE(writer, "error", errorJson);
                    writer.flush();
                    done[0] = true;
                } catch (Exception e) {
                    System.err.println("Error sending error: " + e.getMessage());
                    done[0] = true;
                }
            }
        };

        finalProgress.addListener(listener);

        if (finalProgress.isCompleted() && finalProgress.getResult() != null) {
            listener.onComplete(finalProgress.getResult());
        }

        int keepAliveCount = 0;
        while (!done[0] && keepAliveCount < 600) {
            try {
                Thread.sleep(500);
                keepAliveCount++;

                // Send keep-alive every 5 seconds (10 iterations * 500ms)
                if (keepAliveCount % 10 == 0) {
                    writer.write(": keep-alive\n\n");
                    writer.flush();

                    // Check if client disconnected
                    if (writer.checkError()) {
                        System.out.println("Client disconnected, stopping SSE stream");
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("SSE keep-alive error: " + e.getMessage());
                break;
            }
        }

        finalProgress.removeListener(listener);

        if (finalProgress.isCompleted()) {
            EvaluationProgress.remove(submissionId);
        }
    }

    private void sendSSE(PrintWriter writer, String event, String data) {
        writer.write("event: " + event + "\n");
        writer.write("data: " + data + "\n\n");
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(200);
    }
}
