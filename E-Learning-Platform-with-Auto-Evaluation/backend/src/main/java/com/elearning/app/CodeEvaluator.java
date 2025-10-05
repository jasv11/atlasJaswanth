package com.elearning.app;

import javax.tools.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class CodeEvaluator {

    private static final String WORKSPACE_DIR = "/tmp/code_evaluation";
    private LogManager logManager;

    public CodeEvaluator() {
        this.logManager = LogManager.getInstance();
        try {
            Files.createDirectories(Paths.get(WORKSPACE_DIR));
        } catch (IOException e) {
            System.err.println("Failed to create workspace directory: " + e.getMessage());
        }
    }

    public CodeEvaluationResult evaluateCode(File studentCodeFile, List<TestCase> testCases,
                                             String studentId, String submissionId) {
        String workspaceId = UUID.randomUUID().toString();
        File workspace = new File(WORKSPACE_DIR, workspaceId);

        try {
            workspace.mkdirs();

            logManager.logInfo("Code evaluation started", studentId, submissionId,
                             "Workspace: " + workspace.getAbsolutePath());

            List<File> javaFiles = extractJavaFiles(studentCodeFile, workspace);
            if (javaFiles.isEmpty()) {
                return new CodeEvaluationResult(false, 0, 0,
                    "No valid Java files found in submission", new ArrayList<>());
            }

            CompilationResult compilationResult = compileJavaFiles(javaFiles, workspace);
            if (!compilationResult.success) {
                logManager.logWarning("Compilation failed", studentId, submissionId,
                                    compilationResult.errorMessage);
                return new CodeEvaluationResult(false, 0, testCases.size(),
                    "Compilation failed: " + compilationResult.errorMessage, new ArrayList<>());
            }

            logManager.logInfo("Compilation successful", studentId, submissionId,
                             javaFiles.size() + " files compiled");

            List<TestResult> testResults = new ArrayList<>();
            int passedTests = 0;

            for (TestCase testCase : testCases) {
                TestResult result = runTestCase(testCase, workspace, javaFiles);
                testResults.add(result);
                if (result.passed) {
                    passedTests++;
                }
            }

            int totalTests = testCases.size();
            int score = totalTests > 0 ? (passedTests * 100) / totalTests : 0;

            String feedback = generateFeedback(passedTests, totalTests, testResults);

            logManager.logInfo("Code evaluation completed", studentId, submissionId,
                             String.format("Score: %d (%d/%d tests passed)", score, passedTests, totalTests));

            return new CodeEvaluationResult(true, score, totalTests, feedback, testResults);

        } catch (Exception e) {
            logManager.logError("Code evaluation error", e.getMessage());
            return new CodeEvaluationResult(false, 0, testCases.size(),
                "Evaluation error: " + e.getMessage(), new ArrayList<>());
        } finally {
            cleanupWorkspace(workspace);
        }
    }

  
    private List<File> extractJavaFiles(File sourceFile, File workspace) throws IOException {
        List<File> javaFiles = new ArrayList<>();

        if (sourceFile.getName().endsWith(".java")) {
            String content = new String(Files.readAllBytes(sourceFile.toPath()));
            String className = extractPublicClassName(content);

            String targetFileName = className != null ? className + ".java" : sourceFile.getName();
            File destFile = new File(workspace, targetFileName);

            Files.write(destFile.toPath(), content.getBytes());
            javaFiles.add(destFile);

            System.out.println("✓ Extracted Java file: " + targetFileName + " (class: " + className + ")");
        } else if (sourceFile.getName().endsWith(".zip")) {
            javaFiles.addAll(extractZipFile(sourceFile, workspace));
        } else if (sourceFile.getName().endsWith(".txt")) {
            javaFiles.addAll(extractJavaFromText(sourceFile, workspace));
        }

        return javaFiles;
    }

    private List<File> extractZipFile(File zipFile, File workspace) throws IOException {
        List<File> javaFiles = new ArrayList<>();

        try {
            ProcessBuilder pb = new ProcessBuilder("unzip", "-q", zipFile.getAbsolutePath(), "-d", workspace.getAbsolutePath());
            Process process = pb.start();
            process.waitFor();

            Files.walk(workspace.toPath())
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> javaFiles.add(path.toFile()));

        } catch (Exception e) {
            System.err.println("Failed to extract ZIP: " + e.getMessage());
        }

        return javaFiles;
    }

    private List<File> extractJavaFromText(File textFile, File workspace) throws IOException {
        List<File> javaFiles = new ArrayList<>();
        String content = new String(Files.readAllBytes(textFile.toPath()));

        Pattern classPattern = Pattern.compile("(public\\s+)?class\\s+(\\w+)\\s*\\{", Pattern.MULTILINE);
        Matcher matcher = classPattern.matcher(content);

        if (matcher.find()) {
            String className = matcher.group(2);
            File javaFile = new File(workspace, className + ".java");
            Files.write(javaFile.toPath(), content.getBytes());
            javaFiles.add(javaFile);
        }

        return javaFiles;
    }

    private CompilationResult compileJavaFiles(List<File> javaFiles, File workspace) {
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                return new CompilationResult(false, "Java compiler not available");
            }

            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

            Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromFiles(javaFiles);

            List<String> optionList = new ArrayList<>();
            optionList.add("-d");
            optionList.add(workspace.getAbsolutePath());
            optionList.add("-classpath");
            optionList.add(workspace.getAbsolutePath());

            JavaCompiler.CompilationTask task = compiler.getTask(
                null, fileManager, diagnostics, optionList, null, compilationUnits);

            boolean success = task.call();
            fileManager.close();

            if (!success) {
                StringBuilder errors = new StringBuilder();
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    errors.append(String.format("Line %d: %s\n",
                        diagnostic.getLineNumber(), diagnostic.getMessage(null)));
                }
                return new CompilationResult(false, errors.toString());
            }

            return new CompilationResult(true, "Compilation successful");

        } catch (Exception e) {
            return new CompilationResult(false, "Compilation exception: " + e.getMessage());
        }
    }

    private TestResult runTestCase(TestCase testCase, File workspace, List<File> javaFiles) {
        try {
            String className = extractMainClassName(javaFiles);
            if (className == null) {
                return new TestResult(testCase.name, false, "No main class found", "");
            }

            ProcessBuilder pb = new ProcessBuilder("java", "-cp", workspace.getAbsolutePath(), className);
            pb.directory(workspace);

            Process process = pb.start();
            String processedInput = testCase.input != null ? testCase.input.replace("\\n", "\n") : "";

            System.out.println(String.format("  Running test '%s' with input: %s",
                testCase.name, processedInput.replace("\n", "\\n")));

            if (processedInput != null && !processedInput.isEmpty()) {
                try (OutputStream os = process.getOutputStream()) {
                    os.write(processedInput.getBytes());
                    os.flush();
                }
            }

            String output = readProcessOutput(process.getInputStream());
            String errorOutput = readProcessOutput(process.getErrorStream());

            boolean completed = process.waitFor(5, java.util.concurrent.TimeUnit.SECONDS);

            if (!completed) {
                process.destroyForcibly();
                return new TestResult(testCase.name, false, "Test timed out (>5s)", "");
            }

            int exitCode = process.exitValue();
            if (exitCode != 0) {
                return new TestResult(testCase.name, false,
                    "Runtime error (exit code: " + exitCode + ")", errorOutput);
            }

            boolean passed = checkOutput(output.trim(), testCase.expectedOutput.trim());
            String message = passed ? "Test passed" :
                String.format("Expected: '%s', Got: '%s'", testCase.expectedOutput.trim(), output.trim());

            System.out.println(String.format("  Test '%s': %s | Expected='%s' | Actual='%s'",
                testCase.name, passed ? "PASS" : "FAIL",
                testCase.expectedOutput.trim(), output.trim()));

            return new TestResult(testCase.name, passed, message, output.trim());

        } catch (Exception e) {
            return new TestResult(testCase.name, false, "Test execution error: " + e.getMessage(), "");
        }
    }

    private String extractPublicClassName(String content) {
        Pattern pattern = Pattern.compile("public\\s+class\\s+(\\w+)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }

        pattern = Pattern.compile("class\\s+(\\w+)", Pattern.MULTILINE);
        matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private String extractMainClassName(List<File> javaFiles) throws IOException {
        for (File file : javaFiles) {
            String content = new String(Files.readAllBytes(file.toPath()));
            if (content.contains("public static void main")) {
                return file.getName().replace(".java", "");
            }
        }
        return javaFiles.isEmpty() ? null : javaFiles.get(0).getName().replace(".java", "");
    }
    private String readProcessOutput(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }

    
    private boolean checkOutput(String actual, String expected) {
        String normalizedActual = actual.replaceAll("\\s+", " ").trim();
        String normalizedExpected = expected.replaceAll("\\s+", " ").trim();

        return normalizedActual.equals(normalizedExpected);
    }


    private String generateFeedback(int passed, int total, List<TestResult> results) {
        StringBuilder feedback = new StringBuilder();
        feedback.append(String.format("Tests Passed: %d/%d\n\n", passed, total));

        for (TestResult result : results) {
            feedback.append(String.format("%s %s: %s\n",
                result.passed ? "✓" : "✗", result.testName, result.message));

            if (!result.passed && result.output != null && !result.output.isEmpty()) {
                feedback.append("  Output: ").append(result.output.substring(0, Math.min(100, result.output.length()))).append("\n");
            }
        }

        return feedback.toString();
    }

    private void cleanupWorkspace(File workspace) {
        try {
            if (workspace.exists()) {
                deleteDirectory(workspace);
            }
        } catch (Exception e) {
            System.err.println("Failed to cleanup workspace: " + e.getMessage());
        }
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    public static class CodeEvaluationResult {
        public final boolean compilationSuccess;
        public final int score;
        public final int totalTests;
        public final String feedback;
        public final List<TestResult> testResults;

        public CodeEvaluationResult(boolean compilationSuccess, int score, int totalTests,
                                   String feedback, List<TestResult> testResults) {
            this.compilationSuccess = compilationSuccess;
            this.score = score;
            this.totalTests = totalTests;
            this.feedback = feedback;
            this.testResults = testResults;
        }
    }

    public static class CompilationResult {
        public final boolean success;
        public final String errorMessage;

        public CompilationResult(boolean success, String errorMessage) {
            this.success = success;
            this.errorMessage = errorMessage;
        }
    }

    public static class TestCase {
        public final String name;
        public final String input;
        public final String expectedOutput;
        public final int points;

        public TestCase(String name, String input, String expectedOutput, int points) {
            this.name = name;
            this.input = input;
            this.expectedOutput = expectedOutput;
            this.points = points;
        }
    }

    public static class TestResult {
        public final String testName;
        public final boolean passed;
        public final String message;
        public final String output;

        public TestResult(String testName, boolean passed, String message, String output) {
            this.testName = testName;
            this.passed = passed;
            this.message = message;
            this.output = output;
        }
    }
}
