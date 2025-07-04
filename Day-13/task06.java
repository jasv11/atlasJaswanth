import java.util.LinkedList;

public class task06 {
    public static void main(String[] args) {
        LinkedList<String> fruits = new LinkedList<>();

        System.out.println("Adding fruits to the LinkedList:");
        
        fruits.add("Apple");
        fruits.add("Banana");
        
        fruits.addFirst("Orange");
        
        fruits.addLast("Mango");
        
        fruits.add(2, "Grapes");

        System.out.println("\nFruits in the LinkedList:");
        System.out.println(fruits);

        LinkedList<Integer> numbers = new LinkedList<>();

        numbers.add(10);
        numbers.add(20);
        numbers.add(30);
        numbers.addFirst(5);
        numbers.addLast(40);

        System.out.println("\nNumbers in the LinkedList:");
        System.out.println(numbers);

        System.out.println("\nDemonstrating LinkedList Operations:");
        
        System.out.println("First fruit: " + fruits.getFirst());
        System.out.println("Last fruit: " + fruits.getLast());
        System.out.println("Fruit at index 2: " + fruits.get(2));

        System.out.println("Number of fruits: " + fruits.size());

        System.out.println("Contains 'Apple'? " + fruits.contains("Apple"));

        System.out.println("\nRemoving elements:");
        fruits.removeFirst();  
        System.out.println("After removing first: " + fruits);
        
        fruits.removeLast();   
        System.out.println("After removing last: " + fruits);
        
        fruits.remove("Grapes");  
        System.out.println("After removing 'Grapes': " + fruits);

        fruits.clear();
        System.out.println("\nAfter clearing the list:");
        System.out.println("Is fruits list empty? " + fruits.isEmpty());
    }
}