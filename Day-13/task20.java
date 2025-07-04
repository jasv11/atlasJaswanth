public class task20 {
    class Node {
        int data;
        Node next;
        
        Node(int data) {
            this.data = data;
            next = null;
        }
    }
    
    Node head = null;
    Node tail = null;
    
    public void addNode(int data) {
        Node newNode = new Node(data);
        
        if(head == null) {
            head = newNode;
            tail = newNode;
            tail.next = head;
        }
        else {
            tail.next = newNode;
            tail = newNode;
            tail.next = head;
        }
    }
    
    public void display() {
        Node current = head;
        
        if(head == null) {
            System.out.println("List is empty");
            return;
        }
        
        System.out.println("Nodes of circular linked list: ");
        do {
            System.out.print(current.data + " -> ");
            current = current.next;
        } while(current != head);
        System.out.println(head.data); 
    }
    
    public static void main(String[] args) {
        task20 cList = new task20();
        
       
        cList.addNode(10);
        cList.addNode(20);
        cList.addNode(30);
        cList.addNode(40);
        cList.addNode(50);
        
        
        cList.display();
        
       
        System.out.println("\nVerifying circular nature:");
        Node temp = cList.head;
        for(int i = 0; i < 7; i++) {
            System.out.print(temp.data + " -> ");
            temp = temp.next;
        }
        System.out.println("...");
    }
}