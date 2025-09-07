public class task10 {
    static class Resource {
        private String name;

        public Resource(String name) {
            this.name = name;
        }

        synchronized void method1(Resource r) {
            System.out.println(Thread.currentThread().getName() + 
                " acquired lock on " + this.name + 
                " and trying to lock " + r.name);
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            System.out.println(Thread.currentThread().getName() + 
                " waiting to lock " + r.name);
            r.method2(this);
        }

        synchronized void method2(Resource r) {
            System.out.println(Thread.currentThread().getName() + 
                " acquired lock on " + this.name + 
                " and trying to lock " + r.name);
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            System.out.println(Thread.currentThread().getName() + 
                " waiting to lock " + r.name);
            r.method1(this);
        }
    }

    public static void main(String[] args) {
        final Resource r1 = new Resource("Resource-1");
        final Resource r2 = new Resource("Resource-2");

        Thread t1 = new Thread(() -> {
            r1.method1(r2);
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            r2.method1(r1);
        }, "Thread-2");

        t1.start();
        t2.start();

        try {
            Thread.sleep(2000);  
            System.out.println("\nDeadlock Detection:");
            System.out.println("Thread-1 State: " + t1.getState());
            System.out.println("Thread-2 State: " + t2.getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}