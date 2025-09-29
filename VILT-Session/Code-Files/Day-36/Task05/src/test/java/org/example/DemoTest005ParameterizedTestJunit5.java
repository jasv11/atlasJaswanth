package org.example;
/*
Parameterized Test - used to test a Test case with different parameters.
use @ParameterizedTest annotations.
*/
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DemoTest005ParameterizedTestJunit5 {

   @ParameterizedTest
   @ValueSource(ints = {100, 25, 30, 70, 40})
   void testSquare(int value) {
       int result = square(value);
       assertEquals(value * value, result, " if wrong");
   }

   private int square(int number) {
       return number * number;
   }
}