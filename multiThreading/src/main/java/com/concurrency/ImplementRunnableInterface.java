package com.concurrency;

public class ImplementRunnableInterface implements Runnable{

    @Override
    public void run()
    {

        System.out.println("Task Executed by " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {

        System.out.println("Process Started By " + Thread.currentThread().getName());
        // create instance of runnable
        ImplementRunnableInterface runnable = new ImplementRunnableInterface();

        // create a new thread passing runnable as the runnable target
        Thread thread2 = new Thread(runnable);

        //start the newly created thread
        thread2.start();

        // run() Method acts as normal method
        runnable.run();

        thread2.start();
    }

}
