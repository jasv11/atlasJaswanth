import java.util.ArrayList;
import java.util.List;


public class task17 {
    
    private static task17 instance;
    
    private List<String> itemList;
    
    private task17() {
        this.itemList = new ArrayList<>();
        System.out.println("task17 singleton instance created");
    }
    
    public static synchronized task17 getInstance() {
        if (instance == null) {
            instance = new task17();
            System.out.println("New task17 instance created");
        } else {
            System.out.println("Returning existing task17 instance");
        }
        return instance;
    }
    
    public synchronized void addItem(String item) {
        if (item != null && !item.trim().isEmpty()) {
            itemList.add(item);
            System.out.println("Item added: " + item);
        } else {
            throw new IllegalArgumentException("Item cannot be null or empty");
        }
    }
    
    
    public synchronized boolean removeItem(String item) {
        if (item != null) {
            boolean removed = itemList.remove(item);
            if (removed) {
                System.out.println("Item removed: " + item);
            } else {
                System.out.println("Item not found: " + item);
            }
            return removed;
        } else {
            throw new IllegalArgumentException("Item cannot be null");
        }
    }
    
    
    public synchronized List<String> list() {
        // Return a copy of the list to prevent external modification
        List<String> copyList = new ArrayList<>(itemList);
        System.out.println("Retrieved list with " + copyList.size() + " items");
        return copyList;
    }
    
   
    public synchronized int size() {
        return itemList.size();
    }
    
    
    public synchronized boolean isEmpty() {
        return itemList.isEmpty();
    }
    
    
    public synchronized void clear() {
        itemList.clear();
        System.out.println("All items cleared from the list");
    }
    
    
    public static void main(String[] args) {
        System.out.println("=== DManager Singleton Demo ===");
        
       
        task17 manager1 = task17.getInstance();
        task17 manager2 = task17.getInstance();
        
        System.out.println("manager1 == manager2: " + (manager1 == manager2));
        
        System.out.println("\n=== Testing List Operations ===");
        
        manager1.addItem("Item 1");
        manager1.addItem("Item 2");
        manager1.addItem("Item 3");
        
        System.out.println("Current list: " + manager1.list());
        System.out.println("List size: " + manager1.size());
        
        manager1.removeItem("Item 2");
        System.out.println("After removal: " + manager1.list());
        
        manager1.removeItem("Item 4");
        
        System.out.println("List via manager2: " + manager2.list());
        
        System.out.println("\n=== Testing Thread Safety ===");
        
        Thread thread1 = new Thread(() -> {
            task17 dm = task17.getInstance();
            for (int i = 0; i < 5; i++) {
                dm.addItem("Thread1-Item" + i);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        Thread thread2 = new Thread(() -> {
            task17 dm = task17.getInstance();
            for (int i = 0; i < 5; i++) {
                dm.addItem("Thread2-Item" + i);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Final list after threading: " + manager1.list());
        System.out.println("Final list size: " + manager1.size());
        
        manager1.clear();
        System.out.println("List after clear: " + manager1.list());
        System.out.println("Is list empty: " + manager1.isEmpty());
    }
}
