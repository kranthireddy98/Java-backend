package com.concurrency;

public class ReorderingDemo {
    /*
    Problem No Volatile
     static boolean initialized = false;
     */

    //Fix
    static volatile boolean initialized;
    static int value = 0;

    static void init() {
        value = 42;
        initialized = true;
    }

    static void use() {
        if (initialized) {
            // In a reordered scenario, this could print 0!
            if (value == 0) {
                System.out.println("Bug Detected: Value is 0 even though initialized is true!");
            } else {
                System.out.println("Value: " + value);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {


        // We run this in a loop because reordering is rare and non-deterministic
        for (int i = 0; i < 100_000; i++) {
            // Reset state
            initialized = false;
            value = 0;

            Thread t1 = new Thread(() -> init());
            Thread t2 = new Thread(() -> use());

            t1.start();
            t2.start();

            t1.join();
            t2.join();
        }
        System.out.println("Finished 100,000 iterations.");
    }
}
