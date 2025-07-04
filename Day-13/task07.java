import java.util.LinkedList;

public class task07 {
    public static void main(String[] args) {
        LinkedList<String> fruits = new LinkedList<>();

        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Orange");
        fruits.add("Mango");
        fruits.add("Grapes");

        System.out.println("LinkedList Operations:\n");

        System.out.println("First Element: " + fruits.getFirst());

        System.out.println("Last Element: " + fruits.getLast());

        
        System.out.println("\nMethod 1 - Direct printing:");
        System.out.println(fruits);

        System.out.println("\nMethod 2 - Using for-each loop:");
        for(String fruit : fruits) {
            System.out.print(fruit + " -> ");
        }
        System.out.println("null");

        System.out.println("\nMethod 3 - Using traditional for loop:");
        for(int i = 0; i < fruits.size(); i++) {
            System.out.print(fruits.get(i));
            if(i < fruits.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println(" -> null");

        System.out.println("\nMethod 4 - Using forEach method:");
        fruits.forEach(fruit -> System.out.print(fruit + " -> "));
        System.out.println("null");

        System.out.println("\nAdditional Information:");
        System.out.println("Size of LinkedList: " + fruits.size());
        System.out.println("Is LinkedList empty? " + fruits.isEmpty());
        System.out.println("Does LinkedList contain 'Mango'? " + fruits.contains("Mango"));

        System.out.println("\nTesting with empty LinkedList:");
        LinkedList<String> emptyList = new LinkedList<>();
        
        try {
            System.out.println("First element of empty list: " + emptyList.getFirst());
        } catch (Exception e) {
            System.out.println("Error getting first element: " + e.getMessage());
        }

        try {
            System.out.println("Last element of empty list: " + emptyList.getLast());
        } catch (Exception e) {
            System.out.println("Error getting last element: " + e.getMessage());
        }
    }
}