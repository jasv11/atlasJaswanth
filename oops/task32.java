// Java Program for Deep Copy of 2D Array
public class task32 {
    // A simple class for illustration purposes
    static class MyClass {
        int value;

        MyClass(int value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        // Create the original 2D array with objects
        MyClass[][] originalArray = {
            {new MyClass(1), new MyClass(2)},
            {new MyClass(3), new MyClass(4)}
        };

        // Perform the deep copy
        MyClass[][] deepCopiedArray = deepCopy2DArray(originalArray);

        // Modify an element in the original array to demonstrate independence
        originalArray[0][0].value = 99;

        // Display the original and deep copied arrays
        System.out.println("Original Array:");
        print2DArray(originalArray);

        System.out.println("\nDeep Copied Array:");
        print2DArray(deepCopiedArray);
    }

    // Method to perform a deep copy of a 2D array with objects
    private static MyClass[][] deepCopy2DArray(MyClass[][] originalArray) {
        int rows = originalArray.length;
        int cols = originalArray[0].length;

        MyClass[][] copiedArray = new MyClass[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Create a new instance of the object for each element
                copiedArray[i][j] = new MyClass(originalArray[i][j].value);
            }
        }

        return copiedArray;
    }

    // Method to print a 2D array with objects
    private static void print2DArray(MyClass[][] array) {
        for (MyClass[] row : array) {
            for (MyClass element : row) {
                System.out.print(element.value + " ");
            }
            System.out.println();
        }
    }
}