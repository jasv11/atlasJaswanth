public class task09 {
    public static void main(String[] args) {
        int[] array = {64, 34, 25, 12, 22, 11, 90};
        
        System.out.println("Original array:");
        printArray(array);
        
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;
            
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j = j - 1;
            }
            array[j + 1] = key;
            
            System.out.println("\nAfter iteration " + i + ":");
            printArray(array);
        }
        
        System.out.println("\nFinal sorted array:");
        printArray(array);
    }
    
    public static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
}