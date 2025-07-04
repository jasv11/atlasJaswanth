import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class task16 {
    public static void main(String[] args) {
        HashMap<String, Integer> hm = new HashMap<>();
        
        hm.put("Anitha", 101);
        hm.put("Kavitha", 102);
        hm.put("Meera", 103);
        hm.put(null, 104);      
        hm.put(null, 105);     

        // Using get method
        System.out.println("Anitha's value: " + hm.get("Anitha"));
        System.out.println("Kavitha's value: " + hm.get("Kavitha"));
        System.out.println("Meera's value: " + hm.get("Meera"));
        System.out.println("Null key's value: " + hm.get(null));

        System.out.println("\nAll entries in HashMap:");
        for (Map.Entry<String, Integer> e : hm.entrySet())
            System.out.println(e.getKey() + " " + e.getValue());

        // Making HashMap synchronized
        Map<String, Integer> syncMap = Collections.synchronizedMap(hm);
        
        System.out.println("\nSynchronized Map entries:");
        for (Map.Entry<String, Integer> e : syncMap.entrySet())
            System.out.println(e.getKey() + " " + e.getValue());
    }
}