import java.util.LinkedList;

public class task10 {
    public static void main(String[] args) {
        LinkedList<String> fruits = new LinkedList<>();

        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Orange");
        fruits.add("Mango");
        fruits.add("Grapes");

        System.out.println("Original LinkedList:");
        
        System.out.println("\n1. Display using get() method in for loop:");
        for(int i = 0; i < fruits.size(); i++) {
            System.out.print(fruits.get(i));
            if(i < fruits.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println(" -> null");

        System.out.println("\n2. Display using for-each loop:");
        for(String fruit : fruits) {
            System.out.print(fruit + " -> ");
        }
        System.out.println("null");

        System.out.println("\nUpdating elements:");
        fruits.set(0, "Pineapple");
        fruits.set(1, "Strawberry");

        System.out.println("\nAfter updates:");
        
        System.out.println("\n1. Display using get() method in for loop:");
        for(int i = 0; i < fruits.size(); i++) {
            System.out.print(fruits.get(i));
            if(i < fruits.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println(" -> null");

        System.out.println("\n2. Display using for-each loop:");
        for(String fruit : fruits) {
            System.out.print(fruit + " -> ");
        }
        System.out.println("null");

        System.out.println("\nRemoving elements:");
        fruits.removeFirst();
        fruits.removeLast();

        System.out.println("\nAfter removing first and last elements:");
        
        System.out.println("\n1. Display using get() method in for loop:");
        for(int i = 0; i < fruits.size(); i++) {
            System.out.print(fruits.get(i));
            if(i < fruits.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println(" -> null");

        System.out.println("\n2. Display using for-each loop:");
        for(String fruit : fruits) {
            System.out.print(fruit + " -> ");
        }
        System.out.println("null");

        System.out.println("\nError Handling Demonstration:");
        try {
            System.out.println("Trying to access invalid index:");
            System.out.println(fruits.get(10)); 
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        LinkedList<String> emptyList = new LinkedList<>();
        System.out.println("\nDisplaying empty list:");
        
        System.out.println("\n1. Display using get() method in for loop:");
        for(int i = 0; i < emptyList.size(); i++) {
            System.out.print(emptyList.get(i) + " -> ");
        }
        System.out.println("null");

        System.out.println("\n2. Display using for-each loop:");
        for(String item : emptyList) {
            System.out.print(item + " -> ");
        }
        System.out.println("null");
    }
}