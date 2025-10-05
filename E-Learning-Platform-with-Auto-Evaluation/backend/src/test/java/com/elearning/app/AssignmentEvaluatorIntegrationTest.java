package com.elearning.app;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;

@DisplayName("Production Test: Assignment Evaluator")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AssignmentEvaluatorIntegrationTest {

    private static final String TEST_WORKSPACE = "/tmp/assignment_test_workspace";
    private File testWorkspace;
    private AssignmentEvaluator evaluator;

    @BeforeEach
    void setup() {
        testWorkspace = new File(TEST_WORKSPACE);
        testWorkspace.mkdirs();
        evaluator = new AssignmentEvaluator();
    }

    @AfterEach
    void cleanup() {
        deleteDirectory(testWorkspace);
    }

    @Test
    @Order(1)
    @DisplayName("Test 1: Text file extraction works")
    void testTextFileExtraction() throws Exception {
        System.out.println("\n[PRODUCTION TEST] Verifying text file extraction...");

        String content = "This is a test assignment answer. It contains multiple lines.";
        File textFile = createTestFile("answer.txt", content);

        String extracted = evaluator.extractTextFromFile(textFile, "answer.txt");

        assertNotNull(extracted, "Extracted text must not be null");
        assertTrue(extracted.contains("test assignment"), "Must extract content correctly");

        System.out.println("  [PASS] Text file extraction working correctly");
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Answer key evaluation works")
    void testAnswerKeyEvaluation() throws Exception {
        System.out.println("\n[PRODUCTION TEST] Verifying answer key evaluation...");

        Assignment assignment = new Assignment(
            "EVAL-TEST-001",
            "Production Test Assignment",
            "Test assignment evaluation",
            "TEACHER-001",
            "Test Teacher"
        );

        assignment.addQuestion(new Question(
            "Q1",
            "What is Java?",
            "Java is a programming language that is platform-independent and object-oriented",
            10,
            new String[]{"Java", "programming", "platform-independent", "object-oriented"}
        ));

        String studentAnswer = "Java is a programming language. It is object-oriented and works on multiple platforms.";
        File answerFile = createTestFile("student_answer.txt", studentAnswer);

        EvaluationResult result = evaluator.evaluateWithAnswerKey(answerFile, "student_answer.txt", assignment);

        assertNotNull(result, "Evaluation result must not be null");
        assertTrue(result.getScore() >= 0 && result.getScore() <= 100, "Score must be in valid range");
        assertNotNull(result.getFeedback(), "Feedback must be provided");
        assertTrue(result.getScore() > 0, "Student should get points for keyword matches");

        System.out.println("  [PASS] Answer key evaluation working correctly");
        System.out.println("  [INFO] Score: " + result.getScore() + "/100");
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Keyword matching works")
    void testKeywordMatching() throws Exception {
        System.out.println("\n[PRODUCTION TEST] Verifying keyword matching...");

        Assignment assignment = new Assignment(
            "KEYWORD-TEST-001",
            "Keyword Test",
            "Test keyword matching",
            "TEACHER-002",
            "Test Teacher"
        );

        assignment.addQuestion(new Question(
            "Q1",
            "Define OOP",
            "Object-Oriented Programming uses objects and classes",
            10,
            new String[]{"Object-Oriented", "objects", "classes"}
        ));

        String goodAnswer = "Object-Oriented Programming is about using objects and classes to organize code.";
        File goodFile = createTestFile("good_answer.txt", goodAnswer);

        EvaluationResult goodResult = evaluator.evaluateWithAnswerKey(goodFile, "good_answer.txt", assignment);

        String poorAnswer = "It's a way to write code.";
        File poorFile = createTestFile("poor_answer.txt", poorAnswer);

        EvaluationResult poorResult = evaluator.evaluateWithAnswerKey(poorFile, "poor_answer.txt", assignment);

        assertTrue(goodResult.getScore() > poorResult.getScore(),
            "Answer with keywords should score higher");

        System.out.println("  [PASS] Keyword matching working correctly");
        System.out.println("  [INFO] Good answer score: " + goodResult.getScore());
        System.out.println("  [INFO] Poor answer score: " + poorResult.getScore());
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Empty file handling works")
    void testEmptyFileHandling() throws Exception {
        System.out.println("\n[PRODUCTION TEST] Verifying empty file handling...");

        File emptyFile = createTestFile("empty.txt", "");

        Assignment assignment = new Assignment(
            "EMPTY-TEST-001",
            "Empty Test",
            "Test empty file",
            "TEACHER-003",
            "Test Teacher"
        );

        assignment.addQuestion(new Question(
            "Q1",
            "Test question",
            "Test answer",
            10,
            new String[]{"test"}
        ));

        EvaluationResult result = evaluator.evaluateWithAnswerKey(emptyFile, "empty.txt", assignment);

        assertNotNull(result, "Result must not be null for empty file");
        assertEquals(0, result.getScore(), "Empty file should score 0");

        System.out.println("  [PASS] Empty file handling working correctly");
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: Assignment JSON serialization works")
    void testAssignmentJsonSerialization() {
        System.out.println("\n[PRODUCTION TEST] Verifying Assignment JSON serialization...");

        Assignment assignment = new Assignment(
            "JSON-TEST-001",
            "JSON Test Assignment",
            "Test JSON serialization",
            "TEACHER-004",
            "Test Teacher"
        );

        assignment.addQuestion(new Question(
            "Q1",
            "Test question",
            "Test answer",
            10,
            new String[]{"test", "keyword"}
        ));

        String json = assignment.toJson();

        assertNotNull(json, "JSON must not be null");
        assertTrue(json.contains("assignmentId"), "JSON must contain assignmentId");
        assertTrue(json.contains("JSON-TEST-001"), "JSON must contain actual ID");
        assertTrue(json.contains("questions"), "JSON must contain questions array");

        System.out.println("  [PASS] Assignment JSON serialization working correctly");
    }

    @Test
    @Order(6)
    @DisplayName("Test 6: Question JSON serialization works")
    void testQuestionJsonSerialization() {
        System.out.println("\n[PRODUCTION TEST] Verifying Question JSON serialization...");

        Question question = new Question(
            "Q-TEST-001",
            "What is testing?",
            "Testing is verification of software correctness",
            15,
            new String[]{"testing", "verification", "software"}
        );

        String json = question.toJson();

        assertNotNull(json, "JSON must not be null");
        assertTrue(json.contains("questionId"), "JSON must contain questionId");
        assertTrue(json.contains("Q-TEST-001"), "JSON must contain actual ID");
        assertTrue(json.contains("keywords"), "JSON must contain keywords");

        System.out.println("  [PASS] Question JSON serialization working correctly");
    }

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
