public class task17 {
    public static void main(String[] args) {
        int[] array = {64, 34, 25, 12, 22, 11, 90};
        
        System.out.println("Original array:");
        printArray(array);
        
        quickSort(array, 0, array.length - 1);
        
        System.out.println("\nSorted array:");
        printArray(array);
    }
    
    static void quickSort(int[] array, int low, int high) {
        if (low < high) {
            int pi = partition(array, low, high);
            
            System.out.println("\nAfter partitioning around pivot:");
            printArray(array);
            
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }
    
    static int partition(int[] array, int low, int high) {
        int pivot = array[high];
        
        int i = (low - 1);
        
        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;    
                
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        
        return i + 1;
    }
    
    static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
}