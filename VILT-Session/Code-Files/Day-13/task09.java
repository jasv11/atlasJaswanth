import java.util.LinkedList;

public class task09 {
    public static void main(String[] args) {
        LinkedList<String> fruits = new LinkedList<>();

        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Orange");
        fruits.add("Mango");
        fruits.add("Grapes");

        System.out.println("Original LinkedList:");
        displayList(fruits);

        System.out.println("\nUpdating elements:");
        
        System.out.println("Updating first element from '" + fruits.get(0) + 
                         "' to 'Pineapple'");
        fruits.set(0, "Pineapple");
        
        System.out.println("Updating second element from '" + fruits.get(1) + 
                         "' to 'Strawberry'");
        fruits.set(1, "Strawberry");

        System.out.println("\nLinkedList after updates:");
        displayList(fruits);

        System.out.println("\nRemoving First Element: " + fruits.removeFirst());
        System.out.println("After removing first element:");
        displayList(fruits);

        System.out.println("\nRemoving Last Element: " + fruits.removeLast());
        System.out.println("After removing last element:");
        displayList(fruits);

        System.out.println("\nCurrent First Element: " + fruits.getFirst());
        System.out.println("Current Last Element: " + fruits.getLast());

        System.out.println("\nDisplaying elements using different methods:");
        
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

        System.out.println("\nTesting set() with invalid index:");
        try {
            fruits.set(10, "Invalid"); 
        } catch (Exception e) {
            System.out.println("Error updating element: " + e.getMessage());
        }

        if (!fruits.isEmpty()) {
            System.out.println("\nUpdating last element to 'Kiwi'");
            fruits.set(fruits.size() - 1, "Kiwi");
            System.out.println("After updating last element:");
            displayList(fruits);
        }
    }

    private static void displayList(LinkedList<String> list) {
        if (list.isEmpty()) {
            System.out.println("List is empty");
            return;
        }
        
        System.out.print("List elements: ");
        for (String item : list) {
            System.out.print(item + " -> ");
        }
        System.out.println("null");
        System.out.println("Size of list: " + list.size());
    }
}