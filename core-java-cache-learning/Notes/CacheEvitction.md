# What is Cache Invalidation?
When data changes, how do we ensure cache don't serve incorrect data?

# Types of Invalidation Problems
1. Stale Data -> Cache returns outdated value.
2. Partial Invalidation -> Once cache updated, another isn't (L1 Vs 2 mismatch).
3. Race Condition -> Read happens while writes is in progress.
4. Distributed Invalidation -> Multiple JVMs, each with its own L1 cache.

# Invalidation Strategies
1. TTL-Only Invalidation
* How it Works
    * Cache entries expire after fixed time
    * No explicit invalidation
* Why its tempting
    * Very simple
    * No Write-path logic
* Why it fails
    * Stale data served until TTL expires
    * cannot guarantee correctness
    * Writes are invisible for TTL duration
-->TTL is safety net, not a Solution
2. Evict-on-Write
```
    Update DB
    ↓
    Evict cache entry
    ↓
    Next read reloads fresh data

```
* Preferred
    * Simple
    * Correct
    * No Stale data after write
    * Cache remain derived data
3. Update-on-Write (Risky)
``` 
    Update DB
    ↓
    Update cache with new value

```
* Why it looks good
    * No cache mis after write
    * Read are always hot
* Why it's dangerous
    * Multiple cache representations
    * Serialization mismatch
    * partial updates
    * Race Conditions
* Only Safe when:
    * Simple Object
    * Single Cache
    * Strong consistency required
4. Write-Through/Write-Behind
* Write-Through
```
    Write → Cache → DB
```
* Write-Behind
``` 
    Write → Cache
    Cache → DB (async)
```

* Why They're rare
    * Cache becomes part of write path
    * Failure handling is complex
    * Hard to debug
* Mostly used in:
    * Counters 
    * Metrics
    * Mon-critical data
5. Invalidation in L1+L2
* Correct Invalidation Order
``` 
    Update DB
    ↓
    Evict L1
    ↓
    Evict L2

```
* Why this order?
  * DB is source of truth
  * Eviction avoids inconsistency
  * L1 and L2 stay aligned
6. Distributed Invalidation
``` 
    App-1 → L1
    App-2 → L1
    App-3 → L1
           ↓
    Redis (L2)

```
* Problem
  * App-1 updates DB
  * App- 2 & App-3 still serve stale L1 data
* Solutions
  * Short L1 TTL  (Seconds)
  * Redis Pub/Sub
  * Event Streaming
7. Versioned Keys 
* Instead of 
``` 
    user:123

```
* Use
``` 
    user:123:v5

```
* When User Updates:
  * Increment version
  * Old keys become unreachable
* Pros
  * No Explicit eviction
  * safe under concurrency
* Cons
  * Memory usage
  * Cleanup complexity
8. Race Condition Durin Invalidation
* Classic race
``` 
    Thread A: read cache
    Thread B: update DB + evict
    Thread A: writes stale data back

```
* Solutions
  * Locking
  * Compare-and-set
  * Versioning
  * Atomic loader(Caffeine helps here)

9. Cache Invalidation vs Consistency Models

| Model                | Guarantee                    |
| -------------------- | ---------------------------- |
| Strong consistency   | Always correct, slower       |
| Eventual consistency | Eventually correct, scalable |
| Read-your-writes     | User sees own updates        |

10. Failure Scenarios
* Redis Down
  * Skip L2 eviction
  * L1 TTL handles cleanup
* DB Down
  * Write fails --> no eviction
  * Reads may serve stale data temporarily
* Network Partition
  * Accept temporary inconsistency
* Heal via TTL
11. Rules
  * DB is always the source of truth
  * Eviction is safer than update
  * TTL is not consistency
  * Distributed invalidation is expensive
  * Bound staleness, don’t eliminate it
  * Prefer simple, predictable behavior