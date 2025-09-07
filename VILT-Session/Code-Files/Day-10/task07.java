public class task07 {
    static class Counter {
        private int count = 0;

        public void increment() {
            
            synchronized (this) {
                count++;
                System.out.println(Thread.currentThread().getName() + 
                    " is incrementing. Count is now: " + count);
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
           
            System.out.println(Thread.currentThread().getName() + 
                " finished processing increment operation.");
        }

        public int getCount() {
            return count;
        }
    }

    static class ThreadDemo extends Thread {
        Counter counter;

        ThreadDemo(Counter counter, String name) {
            super(name);
            this.counter = counter;
        }

        public void run() {
            System.out.println(Thread.currentThread().getName() + " started");
            for (int i = 0; i < 5; i++) {
                counter.increment();
            }
            System.out.println(Thread.currentThread().getName() + " finished");
        }
    }

    public static void main(String[] args) {
        Counter counter = new Counter();
        
        ThreadDemo t1 = new ThreadDemo(counter, "Thread-1");
        ThreadDemo t2 = new ThreadDemo(counter, "Thread-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final count: " + counter.getCount());
    }
}