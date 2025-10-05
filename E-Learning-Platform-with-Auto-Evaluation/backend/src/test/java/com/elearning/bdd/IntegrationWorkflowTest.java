package com.elearning.bdd;

import com.elearning.app.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.*;

@DisplayName("Integration: Complete E-Learning Workflow")
class IntegrationWorkflowTest extends BDDTestBase {

    @Test
    @DisplayName("Complete Workflow: Upload -> Evaluate -> Store -> Retrieve")
    void completeWorkflow_EndToEnd() throws Exception {
        System.out.println("\nTesting: Complete E-Learning Workflow");
        System.out.println("  Steps: Code Upload -> Compilation -> Testing -> Scoring -> Storage -> Retrieval\n");

        ScoreManager scoreManager = ScoreManager.getInstance();
        scoreManager.clearCache();

        String helloWorldCode =
            "public class HelloWorld {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"Hello E-Learning Platform\");\n" +
            "    }\n" +
            "}";
        File codeFile = createTestFile("HelloWorld.java", helloWorldCode);
        List<CodeEvaluator.TestCase> tests = Arrays.asList(
            new CodeEvaluator.TestCase("Test 1", "", "Hello E-Learning Platform", 100)
        );

        System.out.println("  Step 1: Compiling and evaluating code...");
        CodeEvaluator codeEval = new CodeEvaluator();
        CodeEvaluator.CodeEvaluationResult evalResult =
            codeEval.evaluateCode(codeFile, tests, "STU999", "INTEGRATION-TEST");
        System.out.println("    Compilation: Success");
        System.out.println("    Tests: Passed");

        System.out.println("  Step 2: Storing score in HashMap...");
        StudentScore score = new StudentScore(
            "ASSIGN-INTEGRATION", "STU999", "Integration Test Student",
            evalResult.score, evalResult.feedback, "HelloWorld.java"
        );
        scoreManager.saveScore(score);
        System.out.println("    Score saved to ConcurrentHashMap");

        System.out.println("  Step 3: Retrieving score from HashMap...");
        StudentScore retrieved = scoreManager.getScore("ASSIGN-INTEGRATION");
        System.out.println("    Score retrieved (O(1) lookup)");

        System.out.println("\n  Final Results:");
        System.out.println("    - Student ID: " + retrieved.getStudentId());
        System.out.println("    - Score: " + retrieved.getScore() + "/100");
        System.out.println("    - Feedback: " + evalResult.feedback.substring(0, Math.min(50, evalResult.feedback.length())) + "...");

        assertTrue(evalResult.compilationSuccess, "Step 1: Code should compile");
        assertEquals(100, evalResult.score, "Step 1: Tests should pass");
        assertNotNull(retrieved, "Step 3: Score should be retrievable");
        assertEquals("STU999", retrieved.getStudentId(), "Step 3: Correct student");
        assertEquals(100, retrieved.getScore(), "Step 3: Correct score");

        System.out.println("\n  COMPLETE WORKFLOW SUCCESSFUL!");
        System.out.println("  All components working together perfectly!\n");
    }

    @Test
    @DisplayName("Assignment Evaluation: Text assignment with answer key")
    void textAssignment_WithAnswerKey() throws Exception {
        System.out.println("\nTesting: Text Assignment Evaluation");

        Assignment assignment = new Assignment(
            "ASSIGN-TEXT-001",
            "Java Basics Quiz",
            "Test your Java knowledge",
            "TEACH001",
            "Ms. Sharma"
        );

        assignment.addQuestion(new Question(
            "Q1",
            "What is OOP?",
            "Object-Oriented Programming is a programming paradigm based on objects and classes",
            10,
            new String[]{"OOP", "objects", "classes", "programming"}
        ));

        String studentAnswer = "OOP stands for Object-Oriented Programming. " +
                              "It is a programming paradigm that uses objects and classes.";
        File answerFile = createTestFile("student_answer.txt", studentAnswer);

        System.out.println("  Evaluating against teacher's answer key...");
        AssignmentEvaluator evaluator = new AssignmentEvaluator();
        EvaluationResult result = evaluator.evaluateWithAnswerKey(answerFile, "student_answer.txt", assignment);

        System.out.println("  Results:");
        System.out.println("    - Score: " + result.getScore() + "/100");
        System.out.println("    - Feedback length: " + result.getFeedback().length() + " characters");

        assertNotNull(result, "Evaluation result should not be null");
        assertTrue(result.getScore() > 0, "Student should get points for keyword matches");
        assertNotNull(result.getFeedback(), "Should provide feedback");

        System.out.println("  Text assignment evaluation works!\n");
    }
}
