public class task05 {
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
            System.out.println(stack[i]);
        }

        System.out.println("\nRemoving elements from stack:");
        
        System.out.println("Removed: " + stack[top]);
        top--;
        
        System.out.println("Removed: " + stack[top]);
        top--;

        System.out.println("\nRemaining stack:");
        for(int i = top; i >= 0; i--) {
            System.out.println(stack[i]);
        }
    }
}