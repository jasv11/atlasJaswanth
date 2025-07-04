public class task06 {
    public static void main(String[] args) {
        int[] stack = new int[5];
        int top = -1;

        System.out.println("Adding elements to stack:");
        
        top++;
        stack[top] = 10;
        System.out.println("Added: " + stack[top]);
        
        top++;
        stack[top] = 20;
        System.out.println("Added: " + stack[top]);
        
        top++;
        stack[top] = 30;
        System.out.println("Added: " + stack[top]);

        System.out.println("\nCurrent stack:");
        for(int i = top; i >= 0; i--) {
            System.out.println("Position " + i + ": " + stack[i]);
        }

        int searchValue = 20; 
        System.out.println("\nSearching for value: " + searchValue);
        
        boolean found = false;
        for(int i = top; i >= 0; i--) {
            if(stack[i] == searchValue) {
                System.out.println("Found " + searchValue + " at position: " + i);
                System.out.println("Distance from top: " + (top - i));
                found = true;
                break;
            }
        }
        
        if(!found) {
            System.out.println(searchValue + " not found in stack");
        }

        searchValue = 20;
        System.out.println("\nSearching for value: " + searchValue);
        
        found = false;
        for(int i = top; i >= 0; i--) {
            if(stack[i] == searchValue) {
                System.out.println("Found " + searchValue + " at position: " + i);
                System.out.println("Distance from top: " + (top - i));
                found = true;
                break;
            }
        }
        
        if(!found) {
            System.out.println(searchValue + " not found in stack");
        }
    }
}