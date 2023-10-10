public class SimpleDeadLock extends Thread {
    public static Object l1 = new Object();
    public static Object l2 = new Object();
    private static int stack = 0;

    private static Boolean LOCK_MODE = false;

    public static void main(String[] args) {

        if (args.length > 0 && args[0].equals("lock")) {
            LOCK_MODE = true;
            System.out.println("LOCK MODE activated");
        }

        SimpleDeadLock simpleDeadLock = new SimpleDeadLock();
        Thread t1 = new Thread1(simpleDeadLock);
        Thread t2 = new Thread2(simpleDeadLock);
        t1.start();
        t2.start();
    }

    public synchronized void put() {
        while (stack >= 500000) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        stack++;
        System.out.println("Stack was increased by 1");
        System.out.println("Current stack: " + stack);
        notify();
    }

    public synchronized void get() {
        while (stack < 1) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        stack--;
        System.out.println("Stack was decreased by 1");
        System.out.println("Current stack: " + stack);
        notify();
    }

    private static class Thread1 extends Thread {
        SimpleDeadLock simpleDeadLock;

        Thread1(SimpleDeadLock simpleDeadLock) {
            this.simpleDeadLock = simpleDeadLock;
        }

        public void run() {

            if (LOCK_MODE) {
                synchronized (l1) {
                    System.out.println("Thread 1: Holding lock 1...");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                    }
                    System.out.println("Thread 1: Waiting for lock 2...");


                    synchronized (l2) {
                        System.out.println("Thread 2: Holding lock 1 & 2...");
                    }
                }
            } else {
                for (int i = 1; i < 1000000; i++) {
                    simpleDeadLock.put();
                }
            }
        }
    }


    private static class Thread2 extends Thread {
        SimpleDeadLock simpleDeadLock;

        Thread2(SimpleDeadLock simpleDeadLock) {
            this.simpleDeadLock = simpleDeadLock;
        }

        public void run() {

            if (LOCK_MODE) {
                synchronized (l2) {
                    System.out.println("Thread 2: Holding lock 2...");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                    }
                    System.out.println("Thread 2: Waiting for lock 1...");

                    synchronized (l1) {
                        System.out.println("Thread 2: Holding lock 2 & 1...");
                    }

                }
            } else {
                for (int i = 1; i < 1000000; i++) {
                    simpleDeadLock.get();
                }
            }
        }
    }
}