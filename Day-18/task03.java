import java.util.Scanner;

class task03 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int number = sc.nextInt();
        
        String numStr = String.valueOf(number);
        System.out.println("Its a " + numStr.length() + " digit number");

        
        sc.close();
    }
}