package com.concurrency;

public class ThreadExtends extends Thread{

    @Override
    public void run()
    {
        System.out.println("Thread running: " + Thread.currentThread().getName());

    }


}

class Demo {
    public static void main(String[] args) {
        ThreadExtends t =new ThreadExtends();
        System.out.println("Main Thread running: " + Thread.currentThread().getName());
        t.start(); // not run()
        t.run();
        t.start();
    }
}