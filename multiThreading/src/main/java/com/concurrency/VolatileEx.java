package com.concurrency;

class MyTask implements Runnable{
    volatile boolean running = true;
    @Override
    public void run() {
        int i = 0;
        while (running){
            System.out.println("Current Thread " + Thread.currentThread().getName() + " Count: " + i);
            String currentThread = Thread.currentThread().getName();
            if(currentThread.equals("killer") && i == 20){
                running = false;
            }
            i++;
        }
    }
}

public class VolatileEx {
    public static void main(String[] args) {
        Runnable myTask = new MyTask();

        Thread t1 = new Thread(myTask);
        t1.setName("killer");
        Thread t2 = new Thread(myTask);
        t2.start();
        t1.start();

    }

}
