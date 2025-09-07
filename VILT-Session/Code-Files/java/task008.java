class Customer {
    void accept() {
        System.out.println("accept customer called");
    }

    void display() {
        System.out.println("display customer called");
    }
}

public class task008 {
    public static void main(String[] args) {
        Customer cobj = new Customer();

        cobj.accept();
        cobj.display();
    }
}