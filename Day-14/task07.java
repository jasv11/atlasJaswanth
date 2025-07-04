public class task07 {
    public static void main(String[] args) {
        int[] stack = new int[5];
        int top = -1;

        top++;
        stack[top] = 10;
        
        top++;
        stack[top] = 20;
        
        top++;
        stack[top] = 30;

        System.out.println("Peeking at top element...");
        if(top >= 0) {
            System.out.println("Top element is: " + stack[top]);
        } else {
            System.out.println("Stack is empty!");
        }
    }
}