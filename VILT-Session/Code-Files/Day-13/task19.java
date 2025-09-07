import java.util.HashMap;

public class task19 {
    public static void main(String[] args) {
        HashMap<String, Integer> hm1 = new HashMap<String, Integer>();
        hm1.put("A", 1);
        hm1.put("B", 2);
        System.out.println("1. Default HashMap:");
        System.out.println(hm1);

        HashMap<String, Integer> hm2 = new HashMap<String, Integer>(10);
        hm2.put("X", 10);
        hm2.put("Y", 20);
        hm2.put("Z", 30);
        System.out.println("\n2. HashMap with capacity 10:");
        System.out.println(hm2);

        HashMap<String, Integer> hm3 = new HashMap<String, Integer>(hm2);
        System.out.println("\n3. HashMap copied from hm2:");
        System.out.println(hm3);

        HashMap<String, Integer> hm4 = new HashMap<String, Integer>(10, 0.75f);
        hm4.put("P", 100);
        hm4.put("Q", 200);
        hm4.put("R", 300);
        System.out.println("\n4. HashMap with capacity 10 and load factor 0.75:");
        System.out.println(hm4);

        hm2.put("New", 40);
        System.out.println("\nAfter modifying hm2:");
        System.out.println("hm2: " + hm2);
        System.out.println("hm3 (copy of original hm2): " + hm3);

        System.out.println("\nSizes of HashMaps:");
        System.out.println("hm1 size: " + hm1.size());
        System.out.println("hm2 size: " + hm2.size());
        System.out.println("hm3 size: " + hm3.size());
        System.out.println("hm4 size: " + hm4.size());
    }
}