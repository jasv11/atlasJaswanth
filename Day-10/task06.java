public class task06 {
    static class Counter {
        private int count = 0;

        
        public synchronized void increment() {
            count++;
            System.out.println(Thread.currentThread().getName() + " incremented count to: " + count);
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
            for (int i = 0; i < 5; i++) {
                counter.increment();
                try {
                    Thread.sleep(100);  
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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