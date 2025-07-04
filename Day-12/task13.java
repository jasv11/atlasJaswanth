import java.util.HashMap;
import java.util.Map;

public class task13 {
    public static void main(String[] args) {
        HashMap<String, Integer> hm = new HashMap<>();
        
        hm.put("Anitha", 101);
        hm.put("Kavitha", 102);
        hm.put("Meera", 103);

        // Using get method
        System.out.println("Anitha's value: " + hm.get("Anitha"));
        System.out.println("Kavitha's value: " + hm.get("Kavitha"));
        System.out.println("Meera's value: " + hm.get("Meera"));

        for (Map.Entry<String, Integer> e : hm.entrySet())
            System.out.println(e.getKey() + " " + e.getValue());
    }
}
