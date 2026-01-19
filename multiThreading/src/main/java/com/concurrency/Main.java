package com.concurrency;


public class Main {
        public static void main(String[] args) throws InterruptedException {
            ThreadExtends thread1 = new ThreadExtends();

            //Start a new thread
            thread1.start();



            //Thread.sleep(100);
            //Main thread continues execution
            System.out.println("Main Thread");
        }

}