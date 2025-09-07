public class task17 {
    static int findMax(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    static void countSort(int[] arr, int position) {
        int[] output = new int[arr.length];
        int[] count = new int[10];  
        for (int i = 0; i < arr.length; i++) {
            int digit = (arr[i] / position) % 10;
            count[digit]++;
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = arr.length - 1; i >= 0; i--) {
            int digit = (arr[i] / position) % 10;
            output[count[digit] - 1] = arr[i];
            count[digit]--;
        }

        for (int i = 0; i < arr.length; i++) {
            arr[i] = output[i];
        }
    }

    static void radixSort(int[] arr) {
        int max = findMax(arr);

        for (int position = 1; max / position > 0; position *= 10) {
            countSort(arr, position);
            System.out.print("After sorting digit position " + position + ": ");
            printArray(arr);
        }
    }

    static void printArray(int[] arr) {
        for (int value : arr) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] arr1 = {170, 45, 75, 90, 802, 24, 2, 66};
        System.out.println("Original array 1:");
        printArray(arr1);
        System.out.println("\nSorting array 1:");
        radixSort(arr1);
        System.out.println("\nFinal sorted array 1:");
        printArray(arr1);

        System.out.println("\n------------------------");

        int[] arr2 = {432, 8, 530, 90, 88, 231, 11, 45, 677, 199};
        System.out.println("Original array 2:");
        printArray(arr2);
        System.out.println("\nSorting array 2:");
        radixSort(arr2);
        System.out.println("\nFinal sorted array 2:");
        printArray(arr2);
    }
}