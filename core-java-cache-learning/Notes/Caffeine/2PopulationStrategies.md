In Production, how you get data into the cache is just as important as how you get it out. 
if 1000 threads all ask for a missing key at the same time, you don't want 1000 database queries hitting your backend simultaneously. 
This is known as a **Cache Stamped** or **Thundering Herd** problem.

Caffeine provides three main ways to handle data population : **Manual, Synchronous Loading and Asynchronous Loading.**

#### Manual Population 
This is the simplest form, where you manually check for existence and "put" the data.

* Risk : it is prone to race conditions. Two threads might both see a "cache miss" and both compute the value independently.
* Best for: Scenarios where logic is highly dynamic or keys are unpredictable

```java
String value = cache.get(key,k-> fetchFromDatabase(1));
```

#### LoadingCache (The Workhorse)

A `LoadingCache` is a self-populating cache. you define a `cacheLoader` at build time. if a value is missing, the cache knows how to get it.

* **Benefit:** it handles coalescing. if multiple threads request the same missing key, only one thread performs the load, while the others block and wait for that result.

```java
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

LoadingCache<String, Data> cache = Caffeine.newBuilder().maximumSize(10000).build(key -> fetchDataFromExternalService(key));

Data data = cache.get("key123"); // Automatically loads if missing
```

#### AsyncLoadingCache (The performance)

In high-throughput reactive systems (like those using Vert.x, Project Rector, or spring), you cannot afford to block a thread.
`AsyncLoadingCache` returns a `CompletableFuture`.

* Benefit: The get call returns immediately. The Computation happens on an executor thread. Even if the load is slow, the calling thread stays free.
* The Magic of Caffeine: if you request an item that is currently being loaded, caffeine returns the same `CompletebleFuture` to all callers.

```java
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;

import javax.xml.crypto.Data;

AsyncLoadingCache<String, Data> asyncCache = Caffeine.newBuilder().maximumSize(10_000).buildAsync(key -> fetchDataAsync(key));

//Usage
CompletableFuture<Data> future = asyncCache.get("key123");
future.thenAccept(data -> System.out.println("Got: " + data));

```

#### Comparison Table

| Strategy	 | Threading | 	Best Use Case |
|-----------| ------------- | ------------ | 
| Manual	   | Caller Blocks	| Simple apps, low concurrency.|
|LoadingCache	| Caller Blocks (Coalesced)	|Standard backend services, DB caching.|
| AsyncLoadingCache	| Non-blocking |	Reactive/Event-loop architectures.|


## QA
1. What is Cache Coalescing in the context of Caffeine?

it's the ability to ensure that for a specific missing key, only one load operation is triggered even if multiple threads request that key simultaneously.
This prevents overloading the underlying data source.

2. When would you prefer `buildAsync` over `build`?

Use Async when working in non-blocking environments (like netty or Spring webflux) or when you want to trigger multiple parallel cache fetches without waiting for each one to finish sequentially.

3. What happens if the Cache Loader throws an exception?

In a `LoadingCache`, the get method will throw a RuntimeException. its important to handle these to prevent application crashes.