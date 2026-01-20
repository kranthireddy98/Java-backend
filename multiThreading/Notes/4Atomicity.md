### Atomicity, Race Conditions & CAS (Compare-And-Swap)

Visibility makes data seen correctly.

Atomicity makes data updated correctly.

#### 1. What is a Race Condition?

A Race condition occurs when multiple threads access shared data concurrently and the final result depends on timing, not logic.

#### 2. Why Atomicity is harder Than Visibility
* volatile fixes visibility
* volatile does not fix atomicity

#### 3. CPU Reality: Why `count++` is Dangerous

`count++` at Machine level
```
Load count
ADD 1
STORE Count
```
Between Load and store:
* Another thread can interfere
* JVM cannot protect this without help

#### Failing Code #1 -- classic Race Condition
```java
class RaceConditionDemo {
    static int count = 0;

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(() -> increment());
        Thread t2 = new Thread(() -> increment());

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(count);
    }

    static void increment() {
        for (int i = 0; i < 10000; i++) {
            count++;
        }
    }
}

```
Expected
```20000```

Actual
```Random < 20000```

* Visibility is fine
* Atomicity is broken

#### Traditional Fix: `synchronized` 
```java
static synchronized void increment() {
    count++;
}
```
* Correct
* Blocking
* Context switching
* Poor Scalability under contention

#### Can we get Atomicity without locks?
**Yes**

**CAS -- Compare And Swap**

#### CAS (Compare and Swap) 
CAS Operation
``` 
IF(memory == expected)
    memory = newValue
Else 
    fail
```
Retry Until Success

**CAS Characteristics**
* Lock-free
* No blocking
* Hardware-level instruction
* Extremely fast under low contention

#### CAS In Java -- Atomic classes

Java exposes CAS via:
* AtomicInteger
* AtomicLong
* AtomicReference
* etc.

#### Failing Code #2 -- Fixed Using AtomicInteger
```java
import java.util.concurrent.atomic.AtomicInteger;

class AtomicCounter {
    static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(() -> increment());
        Thread t2 = new Thread(() -> increment());

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(count.get());
    }

    static void increment() {
        for (int i = 0; i < 10000; i++) {
            count.incrementAndGet();
        }
    }
}

```
* Atomic
* Non-blocking
* Scales better than synchronized

**How AtomicInteger Works internally**
```java
do {
    current = value;
    next = current + 1;
} while (!CAS(value, current, next));

```
CAS is provided by:
* CPU instruction
* JVM intrinsic
* Unsafe / VarHandles internally

#### Failing Cde #3 -- Atomic Trap

Atomic classes do NOT protect invariants

```java
import java.util.concurrent.atomic.AtomicInteger;

AtomicInteger balance = new AtomicInteger(100);

void withdraw(int amount){
    if(balance.get() >= amount){
        balance.set(balance.get() - amount);
    }
}
```
**What Can Happen**

Two threads:
* Both see balance = 100
* Both withdraw 80
* Final balance = 20

Race Condition still exists

Why This fails
* get() and set() are atomic
* the logic between them is not

**Correct Fix Using CAS Loop**
```java
void withdraw(int amount){
    while(true){
        int existing =balance.get();
        if(existing<amount) return;
        
        int updated = existing - amount;
        if(balance.compareAndSet(existing,updated)){
            return;
        }
    }
}
```
* Lock-free
* Atomic invariant preserved

#### CAS Limitations 
**ABA Problem**

Thread sees:
``` A -> B -> A ```
CAS thinks nothing changed

This is Why:
* AtomicStampedReference
* AtomicMarkableReference

exists

#### synchronized vs Atomic 

| Use Case           | Best Choice   |
| ------------------ | ------------- |
| Simple counter     | AtomicInteger |
| Multiple variables | synchronized  |
| Complex invariants | synchronized  |
| High contention    | LongAdder     |
| Low contention     | AtomicInteger |

#### Why LongAdder Exists
* AtomicInteger --> CAS retries increase under contention
* LongAdder --> Striped counters + aggregation

#### Mental Model

Atomic classes remove locks, not responsibility

You must still reason about invariants.

