package com.threading;


public class Main {
        public static void main(String[] args) throws InterruptedException {
            MyThread thread1 = new MyThread();

            //Start a new thread
            thread1.start();



            //Thread.sleep(100);
            //Main thread continues execution
            System.out.println("Main Thread");
        }

}