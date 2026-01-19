package com.exit3.testing;

import java.util.ArrayList;
import java.util.List;

public class TestLogger {
    public static final List<String> logMessages = new ArrayList<>();
    public static void addLogMessage(String logMessage) {
        logMessages.add(logMessage);
    }
    public static String getLogMessagesAsJson() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        for (int i = 0; i < logMessages.size(); i++) {
            String step = "Step " + (i + 1);
            String logMessage = logMessages.get(i);

            jsonBuilder.append("\"").append(step).append("\": ");
            jsonBuilder.append("\"").append(logMessage).append("\"");

            if (i < logMessages.size() - 1) {
                jsonBuilder.append(", ");
            }
        }

        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }
}
