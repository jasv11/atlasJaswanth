public class task08 {
    public static void main(String[] args) {
        int[] stack = new int[5];
        int top = -1;

        System.out.println("Is stack empty initially?");
        if(top == -1) {
            System.out.println("Yes, stack is empty");
        } else {
            System.out.println("No, stack has elements");
        }

        System.out.println("\nAdding elements...");
        top++;
        stack[top] = 10;
        top++;
        stack[top] = 20;

        System.out.println("\nIs stack empty after adding elements?");
        if(top == -1) {
            System.out.println("Yes, stack is empty");
        } else {
            System.out.println("No, stack has elements");
        }
    }
}