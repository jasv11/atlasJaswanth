import java.util.Scanner;

public class task09 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter a name: ");
        String name = scanner.nextLine();
        
        System.out.println("Original name: " + name);
        
        String reversed = reverseString(name);
        
        System.out.println("Reversed name: " + reversed);
        
        scanner.close();
    }
    
    public static String reverseString(String str) {
        char[] charArray = str.toCharArray();
        int start = 0;
        int end = charArray.length - 1;
        
        while (start < end) {
            char temp = charArray[start];
            charArray[start] = charArray[end];
            charArray[end] = temp;
            
            start++;
            end--;
        }
        
        return new String(charArray);
    }
}