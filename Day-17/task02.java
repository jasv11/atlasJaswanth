public class task02 {
    static class Node {
        int data;
        Node left;
        Node right;
        int height;

        Node(int data) {
            this.data = data;
            this.height = 1;
            left = right = null;
        }
    }

    static class AVLTree {
        Node root;

        int getHeight(Node node) {
            if (node == null)
                return 0;
            return node.height;
        }

        int getBalance(Node node) {
            if (node == null)
                return 0;
            return getHeight(node.left) - getHeight(node.right);
        }

        Node rightRotate(Node y) {
            Node x = y.left;
            Node T2 = x.right;

            x.right = y;
            y.left = T2;

            y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
            x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

            return x;
        }

        Node leftRotate(Node x) {
            Node y = x.right;
            Node T2 = y.left;

            y.left = x;
            x.right = T2;

            x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
            y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;

            return y;
        }

        void insert(int data) {
            root = insertNode(root, data);
        }

        Node insertNode(Node node, int data) {
            if (node == null)
                return new Node(data);

            if (data < node.data)
                node.left = insertNode(node.left, data);
            else if (data > node.data)
                node.right = insertNode(node.right, data);
            else
                return node; 

            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

            int balance = getBalance(node);

            if (balance > 1 && data < node.left.data)
                return rightRotate(node);

            if (balance < -1 && data > node.right.data)
                return leftRotate(node);

            if (balance > 1 && data > node.left.data) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }

            if (balance < -1 && data < node.right.data) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }

            return node;
        }

        void print() {
            System.out.print("AVL Tree: ");
            printInOrder(root);
            System.out.println();
        }

        void printInOrder(Node node) {
            if (node != null) {
                printInOrder(node.left);
                System.out.print(node.data + " ");
                printInOrder(node.right);
            }
        }
    }

    public static void main(String[] args) {
        AVLTree tree = new AVLTree();

        System.out.println("Inserting nodes into AVL tree...");
        tree.insert(10);
        tree.print();  
        
        tree.insert(20);
        tree.print();
        
        tree.insert(30);
        tree.print();
        
        tree.insert(40);
        tree.print();
        
        tree.insert(50);
        tree.print();
    }
}