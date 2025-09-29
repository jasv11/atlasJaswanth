package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DemoTest003TestCases {

    @Test
    void firstTest() {
        assertTrue(true);
    }

    @Test
    void secondTest() {
        assertTrue(true, "This test should pass.");
    }
}