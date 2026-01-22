package com.concurrency;

public class DeadLock {
    static final Object lockA = new Object();
    static final Object lockB = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() ->{
            synchronized (lockA){
                sleep();
                synchronized (lockB){
                    System.out.println("Thread 1 acquired both");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lockB){
                sleep();
                synchronized (lockA){
                    System.out.println("Thread 2 acquired both");
                }
            }
        });

        t1.start();
        t2.start();
    }

    private static void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
