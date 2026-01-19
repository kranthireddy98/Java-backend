### 1. What is Thread?
![img.png](img.png)
![img_1.png](img_1.png)

A Thread is:

A lightweight execution unit inside a process.

* one JVM Process
* Multiple threads inside it
* All Threads share heap memory
***
**Creating a Thread** 
(Wrong Way)
```Java
public class MyTask{
    public void run(){
        System.out.println("Running task");
    }
}
```
---> This is NOT a thread, just normal method inside a class.
***
**Correct Way #1 - Extend Thread** 
```java
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
        System.out.println("Main Thread running: " + Thread.currentThread().getName());//  Main Thread running: main
        t.start(); // not run() //Thread running: Thread-0
        
        t.run(); //Thread running: main
       
    }
}
```

**`start()` -- creates a new thread**

**`run()` -- is just a normal method**
***

**Correct Way #2 - Implement Runnable (Preferred)**

```java
package com.concurrency;

public class ImplementRunnableInterface implements Runnable {

    @Override
    public void run() {

        System.out.println("Task Executed by " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {

        System.out.println("Process Started By " + Thread.currentThread().getName());//Process Started By main
        // create instance of runnable
        ImplementRunnableInterface runnable = new ImplementRunnableInterface();

        // create a new thread passing runnable as the runnable target
        Thread thread2 = new Thread(runnable);

        //start the newly created thread
        thread2.start();//Task Executed by Thread-0

        // run() Method acts as normal method
        runnable.run();//Task Executed by main
    }

}

```
**Why Better**
* Separation of task and Thread
* Allows reuse
* Supports executions later


**`IllegalThreadStateException`**

The most significant issue is calling t.start() twice on the same thread object.

The Rule: A thread in Java can only be started once. Once a thread finishes execution or is already in the "started" state, you cannot restart it.

The Result: When the JVM reaches the second t.start(); call, it will throw an IllegalThreadStateException.
***

### 2. Thread Lifecycle 
![img_2.png](img_2.png)
![img_3.png](img_3.png)

**States**

| State         | Meaning                 |
| ------------- | ----------------------- |
| NEW           | Created but not started |
| RUNNABLE      | Ready or running        |
| BLOCKED       | Waiting for lock        |
| WAITING       | Waiting indefinitely    |
| TIMED_WAITING | Waiting with timeout    |
| TERMINATED    | Finished                |

**BLOCKED**

```java
synchronized void method(){
    //only one thread can enter
}
```
Other threads --> BLOCKED

***
### Race Condition

Problem

```java
package com.concurrency;

class Counter {
    int count = 0;

    void increment() {
        count++;
        ;
    }
}

public class RaceCondition {
    public static void main(String[] args) {
        Counter counter = new Counter();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> counter.increment()).start();
            new Thread(() -> counter.increment()).start();
            new Thread(() -> counter.increment()).start();

        }

        System.out.println(counter.count);//Random 2997/2996/3000/2897
    }
}

```
Multiple threads calling `increment()` --> Wrong result

Why?
count++ is Not Atomic:
1. Read
2. Increment
3. Write

Two Threads overlap ---> lost updates

### Synchronization
Using `synchronized`

```java
class SyncCounter{
    private int count =0;

    synchronized void increment(){
        count++;
    }
    synchronized int getCount(){
        return count;
    }
}
public class RaceCondition {
    public static void main(String[] args) {
    
        //SyncCount
        SyncCounter syncCounter = new SyncCounter();
        for(int i = 0;i<1000;i++){
            new Thread(() -> syncCounter.increment()).start();
            new Thread(() -> syncCounter.increment()).start();
            new Thread(() -> syncCounter.increment()).start();

        }

        System.out.println("SyncCount: " + syncCounter.getCount());//3000
    }
}
```
* performance Cost

How `Sychronized Works internally`
![img_4.png](img_4.png)
* Uses monitor lock
* One thread holds lock
* Others block

### Volatile -- Visibility, NOT Atomicity

```java
package com.concurrency;

class MyTask implements Runnable {
    volatile boolean running = true;

    @Override
    public void run() {
        int i = 0;
        while (running) {
            System.out.println("Current Thread " + Thread.currentThread().getName() + " Count: " + i);
            String currentThread = Thread.currentThread().getName();
            if (currentThread.equals("killer") && i == 20) {
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

```
```java
boolean running = true;

while(running){
    //do work
        }
 ```
Another Thread sets `running = false`

---> Thread may never stop.

Fix with `Volatile`
```java
volatile boolean running = true;

while(running){
    //do work
        }
```
**What Volatile Guarantees**
* Visibility

### Locks - More Control than Synchronized
```java
class ReentrantLockEx{
    int count =0;

    Lock lock = new ReentrantLock();

    void increment(){
        lock.lock();
        try {
            count++;
        }finally {
            lock.unlock();
        }

    }


}
public class RaceCondition {
    public static void main(String[] args) {
        
        ReentrantLockEx ReenCounter = new ReentrantLockEx();
        for(int i = 0;i<1000;i++){
            new Thread(() -> ReenCounter.increment()).start();
            new Thread(() -> ReenCounter.increment()).start();
            new Thread(() -> ReenCounter.increment()).start();

        }
        System.out.println("ReenCount: " + ReenCounter.count);
    }
}
```

Why Locks?
* tryLock()
* fairness
* interruptible locking
* multiple conditions

### Executors -- Thread management

Problem With Raw Threads
```java
new Thread(task).start();
```
* Too many threads
* Memory exhaustion
* Context switching hell

**Executor Framework**
```java
ExecutorService executor = Executors.newFixedThreadPool(5);

executor.submit(() -> {
    System.out.println("Handled by pool");
});

executor.shutdown();

```
![img_5.png](img_5.png)
![img_6.png](img_6.png)

Types of Thread Pools

| Pool      | Use Case             |
| --------- | -------------------- |
| Fixed     | CPU bound            |
| Cached    | Short async tasks    |
| Single    | Sequential tasks     |
| Scheduled | Delays, cron         |
| ForkJoin  | Parallel computation |

### Callable & Future (Async Result)

Runnable 
* No return
* No Exception

**Callable**

```java
import java.util.concurrent.Callable;

Callable<Integer> task = () ->{
    return 42;
}

Future<Integer> future = executor.submit(task);
Integer result = future.get();//blocks
```

### CompletebleFuture -- Modern Async

Callable & Future -- Blocking
```java
Integer result = future.get();
```

Non-Blocking

```java
import java.util.concurrent.CompletableFuture;

CompletableFuture.supplyAsync(() -> futureFromDb())
        .thenApply(data -> process(data))
        .thenAccept(result -> System.out.println(result));
```

* Async
* Chainable
* Error handling
```java
.exceptionally(ex -> {
    log.error(ex.getMessage());
    return fallback();
});

```
### Concurrent Collections

| Normal    | Concurrent            |
| --------- | --------------------- |
| HashMap   | ConcurrentHashMap     |
| ArrayList | CopyOnWriteArrayList  |
| HashSet   | ConcurrentSkipListSet |

