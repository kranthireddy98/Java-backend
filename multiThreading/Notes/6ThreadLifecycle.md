### Thread Lifecycle, Blocking, Waiting & Coordination

#### Thread Lifecycle

![img_17.png](img_17.png)

Thread States 

| State         | Meaning                    |
| ------------- | -------------------------- |
| NEW           | Created, not started       |
| RUNNABLE      | Ready or running on CPU    |
| BLOCKED       | Waiting for a monitor lock |
| WAITING       | Waiting indefinitely       |
| TIMED_WAITING | Waiting with timeout       |
| TERMINATED    | Finished execution         |

#### Blocking Vs Waiting 

**BLOCKED**
* Waiting to acquire a lock
* Caused by synchronized
```java
synchronized (lock){
    //
        }
```