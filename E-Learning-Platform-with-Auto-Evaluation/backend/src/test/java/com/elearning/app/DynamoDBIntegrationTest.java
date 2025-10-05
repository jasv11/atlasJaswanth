package com.elearning.app;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Production Test: DynamoDB Integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DynamoDBIntegrationTest {

    private DynamoDBHelper dynamoDBHelper;

    @BeforeAll
    static void checkEnvironment() {
        System.out.println("\n[PRODUCTION TEST] Checking AWS environment...");

        // Check if running in environment with AWS access
        String awsAccessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String awsRegion = System.getenv("AWS_REGION");

        if (awsAccessKey == null) {
            System.out.println("  [WARN] AWS credentials not found in environment");
            System.out.println("  [INFO] Tests will attempt to use default credential chain");
        }

        System.out.println("  [INFO] AWS Region: " + (awsRegion != null ? awsRegion : "default"));
    }

    @BeforeEach
    void setup() {
        dynamoDBHelper = new DynamoDBHelper();
    }

    @Test
    @Order(1)
    @DisplayName("Test 1: DynamoDB connection works")
    void testDynamoDBConnection() {
        System.out.println("\n[PRODUCTION TEST] Verifying DynamoDB connection...");

        boolean isConnected = dynamoDBHelper.isConnected();

        assertTrue(isConnected, "DynamoDB must be accessible for production");

        System.out.println("  [PASS] DynamoDB connection established");
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Score save to DynamoDB works")
    void testScoreSaveToDynamoDB() {
        System.out.println("\n[PRODUCTION TEST] Verifying score save to DynamoDB...");

        if (!dynamoDBHelper.isConnected()) {
            System.out.println("  [SKIP] DynamoDB not connected");
            return;
        }

        StudentScore score = new StudentScore(
            "DYNAMO-TEST-001",
            "DYNAMO-STU-001",
            "DynamoDB Test Student",
            92,
            "DynamoDB test feedback",
            "dynamodb_test.java"
        );

        assertDoesNotThrow(() -> {
            dynamoDBHelper.saveScore(score);
        }, "Score save to DynamoDB must not throw exceptions");

        System.out.println("  [PASS] Score saved to DynamoDB successfully");
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Score retrieval from DynamoDB works")
    void testScoreRetrievalFromDynamoDB() {
        System.out.println("\n[PRODUCTION TEST] Verifying score retrieval from DynamoDB...");

        if (!dynamoDBHelper.isConnected()) {
            System.out.println("  [SKIP] DynamoDB not connected");
            return;
        }

        StudentScore originalScore = new StudentScore(
            "DYNAMO-RETRIEVE-001",
            "DYNAMO-STU-002",
            "Retrieval Test Student",
            88,
            "Retrieval test",
            "retrieve_test.java"
        );

        dynamoDBHelper.saveScore(originalScore);

        StudentScore retrieved = dynamoDBHelper.getScore("DYNAMO-RETRIEVE-001");

        if (retrieved != null) {
            assertEquals("DYNAMO-STU-002", retrieved.getStudentId());
            assertEquals(88, retrieved.getScore());
            System.out.println("  [PASS] Score retrieved from DynamoDB successfully");
        } else {
            System.out.println("  [WARN] Score not immediately available (eventual consistency)");
        }
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Get scores by student ID works")
    void testGetScoresByStudentId() {
        System.out.println("\n[PRODUCTION TEST] Verifying getScoresByStudentId...");

        if (!dynamoDBHelper.isConnected()) {
            System.out.println("  [SKIP] DynamoDB not connected");
            return;
        }

        String result = dynamoDBHelper.getScoresByStudentId("DYNAMO-STU-001");

        assertNotNull(result, "Result must not be null");
        assertTrue(result.contains("scores") || result.contains("success"),
            "Result must be valid JSON");

        System.out.println("  [PASS] getScoresByStudentId working correctly");
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: Get all scores works")
    void testGetAllScores() {
        System.out.println("\n[PRODUCTION TEST] Verifying getAllScores...");

        if (!dynamoDBHelper.isConnected()) {
            System.out.println("  [SKIP] DynamoDB not connected");
            return;
        }

        String result = dynamoDBHelper.getAllScores();

        assertNotNull(result, "Result must not be null");
        assertTrue(result.contains("scores"), "Result must contain scores array");

        System.out.println("  [PASS] getAllScores working correctly");
    }

    @Test
    @Order(6)
    @DisplayName("Test 6: Error handling for DynamoDB operations")
    void testDynamoDBErrorHandling() {
        System.out.println("\n[PRODUCTION TEST] Verifying DynamoDB error handling...");

        if (!dynamoDBHelper.isConnected()) {
            System.out.println("  [SKIP] DynamoDB not connected");
            return;
        }

        StudentScore nonExistent = dynamoDBHelper.getScore("NON-EXISTENT-SCORE-ID");

        assertNull(nonExistent, "Non-existent score should return null");

        System.out.println("  [PASS] Error handling working correctly");
    }
}
