## Async Processing & Scheduling in Spring

### Why Async & Scheduling Exist
The real problems:
* Long-running tasks block request threads
* APIs become slow under load
* Background jobs need timers
* Polling, cleanup, notification need automation

Spring provides:
* Async execution -> runs tasks in parallel
* Scheduling --> run tasks at fixed times

Async improves responsiveness

Scheduling improves automation

### What @Async Actually does
@Async tells Spring:

Run this method in a separate thread, not the caller thread.

````java
@Async
public void sendEmail() {
    // long-running task
}
````
caller:
```java
service.sendEmail();
```
* caller returns immediately
* Method runs in background thread

### how `@Async` works internally

Spring uses:

AOP + proxies

```text
Caller
 ↓
Spring Proxy
 ↓
TaskExecutor (Thread Pool)
 ↓
Actual Method

```
* No proxy --> No async

### Enabling Async support
```java
@EnableAsync
@Configuration
public class AsyncConfig {
}
```
without this `@async` is ignored silently

### Problem 1: `@Async` Not working
Problematic code
```java
@Service
public class MailService {

    @Async
    public void send() { }

    public void process() {
        send(); //  self-invocation
    }
}
```
Why it fails
* Method call stays inside same class
* Proxy is bypassed
* Runs synchronously

**Solution**

Move async method to another bean:
```java
@Service
public class AsyncMailService {
    @Async
    public void send() { }
}
```
inject and call it.


### Return types of `@Async`

Valid return types:
* void
* Future<T>
* CompletableFuture<T>
```java
@Async
public CompletableFuture<String> fetch() {
    return CompletableFuture.completedFuture("done");
}
```
Exception in async methods do NOT propagate to caller

### problem 2: Exceptions Lost in Async Methods
```java
@Async
public void process() {
    throw new RuntimeException("Failed");
}
```
Caller never knows

**Solution**
1. Handle internally
```java
@Async
public void process() {
    try {
        // logic
    } catch (Exception e) {
        log.error("Async failure", e);
    }
}

```
2. Use `CompletableFuture`
```java
@Async
public CompletableFuture<Void> process() {
    return CompletableFuture.failedFuture(new RuntimeException());
}
```

### thread pools 
Default Behavior

Spring Uses:
``` 
SimpleAsyncTaskExecutor
```
Problems:
* Unbounded threads
* no queue
* Can crash JVM under load

### Custom Thread Pool
```java
@Bean
public Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(50);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("async-");
    executor.initialize();
    return executor;
}
```

And reference it:
```java
@Async("taskExecutor")
public void process(){}
```

### Problem 3: Async + Transaction = Unexpected Behavior
```java
@Async
@Transactional
public void processAsync() {
    // DB operations
}
```

Why this is dangerous
* Transaction is thread-bound
* Async runs in new thread
* Transaction context may not behave as expected

**BEST Practice**
* keep transactions inside async method
* Do NOT expect parent transaction to propagate
* or redesign using events / messaging


## Scheduling in Spring (`@Scheduled`)
Scheduling runs method automatically at defined intervals.

Enable it:
```java
@EnableScheduling
@Configuration
public class SchedulerConfig { }
```

### Scheduling types
Fixed Rate
``` 
@Scheduled(fixedRate = 5000)
```
Runs every 5 seconds (start-to-start)

Fixed Delay 
```java
@Scheduled(fixedDelay = 5000)
```
Runs 5 seconds after previous finishes

Cron 
```java
@Scheduled(cron = "0 0 2 * * *")
```
Runs every day at 2 AM

### Problem 4: Scheduled Job Runs concurrently
```java
@Scheduled(fixedRate = 1000)
public void job() {
    Thread.sleep(5000);
}
```
Multiple executions overlap

**Solution*
1. Fixed delay
```java
@Scheduled(fixedDelay = 1000)
```
2. Single-thread scheduler
```java
spring.task.scheduling.pool.size=1
```

### Problem 5: Scheduled Task stops after Exception
Cause

uncaught exception kills scheduler thread

Solution
```java
@Scheduled(...)
public void job() {
    try {
        // logic
    } catch (Exception e) {
        log.error("Job failed", e);
    }
}

```

### Combining Async + Scheduling
```java
@Scheduled(cron = "0 * * * * *")
@Async
public void runJob() { }
```
use carefully;
* can spawn many threads
* Need strict pool limits

### Context propagation
By default, async threads do not inherit
* SecurityContext
* Transaction context
* MDC logging context

**Solutions:**
1. `DelegatingSecurityContextExecutor`
2. custom TaskDecorator

### Common Production mistakes
* using default executor
* Async + self-invocation
* no exception handling
* Unlimited scheduling concurrency
* Assuming async improves everything








