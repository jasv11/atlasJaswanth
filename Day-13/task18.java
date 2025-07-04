import java.util.HashMap;
import java.util.Map;

public class task18 {
    public static void main(String[] args) {
        HashMap<String, Integer> firstMap = new HashMap<String, Integer>();
        firstMap.put("One", 1);
        firstMap.put("Two", 2);
        firstMap.put("Three", 3);
        firstMap.put("Four", 4);
        firstMap.put("Five", 5);
        
        HashMap<String, Integer> secondMap = new HashMap<String, Integer>(firstMap);
        
        HashMap<String, Integer> thirdMap = new HashMap<String, Integer>();
        thirdMap.putAll(firstMap);
        
        System.out.println("Original Map:");
        System.out.println(firstMap);
        
        System.out.println("\nSecond Map (copied using constructor):");
        System.out.println(secondMap);
        
        System.out.println("\nThird Map (copied using putAll):");
        System.out.println(thirdMap);
        
        firstMap.put("Six", 6);
        
        System.out.println("\nAfter modifying first map:");
        System.out.println("First Map: " + firstMap);
        System.out.println("Second Map: " + secondMap);
        System.out.println("Third Map: " + thirdMap);
        
        secondMap.clear();
        
        System.out.println("\nAfter clearing second map:");
        System.out.println("First Map: " + firstMap);
        System.out.println("Second Map: " + secondMap);
        System.out.println("Third Map: " + thirdMap);
    }
}