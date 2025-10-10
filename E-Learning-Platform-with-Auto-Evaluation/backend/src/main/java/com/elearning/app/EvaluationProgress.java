package com.elearning.app;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.Map;

/**
 * Tracks evaluation progress and broadcasts updates to listeners
 */
public class EvaluationProgress {

    private static final Map<String, EvaluationProgress> activeEvaluations = new ConcurrentHashMap<>();

    private final String submissionId;
    private final List<ProgressListener> listeners = new CopyOnWriteArrayList<>();
    private volatile boolean completed = false;
    private volatile EvaluationResult result;

    public interface ProgressListener {
        void onProgress(ProgressUpdate update);
        void onComplete(EvaluationResult result);
        void onError(String error);
    }

    public static class ProgressUpdate {
        public final String stage;
        public final String message;
        public final int progress; // 0-100
        public final long timestamp;

        public ProgressUpdate(String stage, String message, int progress) {
            this.stage = stage;
            this.message = message;
            this.progress = progress;
            this.timestamp = System.currentTimeMillis();
        }

        public String toJson() {
            return String.format("{\"stage\":\"%s\",\"message\":\"%s\",\"progress\":%d,\"timestamp\":%d}",
                escapeJson(stage), escapeJson(message), progress, timestamp);
        }

        private String escapeJson(String str) {
            if (str == null) return "";
            return str.replace("\\", "\\\\")
                     .replace("\"", "\\\"")
                     .replace("\n", "\\n")
                     .replace("\r", "\\r");
        }
    }

    private EvaluationProgress(String submissionId) {
        this.submissionId = submissionId;
    }

    public static EvaluationProgress create(String submissionId) {
        EvaluationProgress progress = new EvaluationProgress(submissionId);
        activeEvaluations.put(submissionId, progress);
        return progress;
    }

    public static EvaluationProgress get(String submissionId) {
        return activeEvaluations.get(submissionId);
    }

    public static void remove(String submissionId) {
        activeEvaluations.remove(submissionId);
    }

    public void addListener(ProgressListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ProgressListener listener) {
        listeners.remove(listener);
    }

    public void updateProgress(String stage, String message, int progress) {
        ProgressUpdate update = new ProgressUpdate(stage, message, progress);
        for (ProgressListener listener : listeners) {
            try {
                listener.onProgress(update);
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }

    public void complete(EvaluationResult result) {
        this.result = result;
        this.completed = true;
        for (ProgressListener listener : listeners) {
            try {
                listener.onComplete(result);
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }

    public void error(String errorMessage) {
        this.completed = true;
        for (ProgressListener listener : listeners) {
            try {
                listener.onError(errorMessage);
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }

    public boolean isCompleted() {
        return completed;
    }

    public EvaluationResult getResult() {
        return result;
    }
}
