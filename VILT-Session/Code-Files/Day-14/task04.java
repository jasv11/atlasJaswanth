public class task04 {
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            next = null;
        }
    }

    static class CircularLinkedList {
        Node head;

        CircularLinkedList() {
            head = null;
        }

        void addElement(int data) {
            Node newNode = new Node(data);

            if (head == null) {
                head = newNode;
                head.next = head;
                return;
            }

            Node current = head;
            while (current.next != head) {
                current = current.next;
            }
            current.next = newNode;
            newNode.next = head;
        }

        void deleteNode(int value) {
            if (head == null) {
                System.out.println("List is empty! Nothing to delete.");
                return;
            }

            Node current = head;
            Node prev = null;

            if (current.data == value) {
                if (current.next == head) {
                    head = null;
                    System.out.println("Deleted " + value + " (was the only node)");
                    return;
                }
                while (current.next != head) {
                    current = current.next;
                }
                current.next = head.next;
                head = head.next;
                System.out.println("Deleted " + value + " (was head node)");
                return;
            }

            do {
                prev = current;
                current = current.next;
                
                if (current.data == value) {
                    prev.next = current.next;
                    System.out.println("Deleted " + value);
                    return;
                }
            } while (current != head);

            System.out.println("Value " + value + " not found in the list!");
        }

        void traverseList() {
            if (head == null) {
                System.out.println("List is empty!");
                return;
            }

            Node current = head;
            System.out.print("Elements in circular list: ");
            
            do {
                System.out.print(current.data + " ");
                current = current.next;
            } while (current != head);
            
            System.out.println();
        }

        void showMultipleRounds(int rounds) {
            if (head == null) {
                System.out.println("List is empty!");
                return;
            }

            System.out.print("Multiple rounds: ");
            Node current = head;
            int roundCount = 0;
            
            do {
                System.out.print(current.data + " ");
                current = current.next;
                
                if (current == head) {
                    roundCount++;
                    if (roundCount < rounds) {
                        System.out.print("| ");
                    }
                }
            } while (roundCount < rounds);
            
            System.out.println();
        }
    }

    public static void main(String[] args) {
        CircularLinkedList list = new CircularLinkedList();

        System.out.println("Adding elements: 5, 10, 15, 20, 25");
        list.addElement(5);
        list.addElement(10);
        list.addElement(15);
        list.addElement(20);
        list.addElement(25);

        System.out.println("\nOriginal List:");
        list.traverseList();
        list.showMultipleRounds(2);

        System.out.println("\nDeleting 15...");
        list.deleteNode(15);
        list.traverseList();
        list.showMultipleRounds(2);

        System.out.println("\nDeleting 5...");
        list.deleteNode(5);
        list.traverseList();
        list.showMultipleRounds(2);
    }
}