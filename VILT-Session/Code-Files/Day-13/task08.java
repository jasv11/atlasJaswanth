import java.util.LinkedList;

public class task08 {
    public static void main(String[] args) {
        LinkedList<String> fruits = new LinkedList<>();

        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Orange");
        fruits.add("Mango");
        fruits.add("Grapes");

        System.out.println("Original LinkedList:");
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

        System.out.println("\nAdditional Remove Operations:");
        
        System.out.println("Removing element at index 1: " + fruits.remove(1));
        System.out.println("After removing element at index 1:");
        displayList(fruits);

        System.out.println("\nRemoving 'Orange': " + fruits.remove("Orange"));
        System.out.println("After removing 'Orange':");
        displayList(fruits);

        System.out.println("\nTesting remove operations on empty list:");
        LinkedList<String> emptyList = new LinkedList<>();
        
        try {
            emptyList.removeFirst();
        } catch (Exception e) {
            System.out.println("Error removing first element: " + e.getMessage());
        }

        try {
            emptyList.removeLast();
        } catch (Exception e) {
            System.out.println("Error removing last element: " + e.getMessage());
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