import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

interface SortingStrategy {
    void sort(List<String> items);
}

class AlphabeticalSorting implements SortingStrategy {
    @Override
    public void sort(List<String> items) {
        Collections.sort(items, String.CASE_INSENSITIVE_ORDER);
    }
}

class LengthwiseSorting implements SortingStrategy {
    @Override
    public void sort(List<String> items) {
        Collections.sort(items, (s1, s2) -> s2.length() - s1.length());
    }
}

class SortingContext {
    private List<String> items;
    private SortingStrategy strategy;

    public SortingContext() {
        this.items = new ArrayList<>();
    }

    public void setStrategyForSorting(SortingStrategy strategy) {
        this.strategy = strategy;
    }

    public void addItems(String item) {
        items.add(item);
    }

    public void performSort() {
        if (strategy == null) {
            throw new IllegalStateException("Sorting strategy not set");
        }
        strategy.sort(items);
    }

    public List<String> getList() {
        return items;
    }
}

public class Task01 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SortingContext context = new SortingContext();
        
        System.out.println("Enter strings (type 'Done' to finish):");
        String input;
        while (!(input = scanner.nextLine()).equals("Done")) {
            context.addItems(input);
        }

        System.out.println("\nAlpha sorting:");
        context.setStrategyForSorting(new AlphabeticalSorting());
        context.performSort();
        for (String item : context.getList()) {
            System.out.println(item);
        }

        System.out.println("\nLengthwise sorting:");
        context.setStrategyForSorting(new LengthwiseSorting());
        context.performSort();
        for (String item : context.getList()) {
            System.out.println(item);
        }

        scanner.close();
    }
}