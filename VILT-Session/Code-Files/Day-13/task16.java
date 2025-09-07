import java.util.*;

public class task16 {
    class Node {
        int data;
        Node prev;
        Node next;
        
        Node(int data) {
            this.data = data;
        }
    }
    
    Node head, tail = null;
    
    public void addNode(int data) {
        Node newNode = new Node(data);
        
        if(head == null) {
            head = tail = newNode;
            head.prev = null;
            tail.next = null;
        }
        else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
            tail.next = null;
        }
    }
    
    public void display() {
        Node current = head;
        if(head == null) {
            System.out.println("List is empty");
            return;
        }
        System.out.println("Nodes of doubly linked list: ");
        while(current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
    }
    
    public static void main(String[] args) {
        task16 dList = new task16();
        
        dList.addNode(10);
        dList.addNode(20);
        dList.addNode(30);
        dList.addNode(40);
        dList.addNode(50);
        
        dList.display();
    }
}