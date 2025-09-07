public class task03 {
    // Node class definition
    static class Node {
        int data;
        Node next;

        // Constructor for Node
        Node(int value) {
            data = value;
            next = null;
        }
    }

    // LinkedList class definition
    static class LinkedList {
        private Node head;

        // Constructor to initialize empty list
        public LinkedList() {
            head = null;
        }

        // Function to insert a node at the end
        public void insertAtEnd(int value) {
            Node newNode = new Node(value);
            if (head == null) {
                head = newNode; // If list is empty, make newNode the head
            } else {
                Node temp = head;
                while (temp.next != null) {
                    temp = temp.next; // Traverse to the last node
                }
                temp.next = newNode; // Link the last node to newNode
            }
        }

        // Function to delete a Node by Value
        public void deleteByValue(int value) {
            if (head == null) {
                return;
            }
            if (head.data == value) {
                head = head.next; // Move head to the next node
                return;
            }
            Node temp = head;
            while (temp.next != null && temp.next.data != value) {
                temp = temp.next; // Traverse to find the node to delete
            }
            if (temp.next != null) {
                temp.next = temp.next.next; // Unlink the node
            }
        }

        // Function to display the list
        public void display() {
            Node temp = head;
            while (temp != null) {
                System.out.print(temp.data + "->");
                temp = temp.next;
            }
            System.out.println("NULL");
        }
    }

    public static void main(String[] args) {
        LinkedList list = new LinkedList();

        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        System.out.print("Linked List: ");
        list.display();

        list.deleteByValue(20);

        System.out.print("After Deleting 20: ");
        list.display();
    }
}