class Node {
    char data;
    Node left, right;
    
    Node(char item) {
        data = item;
        left = right = null;
    }
}

public class task19 {
    Node root;
    
    void storeAlternate(Node node, char[] arr, int level, int[] index) {
        if (node == null)
            return;
            
        storeAlternate(node.left, arr, level + 1, index);
        if (level % 2 == 0 && level > 0) {
            arr[index[0]] = node.data;
            index[0]++;
        }
        storeAlternate(node.right, arr, level + 1, index);
    }
    
    void modifyTree(Node node, char[] arr, int level, int[] index) {
        if (node == null)
            return;
            
        modifyTree(node.left, arr, level + 1, index);
        if (level % 2 == 0 && level > 0) {
            node.data = arr[index[0]];
            index[0]++;
        }
        modifyTree(node.right, arr, level + 1, index);
    }
    
    void countNodesAtAlternateLevels(Node node, int level, int[] count) {
        if (node == null)
            return;
            
        countNodesAtAlternateLevels(node.left, level + 1, count);
        if (level % 2 == 0 && level > 0)
            count[0]++;
        countNodesAtAlternateLevels(node.right, level + 1, count);
    }
    
    void reverseArray(char[] arr) {
        int start = 0;
        int end = arr.length - 1;
        while (start < end) {
            char temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }
    
    void reverseAlternateLevels() {
        int[] count = new int[1];
        countNodesAtAlternateLevels(root, 0, count);
        
        char[] arr = new char[count[0]];
        int[] index = new int[1];
        
        storeAlternate(root, arr, 0, index);
        
        reverseArray(arr);
        
        index[0] = 0;
        modifyTree(root, arr, 0, index);
    }
    
    void printTree() {
        int height = getHeight(root);
        for (int i = 0; i < height; i++) {
            printGivenLevel(root, i, height);
            System.out.println();
        }
    }
    
    int getHeight(Node node) {
        if (node == null)
            return 0;
        int leftHeight = getHeight(node.left);
        int rightHeight = getHeight(node.right);
        return Math.max(leftHeight, rightHeight) + 1;
    }
    
    void printGivenLevel(Node node, int level, int height) {
        if (node == null) {
            printSpaces(height, level);
            return;
        }
        if (level == 0) {
            System.out.print(node.data);
        } else {
            printGivenLevel(node.left, level - 1, height);
            printSpaces(1, 0);
            printGivenLevel(node.right, level - 1, height);
        }
    }
    
    void printSpaces(int height, int level) {
        int spaces = (int) Math.pow(2, height - level - 1);
        for (int i = 0; i < spaces; i++) {
            System.out.print(" ");
        }
    }
    
    public static void main(String[] args) {
        task19 tree = new task19();
        
        tree.root = new Node('A');
        tree.root.left = new Node('B');
        tree.root.right = new Node('C');
        tree.root.left.left = new Node('D');
        tree.root.left.right = new Node('E');
        tree.root.right.left = new Node('F');
        tree.root.right.right = new Node('G');
        tree.root.left.left.left = new Node('H');
        tree.root.left.left.right = new Node('I');
        tree.root.left.right.left = new Node('J');
        tree.root.left.right.right = new Node('K');
        tree.root.right.left.left = new Node('L');
        tree.root.right.left.right = new Node('M');
        tree.root.right.right.left = new Node('N');
        tree.root.right.right.right = new Node('O');
        
        System.out.println("Original Tree:");
        tree.printTree();
        
        tree.reverseAlternateLevels();
        
        System.out.println("\nTree after reversing alternate levels:");
        tree.printTree();
    }
}