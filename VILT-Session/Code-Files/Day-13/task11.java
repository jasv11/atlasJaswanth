import java.util.LinkedList;
import java.util.Arrays;

public class task11 {
    public static void main(String[] args) {
        LinkedList<String> fruits = new LinkedList<>();

        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Orange");
        fruits.add("Mango");
        fruits.add("Grapes");

        System.out.println("Displaying LinkedList in different ways:\n");

        System.out.println("1. Direct printing (toString method):");
        System.out.println(fruits);

        System.out.println("\n2. Using toString() explicitly:");
        System.out.println(fruits.toString());

        System.out.println("\n3. Converting to array and printing:");
        System.out.println(Arrays.toString(fruits.toArray()));

        System.out.println("\n4. Using String.join:");
        System.out.println(String.join(" -> ", fruits) + " -> null");

        System.out.println("\n5. Using get() method in for loop:");
        for(int i = 0; i < fruits.size(); i++) {
            System.out.print(fruits.get(i));
            if(i < fruits.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println(" -> null");

        System.out.println("\n6. Using for-each loop:");
        for(String fruit : fruits) {
            System.out.print(fruit + " -> ");
        }
        System.out.println("null");

        System.out.println("\nAfter modifications:");
        
        fruits.set(0, "Pineapple");
        fruits.set(1, "Strawberry");

        System.out.println("\nAfter updating first two elements:");
        System.out.println("Direct print: " + fruits);
        System.out.println("String.join: " + String.join(" -> ", fruits) + " -> null");

        fruits.removeFirst();
        fruits.removeLast();

        System.out.println("\nAfter removing first and last elements:");
        System.out.println("Direct print: " + fruits);
        System.out.println("String.join: " + String.join(" -> ", fruits) + " -> null");

        LinkedList<String> emptyList = new LinkedList<>();
        System.out.println("\nDisplaying empty list:");
        System.out.println("Empty list direct print: " + emptyList);
        System.out.println("Empty list String.join: " + String.join(" -> ", emptyList) + " -> null");

        System.out.println("\nAdditional List Information:");
        System.out.println("Size of list: " + fruits.size());
        System.out.println("First element: " + (fruits.isEmpty() ? "none" : fruits.getFirst()));
        System.out.println("Last element: " + (fruits.isEmpty() ? "none" : fruits.getLast()));
    }
}