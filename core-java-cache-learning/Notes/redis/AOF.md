### AOF (Append only File) persistence

RDB is:

periodic photo of memory

AOF is:

Continuous log of every write operation

### What is AOF?

Redis supports another persistence mechanism:

AOF = Append Only File

Instead of snapshotting memory, Redis logs every write command.

If you run;
``` 
SET name Kranthi
INCR counter
HSET user:101 age 28
```

AOF file will contain;
``` 
SET name kranthi
INCR counter
HSET user:101 age 28
```

On restart:

Redis replays commands to rebuild memory.

### Internally
``` 
Client → Redis executes command
          │
          ▼
      Append command to AOF buffer
          │
          ▼
      Flush buffer to disk (based on fsync policy)
```

### Enabling AOF

in redis.conf

``` 
appendonly yes
appendFilename "appendonly.aof"
```

### Fsync Policies 

This decides durability vs performance tradeoff.

1. appendfsync always

after every write:
* Immediately write to disk
* Safest 
* Slowest

Data loss almost zero

2. appendfsync everysec (recommended)
* sync once per second
* Good balance 
* Default in production

Data loss: up to 1 second

3. appendfsync no
* Let OS handle flushing
* Fastest 
* Risky

Data loss: unpredictable

### Crash Scenario Comparison

With RDB only;

``` 
Last snapshot: 10:00
Crash: 10:05
Lost: 5 minutes data
```

With AOF everysec:
``` 
Crash at 10:05:30
Lost: at most 1 second data
```

Huge difference.

### AOF Rewrite

Problem:

AOF grows continuously

If Redis runs for months.

File becomes huge.

Solution:

AOF Rewrite

Redis:
* Creates new compact file
* Writes current dataset as minimal commands

Example:

Instead of:
``` 
INCR counter
INCR counter
INCR counter
```
Rewrite becomes:
``` 
SET counter 3
```
Much smaller.

### Rewrite Process
Similar to RDB
``` 
Fork child
child writes optimized AOF
Parent continues serving
After completion -> replace old AOF
```

### Performance Impact
AOF is slower than RDB because:
* Every write must be logged
* Disk I/O involved

But with `everysec`, performance impact is minimal.

### RDB vs AOF Comparison

| Feature           | RDB     | AOF             |
| ----------------- | ------- | --------------- |
| Data loss risk    | Higher  | Very low        |
| File size         | Smaller | Larger          |
| Recovery speed    | Faster  | Slower          |
| Write performance | Better  | Slightly slower |
| Backup friendly   | Yes     | Not ideal       |


### Production Strategy

Most production systems use:

RDB + AOF together

Why?
* RDB for faster restart
* AOF for durability


### If Redis used for:
* Sessions
* Message queue
* Counters
* Financial data

You must enable AOF.

If using only as cache.

RDB is usually enough.








































