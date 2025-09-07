import java.util.LinkedList;
import java.util.Arrays;

public class task12 {
    public static void main(String[] args) {
        LinkedList<String> fruits = new LinkedList<>();

        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Orange");
        fruits.add("Mango");
        fruits.add("Grapes");

        System.out.println("Original LinkedList:");
        System.out.println(fruits);

        System.out.println("\n1. Converting to Object array:");
        Object[] array1 = fruits.toArray();
        System.out.println("Using Object array:");
        System.out.print("Array elements: ");
        for(Object fruit : array1) {
            System.out.print(fruit + " ");
        }
        System.out.println("\nArray length: " + array1.length);

        System.out.println("\n2. Converting to String array:");
        String[] array2 = fruits.toArray(new String[0]);
        System.out.println("Using String array:");
        System.out.print("Array elements: ");
        System.out.println(Arrays.toString(array2));
        System.out.println("Array length: " + array2.length);

        System.out.println("\n3. Converting to String array with exact size:");
        String[] array3 = fruits.toArray(new String[fruits.size()]);
        System.out.println("Using String array (exact size):");
        System.out.print("Array elements: ");
        System.out.println(Arrays.toString(array3));
        System.out.println("Array length: " + array3.length);

        System.out.println("\nAfter modifications:");
        
        fruits.set(0, "Pineapple");
        fruits.set(1, "Strawberry");
        
        Object[] modifiedArray = fruits.toArray();
        System.out.println("Modified list as array:");
        System.out.println(Arrays.toString(modifiedArray));

        fruits.removeFirst();
        fruits.removeLast();

        Object[] shorterArray = fruits.toArray();
        System.out.println("\nArray after removing elements:");
        System.out.println(Arrays.toString(shorterArray));

        LinkedList<String> emptyList = new LinkedList<>();
        Object[] emptyArray = emptyList.toArray();
        System.out.println("\nEmpty list to array:");
        System.out.println("Array length: " + emptyArray.length);
        System.out.println("Array content: " + Arrays.toString(emptyArray));

        LinkedList<Integer> numbers = new LinkedList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        
        Object[] numberArray = numbers.toArray();
        System.out.println("\nInteger LinkedList to array:");
        System.out.println(Arrays.toString(numberArray));
        
        Integer[] intArray = numbers.toArray(new Integer[0]);
        System.out.println("As Integer array:");
        System.out.println(Arrays.toString(intArray));
    }
}