# Caffeine Cache

```
 Caffeine.newBuilder()
                    .maximumSize(10_000)//Eviction
                    .expireAfterWrite(30,TimeUnit.MINUTES)//Hard TTL
                    .refreshAfterWrite(5,TimeUnit.MINUTES)// Refresh-ahead
                    .recordStats()//Metrics
                    .executor(dbExecutor)
                    .buildAsync(userId ->
                    {
                        System.out.println("CACHE MISS -> DB CALL ASYNC Caffeine");
                        return repository.gerUserById(userId);
                    });
```
## maximumSize(10_000)
--> Cache will store at most 10,000 entries
When the cache exceeds this size:
1. Caffeine automatically evicts entries
2. Eviction is incremental, not sudden
3. JVM heap is protected

How eviction actually works
Caffeine does NOT use simple LRU.
It uses W-TinyLFU:
Combines recency + frequency
Prevents scan pollution
Keeps truly hot entries
So eviction is:
Smart
Adaptive
Production-grade



## expireAfterWrite(30, TimeUnit.MINUTES) — Hard TTL

--> An Entry is invalid 30 minutes after it was written, no matter what.
Why this exists
This protects you from:
Stale data
Forgotten invalidations
Logic bugs
📌 TTL is a safety net, not a consistency mechanism.

## refreshAfterWrite(5, TimeUnit.MINUTES) — Refresh-Ahead

--> After 5 minutes, refresh the entry in the background, but still serve the old value.

Without refresh-ahead:
TTL expiry causes cache miss
Request waits for DB
Latency spike
Possible stampede
With refresh-ahead:
No request waits
No stampede
Smooth traffic