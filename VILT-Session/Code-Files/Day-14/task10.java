public class task10 {
    static class Queue {
        int[] arr;
        int front, rear;
        int size;

        Queue(int size) {
            this.size = size;
            arr = new int[size];
            front = rear = -1;
        }

        boolean isEmpty() {
            return front == -1;
        }

        boolean isFull() {
            return (rear + 1) % size == front;
        }

        void enqueue(int data) {
            if(isFull()) {
                System.out.println("Queue is full!");
                return;
            }
            if(isEmpty()) front = 0;
            rear = (rear + 1) % size;
            arr[rear] = data;
            System.out.println("Added: " + data);
        }

        void dequeue() {
            if(isEmpty()) {
                System.out.println("Queue is empty!");
                return;
            }
            System.out.println("Removed: " + arr[front]);
            if(front == rear) {
                front = rear = -1;
            } else {
                front = (front + 1) % size;
            }
        }

        void peek() {
            if(isEmpty()) {
                System.out.println("Queue is empty!");
                return;
            }
            System.out.println("Front element: " + arr[front]);
        }

        void display() {
            if(isEmpty()) {
                System.out.println("Queue is empty!");
                return;
            }
            System.out.print("Queue: ");
            int i = front;
            do {
                System.out.print(arr[i] + " ");
                i = (i + 1) % size;
            } while(i != (rear + 1) % size);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Queue q = new Queue(3);
        
        q.enqueue(10);
        q.enqueue(20);
        q.display();
        q.peek();
        q.dequeue();
        q.display();
    }
}