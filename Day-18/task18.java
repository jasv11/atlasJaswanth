import java.util.Queue;
import java.util.LinkedList;
class Node {
    char data;
    Node left, right;
    
    Node(char item) {
        data = item;
        left = right = null;
    }
}

public class task18 {
    static class BinaryTree {
        Node root;
        
        void printCornerNodes(Node root) {
            if (root == null)
                return;
                
            Queue<Node> queue = new LinkedList<>();
            queue.add(root);
            
            while (!queue.isEmpty()) {
                int n = queue.size();
                
                for (int i = 0; i < n; i++) {
                    Node curr = queue.poll();
                    
                    if (i == 0 || i == n-1)
                        System.out.print(curr.data + " ");
                    
                    if (curr.left != null)
                        queue.add(curr.left);
                        
                    if (curr.right != null)
                        queue.add(curr.right);
                }
                System.out.println(); 
            }
        }
    }
    
    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();
        
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
        
        System.out.println("Corner nodes at each level:");
        tree.printCornerNodes(tree.root);
    }
}