## Concurrency in Spring Boot

Threads, @Async, Transactions & Web Models

# Where Do threads come from in Spring boot?

Threads come from:
* Web server (Tomcat/jetty/undertow)
* ExecutorService (for async tasks)
* Scheduler pools
* ForkJinPool (CompletableFuture, parallel streams)

### Spring MVC Thread Model (Default)

**Request Flow**
1. client sends HTTP request
2. Tomcat assigns one thread
3. Controller executes on that thread
4. Response returned
5. Thread goes back to pool

One request = one thread (until you offload work)

### The First Concurrency Trap -- Blocking the request Thread
Bad Code
```java

@GetMapping("/report")
public String generateReport(){
    heavyCalculation(); // 5 sceconds
    return "done";
}
```
What happens
* Request thread blocked
* Throughput drops
* Other request wait
* Pod look slow

### Introduce `@Async`
What `@Async` Does

Runs the method in another thread managed by Spring.

```java

@Async
public void generateReportAsync(){
    heavyCalculationn();
}
```
caller returns immediately.

### `@Asnc` is proxy based
This will NOT Work
```java
@Service
public class ReportService {

    @Async
    public void asyncMethod() { }

    public void caller() {
        asyncMethod(); //  runs synchronously
    }
}
```
Why?
* self invocation
* proxy is bypassed

```java
@Service
public class CallerService {

    private final ReportService reportService;

    public void caller() {
        reportService.asyncMethod(); //  async
    }
}
```
`@Async` works only when called via Spring proxy

### Default Executor Used by `@Async`
if you do nothing
````java
simpleAsyncTaskExcutor
````
This is dangerous:
* unbounded threads
* No reuse
* Memory risk

### ALWAYS Define a Custom Executor
```java
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }
}

```
* Controlled threads
* Predictable behavior

### `@Async` + Return Types

Void (Fire & Forget)
```java

@Async
public void sendEmail(){
    
}
```
CompletableFuture

```java

import java.util.concurrent.CompletableFuture;

@Async
public CompletableFuture<String> process() {
    return CompletableFuture.completedFuture("doner");
}
```
* Composable
* Error handling
* Non-blocking chains


### `@Async` + Exception Handling
Exception Lost
```java
@Async
public void fail() {
    throw new RuntimeException("boom");
}
```
No caller sees this.

**Proper Handling**
```java
@Async
public CompletableFuture<String> failSafe() {
    try {
        risky();
        return CompletableFuture.completedFuture("ok");
    } catch (Exception e) {
        return CompletableFuture.failedFuture(e);
    }
}
```

### Transactions + Threads
Transaction are thread-bound.

```java

@Transactional
public void process(){
    asyncMethod(); // runs in another thread
}
```
Why?
* Transaction is bound to original thread
* Async thread has NO transaction

**Correct**
```java
@Async
@Transactional
public void asyncProcess() {
    // transaction applies here
}
```

Transaction annotation must be inside async method.

### Web MVC vs WebFlux
Spring MVC
* Thread-per-request
* Blocking
* Uses Servlet API

Spring WebFlux
* Event-loop model
* Non-blocking
* Uses Netty

WebFlux scales better only when IO-bound and non-blocking.

### Blocking in WebFlux = Disaster
```java
Mono.fromCallable(() -> {
    Thread.sleep(1000); //  blocks event loop
    return "data";
});
```
same starvation problem as ForkJoinPool.

### Thread Safety of Spring Beans

Singleton Beans
```java
@Service
public class CounterService {
    int count = 0; //  shared across threads
}
```
Controller & Services :
* Are Singleton
* Shared across threads

**Correct Fix**
* Make state immutable
* Use local variables
* Use thread-safe structure
* Avoid shared mutable state.

### Real Production Concurrency issues in Spring
* Blocking DB calls in `@Async`
* Using commonPool unintentionally
* Forgetting executor configuration
* Async + transactions mismatch
* Stateful singleton beans
* Thread pool exhaustion

