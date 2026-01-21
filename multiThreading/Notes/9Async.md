### Callable, Future & CompletableFuture

Threads run code

Future model results.

#### Why Runnable is not Enough
`Runnable` Limitation
```java
Runnable task = () -> {
    return 42; //  not allowed
};
```
Problems:
* No return value
* No checked exception
* Fire-and-forget only

#### `callable` -- Runnable with a Result
`Callable<T>` represents a task that returns a value and can throw checked exceptions.

```java
Callable<Integer> task = () -> {
    Thread.sleep(1000);
    return 42;
};
```
* Returns value
* Can throw exception

#### `Future` A Handle to the result

What is Future?

A Future represents a result that will be available later.

Think:
* IOU
* Promise
* Receipt

Submitting Callable
```java
ExecutorService executor = Executors.newFixedThreadPool(1);

Future<Integer> future = executor.submit(() -> {
    Thread.sleep(1000);
    return 42;
});
```
#### Blocking Nature of `Future.get()` 
```java
Integer result = future.get();//Blocks
```

What Happens Internally
* Calling thread goes into Waiting
* Thread pool thread keeps working
* Caller thread wasted

This is the biggest drawback of Future

#### Failing Code #1 -- Accidental Blocking

```java
    import java.util.concurrent.Future;

Future<Integer> future = executor.submit(task);

//immediately blocking
Integer value =future.get();
```
Why this is bad
* You gain nothing over synchronous code
* Thread starvation under load
* Latency increases

#### Checking Status without Blocking
```java
if(future.isDone){
    System.out.println(future.get());
        }
```
Polling is Ugly and error-prone.

#### Exception Handling with Future

```java
try {
    future.get();
} catch (ExecutionException e) {
    Throwable cause = e.getCause(); // real exception
}
```
Future wraps exceptions inside ExecutionException

#### The Core Problem With Future

| Problem          | Reason                                 |
| ---------------- | -------------------------------------- |
| Blocking         | get() blocks                           |
| No chaining      | No thenX                               |
| Poor readability | Callback hell avoided but flow unclear |
| No composition   | Can’t combine futures                  |

This led to CompletableFuture

#### `CompletableFuture` Modern Async Model

`CompletableFuture` represents a non-blocking, composable asynchronous computation.

It supports:
* Callbacks
* pipelines
* Error handling
* Composition

#### Creating a CompletableFuture

Asynchronous Task

```java
import java.util.concurrent.CompletableFuture;

CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() ->{
    return 42;
});
```
* Runs asynchronously
* no blocking
* Uses ForkJoinPool by default

#### Consuming Result (Non-Blocking)
```java
cf.thenAccept(result -> {
        System.out.println(result);
});
```
Caller thread is NOT blocked

#### Transforming Results `thenApply`
```java
CompletableFuture<String> cf =
    CompletableFuture
        .supplyAsync(() -> 42)
        .thenApply(n -> "Value is " + n);
```
* Functional pipeline
* Clean flow

#### thenApply vs thenAccept vs thenRun

| Method     | Input  | Output     |
| ---------- | ------ | ---------- |
| thenApply  | result | new result |
| thenAccept | result | void       |
| thenRun    | none   | void       |

#### Failing Code #2 -- wrong mental model
```java
int value = CompletableFuture
                .supplyAsync(() -> 42)
                .thenApply(x -> x * 2)
                .get(); //  blocking again

```
async benefits destroyed

#### Proper Async Flow

```java
CompletableFuture
    .supplyAsync(() -> 42)
    .thenApply(x -> x * 2)
    .thenAccept(System.out::println);
```
* Fully non-blocking
* Callback-driven

#### Exception Handling
Using exceptionally()

```java
import java.util.concurrent.CompletableFuture;

CompletableFuture.supplyAsync(() ->{
    throw new RuntimeException("Boom");
        })
                .exceptionally(ex ->{
                    System.out.println(ex.getMessage());
                    return -1;
        })
```
* Clean
* Localized

#### Combining Async Tasks
thenCompose (Flattening)
```java
CompletableFuture
    .supplyAsync(() -> userId)
    .thenCompose(id -> fetchUserAsync(id))
    .thenAccept(user -> System.out.println(user));
```
thenCompose is for dependent async tasks.

thenCombine (PARALLEL)
```java
CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> 10);
CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> 20);

f1.thenCombine(f2, (a, b) -> a + b)
  .thenAccept(System.out::println);
```
* Runs in parallel
* Combines results

#### Waiting for Multiple Tasks

CompletableFuture.allOf(f1,f2).join();

Join():
* Like get()
* but unchecked exception

#### Threading Model
By default:
* uses ForkjoinPool.commonPool
* CPU-bound friendly
* NOT ideal for blocking IO

you can pass your own executor:
```java
CompletableFuture.supplyAsync(task, executor);
```

#### Mental Model
Future = pull model

CompletableFuture = push model

