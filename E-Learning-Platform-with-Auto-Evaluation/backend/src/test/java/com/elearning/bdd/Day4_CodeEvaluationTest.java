package com.elearning.bdd;

import com.elearning.app.CodeEvaluator;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.*;

@DisplayName("Day 4: Code Evaluation with Test Case Runner")
class Day4_CodeEvaluationTest extends BDDTestBase {

    @Test
    @DisplayName("Scenario 1: TwoSum Problem - Student submits correct solution")
    void twoSumProblem_CorrectSolution_AllTestsPass() throws Exception {
        System.out.println("\nTesting: TwoSum Problem - Correct Solution");

        String correctCode =
            "public class TwoSum {\n" +
            "    public static void main(String[] args) {\n" +
            "        java.util.Scanner sc = new java.util.Scanner(System.in);\n" +
            "        int n = sc.nextInt();\n" +
            "        int target = sc.nextInt();\n" +
            "        int[] arr = new int[n];\n" +
            "        for (int i = 0; i < n; i++) arr[i] = sc.nextInt();\n" +
            "        \n" +
            "        for (int i = 0; i < n; i++) {\n" +
            "            for (int j = i + 1; j < n; j++) {\n" +
            "                if (arr[i] + arr[j] == target) {\n" +
            "                    System.out.println(i + \" \" + j);\n" +
            "                    return;\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "        System.out.println(\"-1 -1\");\n" +
            "    }\n" +
            "}";

        File codeFile = createTestFile("TwoSum.java", correctCode);

        List<CodeEvaluator.TestCase> testCases = Arrays.asList(
            new CodeEvaluator.TestCase("Test 1: Basic case [2,7,11,15] target=9",
                "4\n9\n2 7 11 15", "0 1", 33),
            new CodeEvaluator.TestCase("Test 2: Middle elements [3,2,4,8] target=12",
                "4\n12\n3 2 4 8", "2 3", 33),
            new CodeEvaluator.TestCase("Test 3: No solution [1,2,3] target=10",
                "3\n10\n1 2 3", "-1 -1", 34)
        );

        System.out.println("  Compiling and running tests...");
        CodeEvaluator evaluator = new CodeEvaluator();
        CodeEvaluator.CodeEvaluationResult result = evaluator.evaluateCode(
            codeFile, testCases, "STU001", "TEST-TWOSUM-001"
        );

        System.out.println("  Results:");
        System.out.println("    - Compilation: " + (result.compilationSuccess ? "Success" : "Failed"));
        System.out.println("    - Score: " + result.score + "/100");
        System.out.println("    - Tests Passed: " + result.testResults.stream().filter(t -> t.passed).count() + "/" + result.totalTests);

        assertTrue(result.compilationSuccess, "Code should compile successfully");
        assertEquals(100, result.score, "All tests should pass (100% score)");
        assertEquals(3, result.totalTests, "Should have 3 test cases");

        System.out.println("  TwoSum test PASSED - All requirements met!\n");
    }

    @Test
    @DisplayName("Scenario 2: Factorial Problem - Student submits correct recursive solution")
    void factorialProblem_CorrectSolution_AllTestsPass() throws Exception {
        System.out.println("\nTesting: Factorial Problem - Recursive Solution");

        String correctCode =
            "public class Factorial {\n" +
            "    public static void main(String[] args) {\n" +
            "        java.util.Scanner sc = new java.util.Scanner(System.in);\n" +
            "        int n = sc.nextInt();\n" +
            "        System.out.println(factorial(n));\n" +
            "    }\n" +
            "    \n" +
            "    static long factorial(int n) {\n" +
            "        if (n <= 1) return 1;\n" +
            "        return n * factorial(n - 1);\n" +
            "    }\n" +
            "}";

        File codeFile = createTestFile("Factorial.java", correctCode);

        List<CodeEvaluator.TestCase> testCases = Arrays.asList(
            new CodeEvaluator.TestCase("Test 1: factorial(5) = 120", "5", "120", 25),
            new CodeEvaluator.TestCase("Test 2: factorial(0) = 1", "0", "1", 25),
            new CodeEvaluator.TestCase("Test 3: factorial(10) = 3628800", "10", "3628800", 25),
            new CodeEvaluator.TestCase("Test 4: factorial(1) = 1", "1", "1", 25)
        );

        System.out.println("  Compiling and running tests...");
        CodeEvaluator evaluator = new CodeEvaluator();
        CodeEvaluator.CodeEvaluationResult result = evaluator.evaluateCode(
            codeFile, testCases, "STU002", "TEST-FACTORIAL-001"
        );

        System.out.println("  Results:");
        System.out.println("    - Compilation: " + (result.compilationSuccess ? "Success" : "Failed"));
        System.out.println("    - Score: " + result.score + "/100");
        System.out.println("    - Tests Passed: " + result.testResults.stream().filter(t -> t.passed).count() + "/" + result.totalTests);

        assertTrue(result.compilationSuccess, "Code should compile");
        assertEquals(100, result.score, "All factorial tests should pass");

        System.out.println("  Factorial test PASSED!\n");
    }

    @Test
    @DisplayName("Scenario 3: Palindrome Check - Student submits correct solution")
    void palindromeProblem_CorrectSolution_AllTestsPass() throws Exception {
        System.out.println("\nTesting: Palindrome Checker - String Manipulation");

        String correctCode =
            "public class Palindrome {\n" +
            "    public static void main(String[] args) {\n" +
            "        java.util.Scanner sc = new java.util.Scanner(System.in);\n" +
            "        String str = sc.nextLine().toLowerCase().replaceAll(\"[^a-z0-9]\", \"\");\n" +
            "        String reversed = new StringBuilder(str).reverse().toString();\n" +
            "        System.out.println(str.equals(reversed) ? \"YES\" : \"NO\");\n" +
            "    }\n" +
            "}";

        File codeFile = createTestFile("Palindrome.java", correctCode);

        List<CodeEvaluator.TestCase> testCases = Arrays.asList(
            new CodeEvaluator.TestCase("Test 1: 'racecar' is palindrome", "racecar", "YES", 33),
            new CodeEvaluator.TestCase("Test 2: 'hello' is not palindrome", "hello", "NO", 33),
            new CodeEvaluator.TestCase("Test 3: 'A man a plan a canal Panama' is palindrome",
                "A man a plan a canal Panama", "YES", 34)
        );

        System.out.println("  Compiling and running tests...");
        CodeEvaluator evaluator = new CodeEvaluator();
        CodeEvaluator.CodeEvaluationResult result = evaluator.evaluateCode(
            codeFile, testCases, "STU003", "TEST-PALINDROME-001"
        );

        System.out.println("  Results:");
        System.out.println("    - Compilation: " + (result.compilationSuccess ? "Success" : "Failed"));
        System.out.println("    - Score: " + result.score + "/100");

        assertTrue(result.compilationSuccess, "Code should compile");
        assertEquals(100, result.score, "All palindrome tests should pass");

        System.out.println("  Palindrome test PASSED!\n");
    }

    @Test
    @DisplayName("Scenario 4: Compilation Error - Student submits buggy code")
    void studentSubmitsBuggyCode_CompilationFails() throws Exception {
        System.out.println("\nTesting: Buggy Code - Compilation Should Fail");

        String buggyCode =
            "public class BuggyCode {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"Missing semicolon\")\n" + 
            "    }\n" +
            "}";

        File codeFile = createTestFile("BuggyCode.java", buggyCode);
        List<CodeEvaluator.TestCase> testCases = Arrays.asList(
            new CodeEvaluator.TestCase("Test 1", "", "Hello", 100)
        );

        System.out.println("  Attempting to compile buggy code...");
        CodeEvaluator evaluator = new CodeEvaluator();
        CodeEvaluator.CodeEvaluationResult result = evaluator.evaluateCode(
            codeFile, testCases, "STU004", "TEST-BUGGY-001"
        );

        System.out.println("  Results:");
        System.out.println("    - Compilation: " + (result.compilationSuccess ? "Success" : "Failed (Expected)"));
        System.out.println("    - Score: " + result.score + "/100");

        assertFalse(result.compilationSuccess, "Buggy code should NOT compile");
        assertEquals(0, result.score, "Score should be 0 for compilation failure");
        assertTrue(result.feedback.contains("Compilation failed"), "Should mention compilation failure");

        System.out.println("  Compilation error handled correctly!\n");
    }

    @Test
    @DisplayName("Scenario 5: Logic Error - Some tests pass, some fail")
    void logicError_SomeTestsPass() throws Exception {
        System.out.println("\nTesting: Logic Error - Partial Credit");

        String partialCode =
            "public class TwoSumPartial {\n" +
            "    public static void main(String[] args) {\n" +
            "        java.util.Scanner sc = new java.util.Scanner(System.in);\n" +
            "        int n = sc.nextInt();\n" +
            "        int target = sc.nextInt();\n" +
            "        int[] arr = new int[n];\n" +
            "        for (int i = 0; i < n; i++) arr[i] = sc.nextInt();\n" +
            "        \n" +
            "        // BUG: Only checks first two elements!\n" +
            "        if (arr[0] + arr[1] == target) {\n" +
            "            System.out.println(\"0 1\");\n" +
            "        } else {\n" +
            "            System.out.println(\"-1 -1\");\n" +
            "        }\n" +
            "    }\n" +
            "}";

        File codeFile = createTestFile("TwoSumPartial.java", partialCode);
        List<CodeEvaluator.TestCase> testCases = Arrays.asList(
            new CodeEvaluator.TestCase("Test 1: First two elements sum", "4\n9\n2 7 11 15", "0 1", 33),
            new CodeEvaluator.TestCase("Test 2: Middle elements sum", "4\n13\n3 2 4 8", "2 3", 33),
            new CodeEvaluator.TestCase("Test 3: Last elements sum", "4\n19\n1 2 8 11", "2 3", 34)
        );

        System.out.println("  Running tests on partially correct code...");
        CodeEvaluator evaluator = new CodeEvaluator();
        CodeEvaluator.CodeEvaluationResult result = evaluator.evaluateCode(
            codeFile, testCases, "STU005", "TEST-PARTIAL-001"
        );

        System.out.println("  Results:");
        System.out.println("    - Compilation: " + (result.compilationSuccess ? "Success" : "Failed"));
        System.out.println("    - Score: " + result.score + "/100 (Partial credit)");
        long passedCount = result.testResults.stream().filter(t -> t.passed).count();
        System.out.println("    - Tests Passed: " + passedCount + "/" + result.totalTests);

        assertTrue(result.compilationSuccess, "Code should compile");
        assertTrue(result.score < 100, "Not all tests should pass");
        assertTrue(result.score > 0, "At least one test should pass");

        System.out.println("  Partial credit awarded correctly!\n");
    }
}
