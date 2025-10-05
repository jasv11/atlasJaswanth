package com.elearning.app;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Production Test: Score Manager")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ScoreManagerIntegrationTest {

    private ScoreManager scoreManager;

    @BeforeEach
    void setup() {
        scoreManager = ScoreManager.getInstance();
        scoreManager.clearCache();
    }

    @Test
    @Order(1)
    @DisplayName("Test 1: Score save and retrieve works")
    void testScoreSaveAndRetrieve() {
        System.out.println("\n[PRODUCTION TEST] Verifying score save/retrieve...");

        StudentScore score = new StudentScore(
            "PROD-SCORE-001",
            "PROD-STU-001",
            "Production Test Student",
            85,
            "Good work",
            "test.java"
        );

        scoreManager.saveScore(score);

        StudentScore retrieved = scoreManager.getScore("PROD-SCORE-001");

        assertNotNull(retrieved, "Score must be retrievable");
        assertEquals("PROD-STU-001", retrieved.getStudentId());
        assertEquals(85, retrieved.getScore());

        System.out.println("  [PASS] Score save/retrieve working correctly");
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Filter by student ID works")
    void testFilterByStudentId() {
        System.out.println("\n[PRODUCTION TEST] Verifying student score filtering...");

        scoreManager.saveScore(new StudentScore("A1", "STU001", "Student1", 80, "Good", "f1.java"));
        scoreManager.saveScore(new StudentScore("A2", "STU001", "Student1", 90, "Excellent", "f2.java"));
        scoreManager.saveScore(new StudentScore("A3", "STU002", "Student2", 75, "Good", "f3.java"));

        String result = scoreManager.getScoresByStudentId("STU001");

        assertTrue(result.contains("\"success\": true"), "Must return success");
        assertTrue(result.contains("\"count\": 2"), "Must find 2 scores for STU001");

        System.out.println("  [PASS] Student filtering working correctly");
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Cache performance is acceptable")
    void testCachePerformance() {
        System.out.println("\n[PRODUCTION TEST] Verifying cache performance...");

        StudentScore score = new StudentScore(
            "PERF-TEST-001", "PERF-STU-001", "Performance Test",
            95, "Excellent", "perf.java"
        );

        scoreManager.saveScore(score);

        long start = System.nanoTime();
        StudentScore retrieved = scoreManager.getScore("PERF-TEST-001");
        long duration = System.nanoTime() - start;

        assertNotNull(retrieved, "Score must be retrievable");
        assertTrue(duration < 10_000_000, "Cache retrieval must be fast (< 10ms)");

        System.out.println("  [PASS] Cache performance acceptable: " + duration + " ns");
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Non-existent score returns null")
    void testNonExistentScore() {
        System.out.println("\n[PRODUCTION TEST] Verifying null handling...");

        StudentScore score = scoreManager.getScore("NON-EXISTENT-ID");

        assertNull(score, "Non-existent score must return null");

        System.out.println("  [PASS] Null handling working correctly");
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: Cache statistics work")
    void testCacheStatistics() {
        System.out.println("\n[PRODUCTION TEST] Verifying cache statistics...");

        scoreManager.clearCache();
        assertEquals(0, scoreManager.getCacheSize(), "Cache should start empty");

        scoreManager.saveScore(new StudentScore("T1", "S1", "N1", 80, "F1", "f1.java"));
        scoreManager.saveScore(new StudentScore("T2", "S2", "N2", 90, "F2", "f2.java"));

        assertEquals(2, scoreManager.getCacheSize(), "Cache should have 2 entries");

        String stats = scoreManager.getCacheStats();
        assertTrue(stats.contains("cacheSize"), "Stats must include cache size");

        System.out.println("  [PASS] Cache statistics working correctly");
    }

    @Test
    @Order(6)
    @DisplayName("Test 6: getAllScoresAsJson works")
    void testGetAllScoresAsJson() {
        System.out.println("\n[PRODUCTION TEST] Verifying getAllScoresAsJson...");

        scoreManager.clearCache();
        scoreManager.saveScore(new StudentScore("J1", "JS1", "JsonStudent", 88, "JSON test", "json.java"));

        String jsonResult = scoreManager.getAllScoresAsJson();

        assertNotNull(jsonResult, "JSON result must not be null");
        assertTrue(jsonResult.contains("scores"), "JSON must contain scores array");
        assertTrue(jsonResult.contains("count"), "JSON must contain count");

        System.out.println("  [PASS] getAllScoresAsJson working correctly");
    }
}
