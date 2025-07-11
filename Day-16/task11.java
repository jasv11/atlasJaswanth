public class task11 {
    
    //sol-1
    public int calc(int n) {
        if (n == 0) return 0;
        return n + calc(n - 1);  
    }
   //sol-2
    public int calcIterative(int n) {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        return sum;
    }

    public static void main(String[] args) {
        task11 obj = new task11();
        
        int n = 5;
        System.out.println("Using recursion: " + obj.calc(n));
        System.out.println("Using iteration: " + obj.calcIterative(n));
        
        try {
            int largeNumber = 10000;
            System.out.println("Sum of large number (iterative): " + 
                             obj.calcIterative(largeNumber));
            
        } catch (StackOverflowError e) {
            System.out.println("Stack overflow occurred!");
        }
    }
}