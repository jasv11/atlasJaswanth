package task02;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

public class Task02Test {
    
    @Test
    @Tag("firstPriority")
    void testPizzaCreation() {
        System.out.println("Testing Pizza Creation");
        PizzaFactory factory = new PepperoniPizzaFactory();
        Pizza pizza = factory.createPizza();
        assertNotNull(pizza);
        assertTrue(pizza instanceof PepperoniPizza);
    }

    @Test
    @Tag("fastTag")
    void testPizzaOperations() {
        System.out.println("Testing Pizza Operations");
        PizzaFactory factory = new PepperoniPizzaFactory();
        Pizza pizza = factory.createPizza();
        
        assertDoesNotThrow(() -> {
            pizza.preparation();
            pizza.baking();
            pizza.cutting();
            pizza.boxing();
        });
    }

    @Test
    @Tag("firstPriority")
    void testFactoryPattern() {
        System.out.println("Testing Factory Pattern Implementation");
        PizzaFactory factory = new PepperoniPizzaFactory();
        
        Pizza pizza1 = factory.createPizza();
        Pizza pizza2 = factory.createPizza();
        
        assertNotNull(pizza1);
        assertNotNull(pizza2);
        assertTrue(pizza1 instanceof PepperoniPizza);
        assertTrue(pizza2 instanceof PepperoniPizza);
        assertNotSame(pizza1, pizza2, "Factory should create new instances");
    }
}