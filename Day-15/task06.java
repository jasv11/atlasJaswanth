public class task06 {
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

        void insert(int data) {
            if(root == null) {
                root = new Node(data);
                System.out.println("Added: " + data);
                return;
            }

            Node current = root;
            while(true) {
                if(data < current.data) {
                    if(current.left == null) {
                        current.left = new Node(data);
                        System.out.println("Added: " + data);
                        break;
                    }
                    current = current.left;
                } else {
                    if(current.right == null) {
                        current.right = new Node(data);
                        System.out.println("Added: " + data);
                        break;
                    }
                    current = current.right;
                }
            }
        }

        void search(int data) {
            Node current = root;
            
            while(current != null) {
                if(current.data == data) {
                    System.out.println("Found: " + data);
                    return;
                }
                if(data < current.data) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            }
            System.out.println("Not Found: " + data);
        }
    }

    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();

        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);
        tree.insert(40);

        System.out.println("\nSearching for elements:");
        tree.search(30);  
        tree.search(90);  
    }
}