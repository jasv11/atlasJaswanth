package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DemoTest001TestJunit {
    @Test
    public void Testcase1() {
        String str = "Prasunamba";
        assertEquals("prasunamba", str);  
    }
}