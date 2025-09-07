public class task19 {
    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("Code executed in a new thread via Runnable.");
        }
    }

    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Code executed in a new thread via Thread extension.");
        }
    }

    public static void main(String[] args) {
        MyRunnable runnableInstance = new MyRunnable();
        MyThread threadInstance = new MyThread();

        Thread t1 = new Thread(runnableInstance);

        t1.start(); // For MyRunnable
        threadInstance.start(); // For MyThread
    }
}