import java.util.*;

class DManager {
    private static DManager instance;
    
    private List<String> items;
    
    private DManager() {
        items = new ArrayList<>();
    }
    
    public static synchronized DManager getInstance() {
        if (instance == null) {
            instance = new DManager();
        }
        return instance;
    }
    
    public synchronized void addItem(String item) {
        items.add(item);
    }
    
    public synchronized void removeItem(String item) {
        items.remove(item);
    }
    
    public synchronized List<String> getList() {
        return new ArrayList<>(items);
    }
}

public class Task16 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DManager manager = DManager.getInstance();
        
        System.out.println("Enter items (type 'Done' to finish):");
        String input;
        while (!(input = scanner.nextLine()).equals("Done")) {
            manager.addItem(input);
        }
        
        System.out.println("\nEnter item to remove:");
        String itemToRemove = scanner.nextLine();
        manager.removeItem(itemToRemove);
        
        System.out.println("\nFinal List:");
        List<String> finalList = manager.getList();
        for (String item : finalList) {
            System.out.println(item);
        }
        
        scanner.close();
    }
}