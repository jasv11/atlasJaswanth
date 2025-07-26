import java.util.*;

class task01 {
    @SuppressWarnings("unchecked")  // Added to suppress the warning
    LinkedList<Entry>[] data = new LinkedList[10];

    public void put(String keyval, int value) {
        int index = Math.abs(keyval.hashCode() % data.length);

        if (data[index] == null) {
            data[index] = new LinkedList<>();
        }
        for (Entry e : data[index]) {
            if (e.keyval.equals(keyval)) {
                e.value = value;
                return;
            }
        }

        data[index].add(new Entry(keyval, value));
    }

    static class Entry {
        String keyval;
        int value;

        Entry(String k, int v) {
            keyval = k;
            value = v;
        }
    }

    public static void main(String[] args) {
        task01 hash = new task01();
        hash.put("one", 1);
        hash.put("two", 2);
        hash.put("three", 3);
        System.out.println("Hash Table implementation using chaining with LinkedLists");
    }
}