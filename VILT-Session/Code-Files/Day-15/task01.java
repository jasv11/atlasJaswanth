public class task01 {
    static class Node {
        int data;       
        Node left;     
        Node right;    

        Node(int data) {
            this.data = data;
            left = null;    
            right = null;   
        }
    }

    public static void main(String[] args) {
       
        Node root = new Node(10);
        Node node1 = new Node(20);
        Node node2 = new Node(30);

        root.left = node1;   
        root.right = node2;

        System.out.println("Root node: " + root.data);
        System.out.println("Left child: " + root.left.data);
        System.out.println("Right child: " + root.right.data);
    }
}