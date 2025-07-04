public class task07 {
    public static void main(String[] args) {
        int[] digits = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        
        System.out.println("Original array:");
        printArray(digits);
        
        reverseArray(digits);
        
        System.out.println("Reversed array:");
        printArray(digits);
    }
    
    public static void reverseArray(int[] arr) {
        int start = 0;
        int end = arr.length - 1;
        
        while (start < end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            
            start++;
            end--;
        }
    }
    
    public static void printArray(int[] arr) {
        for (int digit : arr) {
            System.out.print(digit + " ");
        }
        System.out.println();
    }
}