public class task08 {
    static class Counter {
        private static int count = 0;

        public static synchronized void increment() {
            count++;
            System.out.println(Thread.currentThread().getName() + 
                " incremented static count to: " + count);
            
            try {
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public static synchronized int getCount() {
            return count;
        }
    }

    static class ThreadDemo extends Thread {
        ThreadDemo(String name) {
            super(name);
        }

        public void run() {
            System.out.println(Thread.currentThread().getName() + " started");
            
        
            Counter c1 = new Counter();
            Counter c2 = new Counter();
            
            for (int i = 0; i < 5; i++) {
                if (i % 2 == 0) {
                    c1.increment(); 
                } else {
                    c2.increment();
                }
            }
            
            System.out.println(Thread.currentThread().getName() + " finished");
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting main thread");
        
        ThreadDemo t1 = new ThreadDemo("Thread-1");
        ThreadDemo t2 = new ThreadDemo("Thread-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final static count: " + Counter.getCount());
    }
}