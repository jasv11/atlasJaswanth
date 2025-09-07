public class task08 {
    public static void main(String[] args) {
        String original = "jaswanth";
        
        System.out.println("Original string: " + original);
        
        String reversed = reverseString(original);
        
        System.out.println("Reversed string: " + reversed);
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