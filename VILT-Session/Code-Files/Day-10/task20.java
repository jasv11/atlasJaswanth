public class task20 {
    public static void main(String[] args) {
        method1();
    }

    public static void method1() {
        method2();
    }

    public static void method2() {
        method3();
    }

    public static void method3() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        System.out.println("Thread Stack Trace:");
        // Iterate through the StackTraceElement array and print details
        for (StackTraceElement element : stackTrace) {
            System.out.println("  Class: " + element.getClassName() +
                               ", Method: " + element.getMethodName() +
                               ", Line: " + element.getLineNumber());
        }
    }
}