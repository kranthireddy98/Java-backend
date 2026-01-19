package com.concurrency;

public class VisibilityDemo {
    static volatile boolean running = true;

    public static void main(String[] args) {


        Thread worker = new Thread(() ->{
            int i = 0;
            while (running){
                //System.out.println("running : " + i);
                i++;
            }
            //System.out.println("Stopped");
        });

        worker.start();

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        running=false; // main thread updates
        System.out.println("main Completed");
    }
}
