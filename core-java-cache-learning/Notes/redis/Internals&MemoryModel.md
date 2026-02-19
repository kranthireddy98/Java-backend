### Redis Execution Model (The Truth About Single Thread)

Redis executes commands in a single thread.

But that does NOT mean:

* It handles one client at a time
* It is slow

How Redis Actually Works
``` 
Clients
   │
   ▼
┌───────────────┐
│ Event Loop    │  ← Single thread executes commands
└───────────────┘
        │
        ▼
┌────────────────┐
│ In-Memory Data │
└────────────────┘
        │
        ▼
 Background Threads
 (RDB, AOF, I/O)

```

Redis uses an event-driven architecture (similar to Node.js)

### Why single Thread is Actually Fast
Imagine multi-threaded DB:

* Thread 1 -> lock
* Thread 2 -> wait
* Thread 3 -> context switch
* Lock contention
* CPU overhead

Redis avoids all that.

Because:
* No locks
* No synchronization
* No context switching
* No race conditions

Everything runs sequentially, extremely fast.

### What is the Redis Event Loop?
Redis uses:
* epoll (Linux)
* Kqueue (Mac)

Flow:
```
1. Client sends request
2. OS notifies Reedis (event ready)
3. Redis reads request
4. Executes command
5. Sends response
6. Move to next request
```
All in microseconds

### Why redis Handles 100K+ RPS
Reasons:
1. Pure memory access

RAM access is nanoseconds

Disk DB;
``` 
Disk seek -> milliseconds
```
Redis:
``` 
Memory read -> microseconds
```
2. Very Small Command Execution
``` 
GET user:101 
```
Internally:
* Lookup key in hash table
* Return pointer
* Send response

no parsing SQL

No Query planner

No disk I/O

3. Optimized Data Structures

Internally Redis uses:

| Type   | Internal Structure          |
| ------ | --------------------------- |
| String | SDS (Simple Dynamic String) |
| Hash   | Hash table / Ziplist        |
| ZSET   | Skiplist + Hash table       |


### Redis Memory Model

Redis stores everything as:

``` 
redisObject
    ├── type
    ├── encoding
    ├── reference count
    └── pointer to actual value
```
Example:
``` 
Key -> redisObject -> Actual Data
```
Even Keys are objects

### What happens When You Run SET?
``` 
SET name "kranthi"
```
Internally:
1. Key hashed
2. Inserted into dictionary (hash table)
3. Value stored as SDS
4. Optional TTL metadata stored

All O(1)

### Is Redis Always Single Threaded?

Important modern detail:

Since Redis 6:

* Network can be multi-threaded
* I/O handling can be multi-threaded
* BUT command execution remains single-threaded

### What Can Slow Down Redis?
Redis is fast unless YOU misuse it

* Storing 100MB JSON in one key
* Blocking commands
``` 
KEYS *
```
This blocks the entire server.

Better:
``` 
SCAN
```
* Long Lua Scripts

### Comparison: Redis vs Multi-threaded Db

| Feature           | Redis           | Traditional DB |
| ----------------- | --------------- | -------------- |
| Execution         | Single-threaded | Multi-threaded |
| Locking           | None            | Heavy          |
| Context switching | No              | Yes            |
| Disk I/O          | Optional        | Always         |
| Latency           | Microseconds    | Milliseconds   |



































