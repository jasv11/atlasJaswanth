import java.util.Hashtable;
import java.util.Map;

public class task12 {
    public static void main(String[] args) {
        Hashtable<String, Integer> ht = new Hashtable<>();
        
        ht.put("Anitha", 101);
        ht.put("Kavitha", 102);
        ht.put("Meera", 103);

        // use  get method of Ht
        System.out.println("Anitha's value: " + ht.get("Anitha"));
        System.out.println("Kavitha's value: " + ht.get("Kavitha"));
        System.out.println("Meera's value: " + ht.get("Meera"));

        for (Map.Entry<String, Integer> e : ht.entrySet())
            System.out.println(e.getKey() + " " + e.getValue());
    }
}