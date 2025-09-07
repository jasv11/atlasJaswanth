package task01;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Task01Test {
    private Task01 task = new Task01();

    @Test
    @Tag("firstPriority")
    void testMethod01() {
        assertEquals("Hello JUnit!", task.getMessage());
    }
    
    @Test
    @Tag("firstPriority")
    void runTestcase02() {
        assertEquals(4, task.add(2, 2));
    } 

    @Test
    @Tag("fastTag")
    void testMethod03() {
        assertEquals(0, task.add(0, 0));
    }
    
    @Test
    @Tag("slowTag")
    void runTestcase04() {
        assertEquals(10, task.add(7, 3));
    }    
}