## What is Redis?

**Redis = Remote Dictionary Server**

It is an:
* In memory data structure store
* Key-value databases
* Cache
* Message Broker
* Distributed lock manager
* Real-time analytics engine

**Redis is an in memory data platform**

### Where Does Redis sit in Architecture?

Typical Java Backend Architecture
``` 
Client → Load Balancer → Spring Boot App → Redis → Database
```
Redis Sits between:
* Your application
* Your database

Why?

Because BD is slow (disk-based)

Redis is fast(RAM-based)

### Why Redis is So Fast?

Because:

* It stores data in RAM
* It is single-threaded for commands
* No thread context switching
* Used event-driven I/O (epoll/kquue)

Think of it Like;

| Component | Speed                    |
| --------- | ------------------------ |
| Disk DB   | Slow (ms)                |
| Redis     | Very fast (microseconds) |

Redis Operations are typically < 1ms

### Single-Threaded Myth

* Uses single thread for command execution
* Uses multiple threads for:
  * Networking
  * Background persistence

Why Single thread?

Because:
* No locks
* No race conditions
* No synchronization overhead
* Predictable performance

That's why it handles 100K+ requests/sec easily.

### Redis is NOT Just String Storage

Unlike simple key-value stores, Redis supports data structures:

| Type       | Example Use  |
| ---------- | ------------ |
| String     | Cache user   |
| List       | Queue        |
| Set        | Unique users |
| Hash       | Store object |
| Sorted Set | Leaderboard  |


### Redis VS Database VS Cache

| Feature    | Redis     | MySQL/Postgres |
| ---------- | --------- | -------------- |
| Storage    | RAM       | Disk           |
| Speed      | Very fast | Slower         |
| Querying   | Limited   | Complex joins  |
| Durability | Optional  | Strong         |
| Scaling    | Easy      | Harder         |

Redis is NOT a replacement for DB

It is a performance accelerator

### Real-world Example (Spring Boot Context)

``` 
First time:
Client → App → DB → Redis → Client

Second time:
Client → App → Redis → Client

```

### Redis Internal Architecture

``` 
          ┌──────────────┐
Client →  │ Event Loop   │
          └──────────────┘
                 │
         ┌─────────────────┐
         │ In-Memory Store │
         └─────────────────┘
                 │
      ┌──────────────────────┐
      │ RDB / AOF Persistence│
      └──────────────────────┘

```
























