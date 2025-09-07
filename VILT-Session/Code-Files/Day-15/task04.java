public class task04 {
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
                System.out.println("Inserted root: " + data);
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
                if(data < current.data)
                    current = current.left;
                else
                    current = current.right;
            }

            if(data < parent.data) {
                parent.left = new Node(data);
                System.out.println("Inserted left: " + data);
            } else {
                parent.right = new Node(data);
                System.out.println("Inserted right: " + data);
            }
        }

        void inorder(Node node) {
            if(node != null) {
                inorder(node.left);
                System.out.print(node.data + " ");
                inorder(node.right);
            }
        }
    }

    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();

        System.out.println("Creating and inserting nodes...");
        tree.insertFirst(50);  
        tree.insert(30);       
        tree.insert(70);      

        tree.insert(20);
        tree.insert(40);

        System.out.print("\nInorder traversal: ");
        tree.inorder(tree.root);
    }
}