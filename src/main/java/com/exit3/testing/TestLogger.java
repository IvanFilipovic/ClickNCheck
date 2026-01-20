package com.exit3.testing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Thread-safe test logger for tracking test execution steps.
 * Each test thread maintains its own log messages.
 */
public class TestLogger {
    private static final ThreadLocal<List<LogEntry>> logMessages = ThreadLocal.withInitial(ArrayList::new);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Adds a log message with timestamp for the current thread.
     *
     * @param logMessage the message to log
     */
    public static void addLogMessage(String logMessage) {
        logMessages.get().add(new LogEntry(Instant.now(), logMessage));
    }

    /**
     * Returns all log messages for the current thread as JSON.
     *
     * @return JSON string containing all log messages with timestamps
     */
    public static String getLogMessagesAsJson() {
        List<LogEntry> entries = logMessages.get();
        if (entries.isEmpty()) {
            return "{}";
        }
        return gson.toJson(entries);
    }

    /**
     * Returns all log messages for the current thread as a list.
     *
     * @return list of log entries
     */
    public static List<LogEntry> getLogMessages() {
        return new ArrayList<>(logMessages.get());
    }

    /**
     * Clears all log messages for the current thread.
     * Call this at the start of each test to ensure clean state.
     */
    public static void clearLogs() {
        logMessages.get().clear();
    }

    /**
     * Removes the log messages for the current thread.
     * Call this after test completion to prevent memory leaks.
     */
    public static void cleanup() {
        logMessages.remove();
    }

    /**
     * Represents a single log entry with timestamp and message.
     */
    public static class LogEntry {
        private final Instant timestamp;
        private final String message;
        private final long stepNumber;
        private static final ThreadLocal<Long> stepCounter = ThreadLocal.withInitial(() -> 0L);

        public LogEntry(Instant timestamp, String message) {
            this.timestamp = timestamp;
            this.message = message;
            this.stepNumber = stepCounter.get() + 1;
            stepCounter.set(this.stepNumber);
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public String getMessage() {
            return message;
        }

        public long getStepNumber() {
            return stepNumber;
        }

        @Override
        public String toString() {
            return String.format("[Step %d @ %s] %s", stepNumber, timestamp, message);
        }

        /**
         * Resets the step counter for a new test.
         */
        public static void resetStepCounter() {
            stepCounter.set(0L);
        }

        /**
         * Cleans up step counter for current thread.
         */
        public static void cleanupStepCounter() {
            stepCounter.remove();
        }
    }
}
