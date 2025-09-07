import java.util.Queue;
import java.util.LinkedList;

class Node {
    int data;
    Node left, right;
    
    public Node(int item) {
        data = item;
        left = right = null;
    }
}

public class task09 {
    public static void printCornerNodes(Node root) {
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
    
    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);
        root.left.left.left = new Node(8);
        root.left.left.right = new Node(9);
        root.left.right.left = new Node(10);
        root.left.right.right = new Node(11);
        root.right.left.left = new Node(12);
        root.right.left.right = new Node(13);
        root.right.right.left = new Node(14);
        root.right.right.right = new Node(15);
        
        System.out.println("Corner nodes at each level:");
        printCornerNodes(root);
    }
}