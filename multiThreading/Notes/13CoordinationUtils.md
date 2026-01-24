## CountDownLatch, CyclicBarrier, Semaphore, Phaser

### Why these Utilities Exist
`wait/nofity` problems:
* Low-level
* Easy to misuse
* Hard to reason about
* Error-prone under scale

Java introduced higher-level coordination constructs to:
* simplify correctness
* Improve readability
* Reduce bugs

### CountDownLatch -- One - Time Gate

CountDownLatch allows one or more threads to wait a set of operations completes

* Cont goes down
* Cannot be reset
* One-time use

Real-World Analogy

Race start gate
* All runners wait
* Gate opens once
* Never closes again

```java
import java.util.concurrent.CountDownLatch;

CountDownLatch latch = new CountDownLatch(3);

new Thread(() -> {
    loadConfig();
    latch.countDown();
        }).start();

new Thread(() -> {
loadConfig();
    latch.countDown();
        }).start();

new Thread(() -> {
loadConfig();
    latch.countDown();
        }).start();

latch.await();
startServer();
```

* Clean startup coordination
* No locks
* No busy waiting

### Common Mistake with CountDownLatch
```java
latch.countDown();
latch.countDown(); // extra calls ignored
```
* No exception
* Can hide bugs

### CyclicBarrier -- Reusable Meeting point

CyclicBarrier allows a fixed number of threads to wait for each other repeatedly.

* Count goes up
* Resets automatically
* Reusable

Real-World Analogy

Team sync meeting
*  Everyone arrives
* Meeting starts
* Next meeting repeats

```java
CyclicBarrier barrier = new CyclicBarrier(3, () -> {
    System.out.println("All threads reached barrier");
});

Runnable task = () -> {
    prepare();
    barrier.await();
    execute();
};

new Thread(task).start();
new Thread(task).start();
new Thread(task).start();
```
* Phased execution
* Barrier action support

### CountDownLatch vs CyclicBarrier

| Feature         | CountDownLatch | CyclicBarrier |
| --------------- | -------------- | ------------- |
| Reusable        | ❌              | ✅             |
| Direction       | Down           | Up            |
| Dynamic parties | ❌              | ❌             |
| Barrier action  | ❌              | ✅             |

### Semaphore - Controlling Access

Semaphore controls how many threads can access a resource at a time.

Think:
* DB connections
* Rate limiting
* API throttling

Real-world Analogy

movie tickets
* Limited seats
* No seat --> wait

```java
Semaphore semaphore = new Semaphore(3);

void accessResource() throws InterruptedException {
    semaphore.acquire();
    try {
        useResource();
    } finally {
        semaphore.release();
    }
}
```
* Limits concurrency
* Prevents overload

### Fair vs Unfair Semaphore
```java
Semaphore fair = new semaphore(3,true);
```
* Fair -- FIFO
* Unfair --> Higher throughput

### Common Semaphore
```java
semaphore.acquire();
useresource()
// forgetting to release
```
* Permits leak
* System freezea

Always release in `finally`

### Phaser -- Dynamic Barrier

Why Phaser exists

CyclicBarrier limitations:
* Fixed number of threads
* No dynamic registration

Phaser supports dynamic registration and multi-phase synchronization

Real-World Analogy

Relay race
* Teams join dynamically
* multiple phases
* Teams drop out

```java
Phaser phaser = new Phaser(1); // main thread registered

phaser.register(); // worker 1
phaser.register(); // worker 2

new Thread(() -> {
    doPhase1();
    phaser.arriveAndAwaitAdvance();

    doPhase2();
    phaser.arriveAndDeregister();
}).start();
```

* Dynamic
* Multi-phase
* Highly flexible

### Choosing the right utility

| Use Case             | Utility        |
| -------------------- | -------------- |
| Startup coordination | CountDownLatch |
| Repeated sync        | CyclicBarrier  |
| Rate limiting        | Semaphore      |
| Dynamic workflows    | Phaser         |

### Failing Code #1 -- Wrong utility choice
Using CountDownLatch where reset needed

Using Semaphore where ordering required

### memory Visibility Guarantee
* Provide happens-before guarantees
* Ensure visibility
* do NOT require volatile/synchronized

###  Mental Model
Locks protect data 

Latches & barriers protect time & order


