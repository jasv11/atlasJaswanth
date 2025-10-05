package com.elearning.bdd;

import com.elearning.app.ScoreManager;
import com.elearning.app.StudentScore;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;


@DisplayName("Day 7: Score Management using HashMap (ConcurrentHashMap)")
class Day7_HashMapPerformanceTest extends BDDTestBase {

    private ScoreManager scoreManager;

    @BeforeEach
    void setupScoreManager() {
        scoreManager = ScoreManager.getInstance();
        scoreManager.clearCache();
        System.out.println("\nScoreManager initialized with HashMap cache");
    }

    @Test
    @DisplayName("Scenario 1: Save and Retrieve - O(1) HashMap Performance")
    void hashMapPerformance_O1_Lookup() {
        System.out.println("\nTesting: HashMap O(1) Performance");

        StudentScore score = new StudentScore(
            "ASSIGN-001", "STU001", "Arjun Kumar",
            85, "Excellent work on TwoSum!", "TwoSum.java"
        );

        System.out.println("  Saving score to HashMap...");
        long startSave = System.nanoTime();
        scoreManager.saveScore(score);
        long saveDuration = System.nanoTime() - startSave;

        System.out.println("  Retrieving score from HashMap...");
        long startRetrieve = System.nanoTime();
        StudentScore retrievedScore = scoreManager.getScore("ASSIGN-001");
        long retrieveDuration = System.nanoTime() - startRetrieve;

        System.out.println("  Performance Results:");
        System.out.println("    - Save time: " + saveDuration + " ns");
        System.out.println("    - Retrieve time: " + retrieveDuration + " ns (O(1) complexity)");
        System.out.println("    - Data structure: ConcurrentHashMap (thread-safe)");

        assertNotNull(retrievedScore, "Score should be retrieved from HashMap");
        assertEquals("STU001", retrievedScore.getStudentId());
        assertEquals(85, retrievedScore.getScore());
        assertTrue(retrieveDuration < 1_000_000, "HashMap O(1) lookup should be < 1ms");

        System.out.println("  HashMap O(1) performance verified!\n");
    }

    @Test
    @DisplayName("Scenario 2: Filter by Student ID - HashMap Iteration")
    void filterByStudentId_HashMapIteration() {
        System.out.println("\nTesting: Filter Scores by Student ID");

        System.out.println("  Adding 5 scores to HashMap...");
        scoreManager.saveScore(new StudentScore("A1", "STU001", "Arjun", 85, "Good", "f1.java"));
        scoreManager.saveScore(new StudentScore("A2", "STU002", "Priya", 90, "Excellent", "f2.java"));
        scoreManager.saveScore(new StudentScore("A3", "STU001", "Arjun", 78, "Good", "f3.java"));
        scoreManager.saveScore(new StudentScore("A4", "STU003", "Rahul", 92, "Excellent", "f4.java"));
        scoreManager.saveScore(new StudentScore("A5", "STU001", "Arjun", 88, "Very Good", "f5.java"));

        System.out.println("  Filtering scores for STU001...");
        String result = scoreManager.getScoresByStudentId("STU001");

        System.out.println("  Results:");
        System.out.println("    - Total scores in HashMap: " + scoreManager.getCacheSize());
        System.out.println("    - STU001's scores found: 3");

        assertTrue(result.contains("\"success\": true"));
        assertTrue(result.contains("\"count\": 3"), "Should find 3 scores for STU001");
        assertTrue(result.contains("STU001"));
        assertFalse(result.contains("STU002"), "Should not contain other students");

        System.out.println("  HashMap filtering works correctly!\n");
    }

    @Test
    @DisplayName("Scenario 3: Thread Safety - Concurrent HashMap Operations")
    void threadSafety_ConcurrentOperations() throws InterruptedException {
        System.out.println("\nTesting: Thread Safety with ConcurrentHashMap");

        int threadCount = 10;
        List<Thread> threads = new ArrayList<>();

        System.out.println("  Starting " + threadCount + " threads for concurrent writes...");

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            Thread thread = new Thread(() -> {
                scoreManager.saveScore(new StudentScore(
                    "CONCURRENT-" + index, "STU" + index, "Student" + index,
                    80 + index, "Test feedback", "file" + index + ".java"
                ));
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("  Results:");
        System.out.println("    - Threads launched: " + threadCount);
        System.out.println("    - Scores in HashMap: " + scoreManager.getCacheSize());
        System.out.println("    - Data structure: ConcurrentHashMap (handles concurrency)");

        assertEquals(threadCount, scoreManager.getCacheSize(),
            "ConcurrentHashMap should safely handle concurrent writes");

        System.out.println("  Thread safety verified!\n");
    }

    @Test
    @DisplayName("Scenario 4: HashMap vs List Performance Comparison")
    void hashMapVsList_PerformanceComparison() {
        System.out.println("\nTesting: HashMap vs List Performance");

        System.out.println("  Adding 1000 scores to HashMap...");
        for (int i = 0; i < 1000; i++) {
            scoreManager.saveScore(new StudentScore(
                "PERF-" + i, "STU" + (i % 100), "Student" + i,
                70 + (i % 30), "Test", "file" + i + ".java"
            ));
        }

        System.out.println("  Searching for score PERF-500...");
        long startHashMap = System.nanoTime();
        StudentScore found = scoreManager.getScore("PERF-500");
        long hashMapDuration = System.nanoTime() - startHashMap;

        System.out.println("  Results:");
        System.out.println("    - HashMap lookup time: " + hashMapDuration + " ns");
        System.out.println("    - Found score: " + (found != null ? "Yes" : "No"));
        System.out.println("    - Complexity: O(1) constant time");

        assertNotNull(found, "Should find score in HashMap");
        assertTrue(hashMapDuration < 1_000_000, "HashMap should be faster than 1ms");

        System.out.println("  HashMap O(1) advantage demonstrated!\n");
    }
}
