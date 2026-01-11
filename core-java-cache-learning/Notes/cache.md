# Core Java Caching with SQL Server

*A complete theoretical guide from fundamentals to advanced concepts,
written alongside the `core-java-cache-learning` codebase.*

---

## Chapter 1: Introduction to Caching

Caching is a performance optimization technique that stores frequently accessed
data in a faster storage layer so that future requests can be served without
repeating expensive operations.

In backend systems, caching primarily exists to reduce:

- Database load
- Network overhead
- CPU consumption
- Latency under concurrent access

Caching is **not** about making slow queries fast.  
Caching is about **avoiding unnecessary work altogether**.

---

## Chapter 2: Baseline Backend Architecture (Without Cache)

A typical backend system without caching follows this flow:

```
Client Request
↓
Service Layer
↓
Repository Layer
↓
SQL Server
```

In this architecture:

- Every request reaches SQL Server
- SQL Server executes the same queries repeatedly
- Application performance is tightly coupled to database performance

Even when:
- Indexes exist
- Execution plans are cached
- Data pages are in memory

The database still performs **query execution** for every request.

---

## Chapter 3: The Role of SQL Server in Performance

SQL Server is optimized for:

- Data consistency
- Transaction management
- Concurrent access
- Disk and memory management

However, SQL Server is **not designed to absorb unlimited duplicate reads**.

Each query execution involves:
- CPU cycles
- Buffer pool usage
- Lock and latch management
- Query scheduling

Under high concurrency, even simple `SELECT` queries can become a bottleneck.

---

## Chapter 4: Understanding Concurrency in Backend Systems

Real backend systems rarely handle one request at a time.

In production:
- Multiple users send requests simultaneously
- Many requests ask for the same data
- Traffic arrives in bursts

Concurrency exposes problems that are invisible in single-threaded testing.

---

## Chapter 5: Parallel Execution and the Illusion of Performance

Parallel execution can make systems *appear* fast.

Example:
- One database query takes 1 second
- Five identical queries run in parallel
- Total wall-clock time ≈ 1 second

This leads to a dangerous misconception:

> “The system is fast, so it must be efficient.”

In reality:
- The database executed the same query multiple times
- CPU and memory usage multiplied
- Scalability is poor

Parallelism hides latency but **multiplies workload**.

---

## Chapter 6: Why Connection Pooling Is Not Enough

Connection pooling is often misunderstood as a performance solution.

### What Connection Pooling Does
- Reuses physical database connections
- Avoids repeated TCP handshakes
- Reduces authentication overhead

### What Connection Pooling Does Not Do
- Does not reduce the number of queries
- Does not reduce SQL Server CPU usage
- Does not prevent duplicate reads
- Does not prevent contention

Mental model:

```
Connection Pool → HOW the application connects
Cache → WHETHER the application connects
```


Connection pools improve efficiency, not scalability.

---

## Chapter 7: Measuring Performance Under Concurrency

Correct performance measurement under concurrency requires:

- Measuring total wall-clock time
- Waiting for all parallel tasks to complete

Short response time does **not** imply low resource usage.

A system can have:
- Acceptable latency
- Severe backend stress
- Poor scalability

---

## Chapter 8: The First Intuition of Caching

The natural first idea of caching is:

- Store database results in memory
- Serve subsequent requests from memory
- Avoid repeated database calls

This is commonly implemented using in-memory maps.

In single-threaded scenarios, this appears to work correctly.

---

## Chapter 9: Naive Caching and Its Limitations

Naive caching typically involves:

- Checking if data exists in memory
- Fetching from the database if it does not
- Storing the result for future use

This approach fails under concurrency.

Multiple threads can see the cache as empty simultaneously and all hit the database.

---

## Chapter 10: Race Conditions in Caching

A race condition occurs when:
- Multiple threads read and write shared data
- The outcome depends on execution timing

In naive caching:
- Cache lookup and population are separate steps
- No atomicity exists

This causes:
- Duplicate database calls
- Inconsistent cache state
- Unpredictable behavior

---

## Chapter 11: Cache Stampede Problem

Cache stampede occurs when:
- Cache is empty or expired
- Many threads request the same data simultaneously
- All threads hit the database at once

Consequences:
- Sudden database overload
- Cascading failures
- Timeouts and retries

Preventing stampede is a **core requirement** of real caches.

---

## Chapter 12: Thread Safety in Caching

Caching is inherently concurrent.

Using non-thread-safe data structures can result in:
- Data corruption
- Infinite loops
- Application crashes

Thread safety alone, however, is **not sufficient**.

---

## Chapter 13: Atomic Cache Loading

A correct cache must ensure:
- Only one thread loads data for a key
- Other threads wait or reuse the result

This is called **atomic cache population**.

Benefits:
- Exactly one database call per key
- Predictable behavior
- Stampede prevention

---

## Chapter 14: Cache Growth and Memory Risk

An unbounded cache grows indefinitely.

Problems:
- Increased heap usage
- GC pressure
- OutOfMemoryError

Caching without eviction is a **memory leak disguised as optimization**.

---

## Chapter 15: Cache Expiry and Stale Data

Cached data becomes outdated over time.

Without expiration:
- Stale data persists indefinitely
- System correctness degrades

Expiration introduces trade-offs:
- Short TTL → more DB calls
- Long TTL → stale data risk

TTL is a **business decision**, not just a technical one.

---

## Chapter 16: Cache Eviction Strategies

When a cache reaches capacity, entries must be evicted.

### LRU (Least Recently Used)
Evicts entries that have not been accessed recently.  
Fails under scan-heavy workloads.

### LFU (Least Frequently Used)
Evicts entries used least often.  
Fails due to old popularity bias.

### TinyLFU (Modern Approach)
Balances:
- Recency
- Frequency
- Memory efficiency

Used by modern caches like **Caffeine**.

---

## Chapter 17: Why Writing a Cache Is Hard

A correct cache must handle:
- Thread safety
- Atomic loading
- Eviction
- Expiry
- Memory efficiency
- Performance under contention

Most handwritten caches fail under real production load.

---

## Chapter 18: Why Libraries Like Caffeine Exist

Libraries like Caffeine:
- Implement advanced eviction (TinyLFU)
- Optimize for concurrency
- Avoid global locks
- Prevent stampede
- Handle expiry efficiently

They are the result of years of research and production tuning.

---

## Chapter 19: In-Memory Cache vs Distributed Cache

### In-Memory Cache
- Extremely fast
- JVM-local
- Lost on restart

### Distributed Cache (Redis)
- Shared across services
- Survives restarts
- Adds network latency

Real systems often use **layered caching (L1 + L2)**.

---

## Chapter 20: Caching Patterns

### Cache-Aside (Lazy Loading)
- App controls cache explicitly
- Most common pattern

### Read-Through
- App never talks to DB directly
- Cache loads data on miss

### Write-Through
- Writes go to cache first, then DB

### Write-Behind
- Writes go to cache
- DB updated asynchronously

### Refresh-Ahead
- Cache refreshes data before expiry
- Old value served during refresh

---

## Chapter 21: Consistency Challenges

### Cache Invalidation
DB updated but cache not updated.

Solutions:
- Evict on write
- Update on write
- TTL as safety net
- Event-based invalidation

### Cache Miss
Types:
- Cold miss
- Capacity miss
- Expiry miss

### Stale Data
Caused by:
- Missing eviction
- Long TTL
- Async writes

Consistency is always a **trade-off**.

---

## Chapter 22: Key Takeaways

1. Caching avoids work, not speeds it up
2. Concurrency reveals hidden failures
3. Connection pooling ≠ caching
4. Naive caches fail at scale
5. TTL and eviction are mandatory
6. Mature libraries exist for a reason

