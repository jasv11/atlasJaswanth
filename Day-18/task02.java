import java.util.Scanner;
import java.util.Stack;

class task02 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int number = sc.nextInt();
        
        Stack<Integer> digitStack = new Stack<>();
        
        while(number > 0) {
            digitStack.push(number % 10);
            number = number / 10;
        }
        
        String[] placeValues = {"Lakhs", "10 thousands", "Thousands", "Hundreds", "Ones", "Units"};
        
        int i = 0;
        while(!digitStack.isEmpty()) {
            System.out.println(placeValues[i] + " digit is " + digitStack.pop());
            i++;
        }
        
        sc.close();
    }
}