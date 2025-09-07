public class task02 {
    static class Node {
        int data;
        Node left, right;

        Node(int data) {
            this.data = data;
            left = right = null;
        }
    }

    static class BinarySearchTree {
        Node root;

        BinarySearchTree() {
            root = null;
        }

        void insertFirst(int data) {
            if(root == null) {
                root = new Node(data);
                System.out.println("Inserted " + data + " as root");
            }
        }

        void insert(int data) {
            if(root == null) {
                insertFirst(data);
                return;
            }

            Node current = root;
            Node parent = null;

            while(current != null) {
                parent = current;
                if(data < current.data) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            }

            if(data < parent.data) {
                parent.left = new Node(data);
                System.out.println("Inserted " + data + " to left");
            } else {
                parent.right = new Node(data);
                System.out.println("Inserted " + data + " to right");
            }
        }
    }

    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();

        tree.insertFirst(50);

        tree.insert(30);
        tree.insert(70);
    }
}