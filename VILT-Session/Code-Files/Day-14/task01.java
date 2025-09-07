public class task01 {
    // Custom Node
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            next = null;
        }
    }

    static class MyList {
        Node head; 

        MyList() {
            head = null;
        }

        void addElement(int data) {
            Node newNode = new Node(data);

            if (head == null) {
                head = newNode;
                return;
            }

            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }

        void traverseList() {
            if (head == null) {
                System.out.println("List is empty!");
                return;
            }

            Node current = head;
            System.out.print("Elements in list: ");
            while (current != null) {
                System.out.print(current.data + " ");
                current = current.next;
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        MyList list = new MyList();

        System.out.println("Adding elements: 5, 10, 15, 20");
        list.addElement(5);
        list.addElement(10);
        list.addElement(15);
        list.addElement(20);

        list.traverseList();

        System.out.println("\nAdding more elements: 25, 30");
        list.addElement(25);
        list.addElement(30);

        list.traverseList();
    }
}