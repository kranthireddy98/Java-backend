### Why Do we Need Distributed Lock?
In Single JVM:
``` 
synchronized method(){}
```
Works because:
* One memory
* One Process

But in Microservices:
``` 
Service Instance 1
Service Instance 2
Service Instance 3
```

All running same code.

If 3 instances try to update same resource:

**Race condition**

### Example Problem
You are building:

Coupon system

Coupon "NEWUSER50"

Only 100 users allowed.

If 3 servers process requests simultaneously:

Without lock:
* 102 users may get coupon.

### Solution: Distributed Lock Using Redis

We use Redis because:
* Fast
* Centralized 
* Atomic operations supported

### Basic Lock Mechanism (SET NX)
Redis command;
``` 
SET lock: coupon unique_id NX EX 10
```
Meaning:
* NX -> set only if not exists
* EX 10 -> expire after 10 seconds

If lock acquired:
* returns OK

Else:
* Returns nil

### Lock Flow
``` 
1. Try SET NX
2. If success → execute critical section
3. Delete lock
```
### Pseudo Code 
```java
String lockValue = UUID.randomUUID().toString();

Boolean acquired = redis.setIfAbsent("lock:coupon", lockValue, 10 seconds);

if (acquired) {
    try {
        // critical section
    } finally {
        redis.delete("lock:coupon");
    }
}
```

### Major problem
Imagine:
1. Instance A acquires lock
2. Before finishing, it crashes
3. Lock remains forever

System stuck.

That's why:

Always use TTl (EX).

### Another Problem
Imagine:
1. Instance A gets lock
2. Takes too long
3. TTL expires
4. Instance B gets lock
5. Instance A finishes and deletes lock.

Now:

Instance B's lock deleted accidentally.

### Proper Lock release
We must ensure:

Only lock owner deletes lock.

Use Lua Script:
```Lua
if redis.call("get",KEYS[1]) == ARGV[1] then
    return redis.call("del",KEYS[1])
else
    return 0
end
```



### Redlock Algorithm 

Redlock is distributed locking across multiple Redis nodes.

Used when:
* High availability required
* Avoid single point failure

Basic idea:
* Acquire lock in majority of Redis instances
* If majority success -> lock acquired

### When NOT To use redis Lock
DO NOT use Redis lock for:

* financial transactions
* Very long-running processes
* Systems requiring strict consistency

Use:
* Database row-level locking
* Zookeeper
* etcd

### Redis Lock vs DB Lock

| Feature     | Redis Lock             | DB Lock            |
| ----------- | ---------------------- | ------------------ |
| Speed       | Very fast              | Slower             |
| Distributed | Yes                    | Limited            |
| Strict ACID | No                     | Yes                |
| Best For    | Short critical section | Strong consistency |

### Production Best practices

If Using Redis lock:

* Always use NX + EX
* Always use unique value
* Always release via Lua script
* Keep lock duration small
* Monitor lock timeouts

### Real Example
Scenario:

You are processing:

Payment webhook

You want to avoid double processing.

Flow:

``` 
Acquire lock on orderId
If acquired → process payment
Else → ignore duplicate
```
Perfect use case.

### How would you handle race conditions in distributed microservices?
* use Redis distributed lock for short critical sections
* Use NX + EX
* Release via Lua
* Consider Redlock for HA
* For strict consistency -> use DB transaction





























