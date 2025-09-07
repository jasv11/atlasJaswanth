public class task03 {
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

            if(data < parent.data)
                parent.left = new Node(data);
            else
                parent.right = new Node(data);
        }

        void inorder(Node node) {
            if(node != null) {
                inorder(node.left);      
                System.out.print(node.data + " "); 
                inorder(node.right);     
            }
        }

        void printInorder() {
            System.out.print("Inorder traversal: ");
            inorder(root);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();

        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);
        tree.insert(40);

        tree.printInorder();
    }
}