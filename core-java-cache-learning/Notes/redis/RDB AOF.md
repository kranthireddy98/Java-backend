### RDB + AOF Together

Now we combine everything.

### Why use both?

We saw:

| RDB               | AOF                    |
| ----------------- | ---------------------- |
| Fast recovery     | Safer (less data loss) |
| Smaller file      | More durable           |
| Periodic snapshot | Continuous logging     |

If you enable both:
* AOF protects from recent data loss
* RDB speeds up restart
* You get layered durability

### Production Configuration 

In redis.conf

``` 
appendonly yes
appendfsync everysec

save 900 1
save 300 10
save 60 10000
```

Also set:
``` 
maxmemory 2gb
maxmmory-policy allkeys-lfu
```

This is typical cache + durability production setup.

### What happens on Restart?

* If AOF is enabled -> Redis loads AOF
* If no AOF -> loads RDB

Why AOF first?

Because AOF has more recent data.

### Startup Flow

``` 
Redis starts
   │
   ├─ If AOF exists → Replay AOF
   │
   └─ Else → Load RDB
```
### Real Crash Scenario

``` 
10:00 RDB snapshot
10:05 AOF logs commands
10:06 crash
```
On restart:

Redis loads AOF -> restores almost everything

RDB acts as backup.

### What if AOF is corrupted?

Redis detects corruption.

You can repair using:
``` 
redis-check-aof --fix appendonly.aof
```
Production tip:

Always keep backups.

### Performance Impact of Using Both
* slight extra disk usage
* Slight overhead during writes
* But very safe

Modern SSDs handle this easily.





| Scenario       | Recommended                           |
| -------------- | ------------------------------------- |
| Pure cache     | RDB                                   |
| Session store  | AOF everysec                          |
| Critical state | AOF + RDB                             |
| Financial data | AOF always (if extreme safety needed) |

































