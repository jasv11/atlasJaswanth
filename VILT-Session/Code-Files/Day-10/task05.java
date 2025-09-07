public class task05 {
    static class Counter {
        private int count = 0;

        public void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    static class ThreadDemo extends Thread {
        Counter counter;

        ThreadDemo(Counter counter) {
            this.counter = counter;
        }

        public void run() {
            for (int i = 0; i < 10; i++) {
                counter.increment();
            }
        }
    }

    public static void main(String[] args) {
        Counter counter = new Counter();
        ThreadDemo t1 = new ThreadDemo(counter);
        ThreadDemo t2 = new ThreadDemo(counter);

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