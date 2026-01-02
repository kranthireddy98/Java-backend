## 1. What is Process and Thread? what is the difference between them?
* **Process**
1. A process is an instance of a program with its own memory space
and system resources.
2. Processes are independent of each other. Changes in one process do not
affect other process.
* **Thread**
1. A thread is the smallest unit of process, that shares memory and resource with other threads
with the same process.
2. Threads are not independent, they share resources such as memory, files and I/O with other threads
within the same process.

## 2. What is main Thread & Daemon Thread in java?
1. Main thread is the temporary thread created by the java Virtual Machine (JVM)
which executes the main() method.
2. Daemon threads are background threads that provide supporting services.

## 3. How many ways we can implement multithreading in jav?
1. Extend Thread class
   * Override Run() method
2. Implement Runnable interface 
   * Implement Run() Method

## 4. Steps to implement multithreading using Thread class
1. Define a class that extends Thread class
2. Override run() method
3. Instantiate the subclass and call its start()
4. The run() method will execute concurrently in a separate thread.
```java
public class MyThread extends Thread{

    @Override
    public void run()
    {
        System.out.println("Second Thread");
    }
    
    
    public static void main(String[] args) throws InterruptedException {
        MyThread thread1 = new MyThread();

        //Start a new thread
        thread1.start();

        //Thread.sleep(100);
        //Main thread continues execution
        System.out.println("Main Thread");
    }

}


```

## 5. Steps to implement multithreading using Runnable interface 
1. Define a class implements Runnable
2. Give implementation for run method
3. Create Object of runnable class
4. Instantiate the Thread class by passing runnable as target and call its start()

````java

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
````

## 6. Difference between Thread class & Runnable interace?


## 7. Important methods of thread class?
1. start()
2. isAlive()
3. getState()
4. getPriority()
5. setPriority()
6. sleep()