public class task04<T> {
    // Generic Node class
    private class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private int size;

    // Constructor
    public task04() {
        head = null;
        size = 0;
    }

    // Add element at the end of the list
    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        size++;

        if (head == null) {
            head = newNode;
            return;
        }

        Node<T> current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;
    }

    // Remove node by value
    public boolean remove(T data) {
        if (head == null) {
            return false;
        }

        if (head.data.equals(data)) {
            head = head.next;
            size--;
            return true;
        }

        Node<T> current = head;
        while (current.next != null && !current.next.data.equals(data)) {
            current = current.next;
        }

        if (current.next != null) {
            current.next = current.next.next;
            size--;
            return true;
        }
        return false;
    }

    // Get element at specific index
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    // Display all elements
    public void display() {
        if (head == null) {
            System.out.println("List is empty");
            return;
        }

        Node<T> current = head;
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("NULL");
    }

    // Get size of the list
    public int size() {
        return size;
    }

    public static void main(String[] args) {
        // Example with Integer type
        System.out.println("Integer LinkedList Example:");
        task04<Integer> intList = new task04<>();
        
        // Adding elements
        intList.addLast(1);
        intList.addLast(2);
        intList.addLast(3);
        
        System.out.println("After adding elements:");
        intList.display();
        System.out.println("Size: " + intList.size());

        // Removing element
        intList.remove(2);
        System.out.println("\nAfter removing 2:");
        intList.display();
        System.out.println("Size: " + intList.size());

        // Getting element by index
        try {
            System.out.println("\nElement at index 1: " + intList.get(1));
            // This will throw IndexOutOfBoundsException
            System.out.println("Element at index 5: " + intList.get(5));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Example with String type
        System.out.println("\nString LinkedList Example:");
        task04<String> stringList = new task04<>();
        
        // Adding elements
        stringList.addLast("Hello");
        stringList.addLast("World");
        stringList.addLast("Java");
        
        System.out.println("After adding elements:");
        stringList.display();
        System.out.println("Size: " + stringList.size());

        // Removing element
        stringList.remove("World");
        System.out.println("\nAfter removing 'World':");
        stringList.display();
        System.out.println("Size: " + stringList.size());
    }
}