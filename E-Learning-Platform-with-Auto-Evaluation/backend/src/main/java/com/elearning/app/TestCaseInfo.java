package com.elearning.app;

/**
 * TestCaseInfo - Stores test case information for code assignments
 */
public class TestCaseInfo {
    private String testName;
    private String input;
    private String expectedOutput;
    private int points;

    public TestCaseInfo(String testName, String input, String expectedOutput, int points) {
        this.testName = testName;
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.points = points;
    }

    public String getTestName() {
        return testName;
    }

    public String getInput() {
        return input;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public int getPoints() {
        return points;
    }

    public String toJson() {
        return String.format(
            "{\"testName\":\"%s\",\"input\":\"%s\",\"expectedOutput\":\"%s\",\"points\":%d}",
            escapeJson(testName), escapeJson(input), escapeJson(expectedOutput), points
        );
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
