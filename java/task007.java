import java.util.Scanner;

public class task007 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Id : ");
        String id = sc.nextLine();

        System.out.print("Pwd: ");
        String password = sc.nextLine();

        System.out.println("Hi");
        System.out.println("Your login id is " + id);
        System.out.println("And your pwd is " + "*".repeat(password.length()));

        sc.close();
    }
}