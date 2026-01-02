package com.threading;

public class RunnableInterface implements Runnable{

    public void run()
    {
        System.out.println("Thread2 Interface");
    }

    public static void main(String[] args) {

        // create instance of runnable
        RunnableInterface runnable = new RunnableInterface();

        // create a new thread passing runnable as the runnable target

        Thread thread2 = new Thread(runnable);

        //start the newly created thread
        thread2.start();
    }

}
