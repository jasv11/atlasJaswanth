public class task03 {
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

        list.traverseList();

        list.showMultipleRounds(3);
    }
}