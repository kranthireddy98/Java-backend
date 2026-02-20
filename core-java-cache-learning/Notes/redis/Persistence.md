### What is RDB?

Redis supports persistence using;

RDB (Redis Database file)

it creates a file called:
`` 
dump.rdb
``
### How RDB works
Imagine Redis memory;
``` 
Key A
Key B
Key C
```
At some configured interval

Redis creates a snapshot copy of entire dataset and saves to disk.

Snapshot Flow

``` 
Client Writes
      │
      ▼
   Redis Memory
      │
      ▼
Fork child process
      │
      ▼
Child writes memory snapshot → dump.rdb
```
Important;

Redis uses fork() to avoid blocking main thread

### RDB Configuration
Inside redis.conf:

``` 
save 900 1
save 300 10
save 60 10000
```
Meaning:
* save if change in 900 seconds
* save if changes in 300 seconds
* save if 10000 changes in 60 seconds

### Why fork()?
When snapshot starts:
1. Redis forks a child process
2. Child writes memory to disk
3. Parent continues serving requests

This avoids blocking:

But:

Fork is expensive if memory is huge.

### Crash Scenarios

Scenario 1: Redis crashes before snapshot

Data lost since last snapshot

Example:

``` 
Last snapshot at 10:00
Crash at 10:05
```

All writes between 10:00-10:05 lost.

Scenario 2: crash during snapshot

Old RDB file remains intact

No corruption 

Redis writes temp file first, then replaces old file.

safe mechanism

### RDB Advantages
* Very fast recovery
* Compact file
* Good for backups
* Minimal runtime overhead

### RDB Disadvantages
* Possible data los (between snapshots)
* Fork() can freeze system briefly on large memory
* Not ideal for real-time durability

### Production Use Case
RDB is good when;
* Redis used mainly as cache
* Some data loss acceptable
* Need periodic backup

Not good when:
* You need zero data loss

### Example
``` 
10:00 snapshot
10:01 SET A
10:02 SET B
10:03 SET C
10:04 crash
```
After restart;

Only data until 10:00 restored.

A,B,C lost.

### Backend Perspective
if using Redis only as cache:

RDB is usually enough.

if using Redis for:
* Sessions
* Message queues
* Critical counters

You need something stronger (AOF)

### Copy-On-Write
When fork happens:

Parent and child share memory pages.

If parent modifies memory:

Only modified pages copied

This is called:

Copy-On-Write (COW)

But if you have heavy writes during snapshot:

memory Usage spikes.

Production issue

### Trigger Manual Snapshot
* SAVE --> bad(blocking)

Better 
* BGSAVE --> background save

* LASTSAVE --> check last save




























