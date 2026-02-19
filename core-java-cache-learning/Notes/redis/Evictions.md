### Key Expiration (TTL Internals)


``` 
SET  user:101 "data" EX 60
```
After 60 seconds -> key disappear

But HOW?

How Redis Handles Expiration Internally

Redis ses two strategies:
1. Lazy Expiration

Key is deleted only when accessed.

Example:
``` 
Key expired at 10:00
Nobody accessed it
It remains in memory

```
When someone runs:
`` 
GET user:101
``
Redis Checks:
* is TTL expired?
* Yes -> delete and return nil

### Active Expiration

background process runs periodically:
* Randomly samples keys
* Deletes expired ones

This prevents memory leak.

Why Not Delete Immediately?

Because:
* Millions of keys
* Checking every second = CPU overhead

Redis chooses smart balance.

### What happens when memory is full?

``` 
Redis maxmemory = 2GB
Memory usage = 2GB
New SET command arrives
```
What happens?

Depends on eviction policy

You configure in redis.conf;
``` 
maxmemory 2gb
maxmemory-policy allkeys-lru
```
There are multiple policies:

1. noeviction (Default if not set)

when memory full:
* New write -> Error

Production impact:

App starts failing

2. allkeys-lru

Removes least recently used key (any key).

Best for caching systems.

3. volatile-lru

Removes least recently used key

BUT only among keys with TTL

Keys without TTL never removed

4. allkeys-lfu

LFU = Least frequently Used

Removes keys accessed least frequently

Better than LRU for long-term patterns

5. Volatile-ttl

Remove key with shortest remaining TTL

### LRU vs LFU

* LRU 

Removes key not used recently

Good for;
Short-term caching

* LFU

Removes key used least overall

good for:

Hot keys

``` 
Key A → accessed 1 sec ago
Key B → accessed 1 hour ago
Key C → accessed 2 hours ago

```
LRU removes Key C.

``` 
Key A → accessed 500 times
Key B → accessed 3 times
Key C → accessed 1 time

```
LFU removes key C. 

### Cache Avalanche, Breakdown, Penetration

* cache Avalanche

Many Keys expire at same time -> DB overload

Solution:

Add random TTL offset.

Example:

Instead of 300 sec:

``` 
300 + random(0-60)
```

* Cache Breakdown

Hot key expires -> thousands of requests hit DB

Solution:

* Use distributed lock
* pre-warm cache

* Cache Penetration

Invalid Key repeatedly requested -> hits DB every time.

Solution:
* Cache null values
* Use Bloom filter























