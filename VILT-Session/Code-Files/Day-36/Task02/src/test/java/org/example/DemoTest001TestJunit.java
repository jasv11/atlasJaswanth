package org.example;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class DemoTest001TestJunit {
    
    @Test
    public void simpleTestMethod() {
        System.out.println("Running simple test method");
        assertTrue(true);
    }
}