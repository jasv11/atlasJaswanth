import java.util.Scanner;

public class task012 {
    
    //while loop
    public static void whileLoopLogin(Scanner sc) {
        String loginid = "";
        String pwd = "";

        while (!loginid.equals("jaswanth") || !pwd.equals("password123")) {
            
            System.out.println("Enter your login id:");
            loginid = sc.nextLine();
            
            System.out.println("Enter your password:");
            pwd = sc.nextLine();
            
            if (loginid.equals("jaswanth") && pwd.equals("password123")) {
                System.out.println("While Loop - Login Successful!");
                break;
            } else {
                System.out.println("Invalid credentials! Try again.");
            }
        }
    }

    //do-while loop
    public static void doWhileLoopLogin(Scanner sc) {
        String loginid = "";
        String pwd = "";
        int count = 0;

        do {
            
            System.out.println("Enter your login id:");
            loginid = sc.nextLine();
            
            System.out.println("Enter your password:");
            pwd = sc.nextLine();
            
            if (loginid.equals("jaswanth") && pwd.equals("password123")) {
                System.out.println("Do-While Loop - Login Successful!");
            } else {
                System.out.println("Invalid credentials! Try again.");
            }
            
        } while (!loginid.equals("jaswanth") || !pwd.equals("password123"));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        whileLoopLogin(sc);
        doWhileLoopLogin(sc);
        sc.close();
    }
}