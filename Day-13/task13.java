import java.util.LinkedList;

public class task13 {
    public static void main(String[] args) {
        LinkedList<String> stack = new LinkedList<>();

        System.out.println("Demonstrating LIFO (Stack) operations with LinkedList\n");

        System.out.println("1. Pushing elements:");
        stack.push("First");
        displayStack(stack);
        
        stack.push("Second");
        displayStack(stack);
        
        stack.push("Third");
        displayStack(stack);
        
        stack.push("Fourth");
        displayStack(stack);
        
        stack.push("Fifth");
        displayStack(stack);

        System.out.println("\n2. Popping elements:");
        try {
            System.out.println("Popped: " + stack.pop());
            displayStack(stack);
            
            System.out.println("Popped: " + stack.pop());
            displayStack(stack);
            
            System.out.println("Popped: " + stack.pop());
            displayStack(stack);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n3. Peek operation:");
        System.out.println("Top element (peek): " + stack.peek());
        displayStack(stack);

        System.out.println("\n4. Pushing more elements:");
        stack.push("New Element 1");
        displayStack(stack);
        
        stack.push("New Element 2");
        displayStack(stack);

        System.out.println("\n5. Popping all elements:");
        while (!stack.isEmpty()) {
            System.out.println("Popped: " + stack.pop());
            displayStack(stack);
        }

        System.out.println("\n6. Trying to pop from empty stack:");
        try {
            stack.pop();
        } catch (Exception e) {
            System.out.println("Error: Cannot pop from empty stack");
        }

        System.out.println("\n7. Demonstrating with Integer type:");
        LinkedList<Integer> numberStack = new LinkedList<>();
        
        numberStack.push(10);
        numberStack.push(20);
        numberStack.push(30);
        System.out.println("After pushing numbers:");
        System.out.println(numberStack);

        System.out.println("\nPopping numbers:");
        while (!numberStack.isEmpty()) {
            System.out.println("Popped: " + numberStack.pop());
        }

        System.out.println("\n8. Additional Stack Operations:");
        stack.push("Element A");
        stack.push("Element B");
        stack.push("Element C");
        
        System.out.println("Current stack:");
        displayStack(stack);
        
        System.out.println("First element: " + stack.getFirst());
        System.out.println("Last element: " + stack.getLast());
        
        System.out.println("\n9. Clearing the stack");
        stack.clear();
        displayStack(stack);
    }

    private static void displayStack(LinkedList<?> stack) {
        if (stack.isEmpty()) {
            System.out.println("Stack is empty");
            return;
        }
        System.out.print("Stack (top to bottom): ");
        for (Object item : stack) {
            System.out.print(item + " -> ");
        }
        System.out.println("bottom");
        System.out.println("Stack size: " + stack.size());
    }
}