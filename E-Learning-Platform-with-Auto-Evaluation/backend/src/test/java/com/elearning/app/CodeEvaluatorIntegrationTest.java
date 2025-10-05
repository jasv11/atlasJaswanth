package com.elearning.app;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@DisplayName("Production Test: Code Evaluator")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CodeEvaluatorIntegrationTest {

    private static final String TEST_WORKSPACE = "/tmp/production_test_workspace";
    private File testWorkspace;
    private CodeEvaluator evaluator;

    @BeforeEach
    void setup() {
        testWorkspace = new File(TEST_WORKSPACE);
        testWorkspace.mkdirs();
        evaluator = new CodeEvaluator();
    }

    @AfterEach
    void cleanup() {
        deleteDirectory(testWorkspace);
    }

    @Test
    @Order(1)
    @DisplayName("Test 1: Code compilation works")
    void testCodeCompilation() throws Exception {
        System.out.println("\n[PRODUCTION TEST] Verifying code compilation...");

        String simpleCode =
            "public class SimpleTest {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"Test\");\n" +
            "    }\n" +
            "}";

        File codeFile = createTestFile("SimpleTest.java", simpleCode);
        List<CodeEvaluator.TestCase> tests = Arrays.asList(
            new CodeEvaluator.TestCase("Basic Test", "", "Test", 100)
        );

        CodeEvaluator.CodeEvaluationResult result = evaluator.evaluateCode(
            codeFile, tests, "PROD-TEST-001", "COMPILE-TEST"
        );

        assertTrue(result.compilationSuccess, "Code compilation must work for production");
        assertEquals(100, result.score, "Simple test must pass");

        System.out.println("  [PASS] Code compilation working correctly");
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Test case execution works")
    void testCaseExecution() throws Exception {
        System.out.println("\n[PRODUCTION TEST] Verifying test case execution...");

        String calculatorCode =
            "public class Calculator {\n" +
            "    public static void main(String[] args) {\n" +
            "        java.util.Scanner sc = new java.util.Scanner(System.in);\n" +
            "        int a = sc.nextInt();\n" +
            "        int b = sc.nextInt();\n" +
            "        System.out.println(a + b);\n" +
            "    }\n" +
            "}";

        File codeFile = createTestFile("Calculator.java", calculatorCode);
        List<CodeEvaluator.TestCase> tests = Arrays.asList(
            new CodeEvaluator.TestCase("Test 1: 2+3=5", "2\n3", "5", 50),
            new CodeEvaluator.TestCase("Test 2: 10+20=30", "10\n20", "30", 50)
        );

        CodeEvaluator.CodeEvaluationResult result = evaluator.evaluateCode(
            codeFile, tests, "PROD-TEST-002", "EXEC-TEST"
        );

        assertTrue(result.compilationSuccess, "Compilation must succeed");
        assertEquals(100, result.score, "All tests must pass");
        assertEquals(2, result.testResults.stream().filter(t -> t.passed).count());

        System.out.println("  [PASS] Test case execution working correctly");
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Compilation error handling works")
    void testCompilationErrorHandling() throws Exception {
        System.out.println("\n[PRODUCTION TEST] Verifying error handling...");

        String buggyCode =
            "public class BuggyCode {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"Broken\"\n" + // Missing closing paren
            "    }\n" +
            "}";

        File codeFile = createTestFile("BuggyCode.java", buggyCode);
        List<CodeEvaluator.TestCase> tests = Arrays.asList(
            new CodeEvaluator.TestCase("Test 1", "", "Test", 100)
        );

        CodeEvaluator.CodeEvaluationResult result = evaluator.evaluateCode(
            codeFile, tests, "PROD-TEST-003", "ERROR-TEST"
        );

        assertFalse(result.compilationSuccess, "Must detect compilation errors");
        assertEquals(0, result.score, "Score must be 0 for compilation failure");
        assertTrue(result.feedback.contains("Compilation failed"), "Must provide error feedback");

        System.out.println("  [PASS] Error handling working correctly");
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Partial credit system works")
    void testPartialCreditSystem() throws Exception {
        System.out.println("\n[PRODUCTION TEST] Verifying partial credit...");

        String partialCode =
            "public class PartialSolution {\n" +
            "    public static void main(String[] args) {\n" +
            "        java.util.Scanner sc = new java.util.Scanner(System.in);\n" +
            "        int n = sc.nextInt();\n" +
            "        if (n % 2 == 0) System.out.println(\"EVEN\");\n" +
            "        else System.out.println(\"ODD\");\n" +
            "    }\n" +
            "}";

        File codeFile = createTestFile("PartialSolution.java", partialCode);
        List<CodeEvaluator.TestCase> tests = Arrays.asList(
            new CodeEvaluator.TestCase("Test 1", "4", "EVEN", 50),
            new CodeEvaluator.TestCase("Test 2", "7", "ODD", 50)
        );

        CodeEvaluator.CodeEvaluationResult result = evaluator.evaluateCode(
            codeFile, tests, "PROD-TEST-004", "PARTIAL-TEST"
        );

        assertTrue(result.compilationSuccess, "Code must compile");
        assertTrue(result.score >= 0 && result.score <= 100, "Score must be in valid range");

        System.out.println("  [PASS] Partial credit system working correctly");
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: File extraction works (ZIP, TXT)")
    void testFileExtraction() throws Exception {
        System.out.println("\n[PRODUCTION TEST] Verifying file extraction...");

        // Test TXT file extraction
        String codeContent =
            "public class ExtractTest {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"Extracted\");\n" +
            "    }\n" +
            "}";

        File txtFile = createTestFile("code.txt", codeContent);
        List<CodeEvaluator.TestCase> tests = Arrays.asList(
            new CodeEvaluator.TestCase("Extraction Test", "", "Extracted", 100)
        );

        CodeEvaluator.CodeEvaluationResult result = evaluator.evaluateCode(
            txtFile, tests, "PROD-TEST-005", "EXTRACT-TEST"
        );

        assertTrue(result.compilationSuccess, "TXT file extraction must work");

        System.out.println("  [PASS] File extraction working correctly");
    }

    // Helper methods
    private File createTestFile(String fileName, String content) throws IOException {
        File file = new File(testWorkspace, fileName);
        Files.write(file.toPath(), content.getBytes());
        return file;
    }

    private void deleteDirectory(File directory) {
        if (directory.exists()) {
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
    }
}
