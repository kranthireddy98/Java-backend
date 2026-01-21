### Thread Interruption & Graceful Shutdown

Thread must be stoppable, cooperative, and predictable

Threads that don't respond to interruption:
* leak resources
* Block shutdown
* Break Kubernetes pods
* Cause forced `kill -9`

#### What is Thread interruption?

Interruption does not stop a thread.

It is :
* A signal
* A request
* A cooperative mechanism

Thread must check and react.

#### How interruption works internally

Each thread has:
* An interrupt flag (boolean)

When you call:
``` 
thread.interrupt()
```
JVM:
* sets interrupt flag = true
* if thread is blocked -> throws `InteruptedException`

#### Checking Interruption (Two ways)

**Method 1 -- Clears the flag**
```java
Thread.interrupted();
```
* Returns flag
* Clears it
**Method 2 -- Does NOT clear the flag**
```java
Thread.currentThread.isInterrupted();
```

#### Failing Code #1 -- ignoring interrupt 

```java
class BadWorker implements Runnable {
    public void run() {
        while (true) {
            doWork(); // infinite loop 
        }
    }
}
```
Problem:
* Thread never stops
* interrupt() useless
* JVM shutdown stuck

**Correct Pattern**
```java
class GoodWorker implements Runnable {
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            doWork();
        }
    }
}
```
* Cooperative shutdown
* Production-safe

#### InterruptedException
Wrong
```java
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // ignore 
}

```
Why this is dangerous
* interrupt flag is cleared
* Thread continues running
* Shutdown signal lost

**Correct (Re-interrupt)**
```java
catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // restore flag
    return; // or break
}
```
Always restore the interrupt status unless you are intentionally swallowing it.

#### Blocking methods that respond to interrupts

| Method  | Behavior                    |
| ------- | --------------------------- |
| sleep() | Throws InterruptedException |
| wait()  | Throws InterruptedException |
| join()  | Throws InterruptedException |

#### Failing Code #2 -- Interrupt Lost in Loop
```java
while (!Thread.currentThread().isInterrupted()) {
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        // swallowed 
    }
}
```
What Happens
* sleep throws exception
* flag cleared
* loop continues forever

**Correct**
```java
while (!Thread.currentThread().isInterrupted()) {
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
    }
}

```

#### Interrupting Waiting Threads
If thread is:
* wait()ing
* sleep()ing 
* join()ing

Calling
```java
thread.interrupt();
```
Throws `InterruptedException`

#### Interrupt vs stop() vs suspend()

NEVER USE THESE
* Thread.stop() -- unsafe, kills thread mid-lock
* Thread.suspend() -- deadlock risk
* Thread.resume() -- unsafe

They are deprecated for a reason

#### Graceful Shutdown
```java
class ServerWorker implements Runnable {
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                handleRequest();
            }
        } finally {
            cleanupResources();
        }
    }
}

```
On shutdown:
```java
workerthread.interrupt();
```
* clean exit 
* Resource released
* Pod shuts down gracefully

### Mental Model
interrupt == knock on the door, not a bullet.

The thread must open the door.

