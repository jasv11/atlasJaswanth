import java.util.HashMap;
import java.util.Map;

public class task17 {
    public static void main(String[] args) {
        HashMap<String, Integer> hm = new HashMap<String, Integer>(10);
        
        hm.put("One", 1);
        hm.put("Two", 2);
        hm.put("Three", 3);
        hm.put("Four", 4);
        hm.put("Five", 5);
        
        System.out.println("HashMap with capacity 10:");
        System.out.println("Size: " + hm.size());
        
        System.out.println("\nElements in HashMap:");
        for(Map.Entry<String, Integer> entry : hm.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        
        System.out.println("\nGet value for key 'Three': " + hm.get("Three"));
        
        hm.remove("Two");
        System.out.println("\nAfter removing 'Two':");
        System.out.println(hm);
        
        System.out.println("\nContains key 'Four': " + hm.containsKey("Four"));
        System.out.println("Contains value 2: " + hm.containsValue(2));
        
        hm.clear();
        System.out.println("\nAfter clearing HashMap:");
        System.out.println("Size: " + hm.size());
        System.out.println("Is Empty: " + hm.isEmpty());
    }
}