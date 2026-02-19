## Redis Data types

Redis Supports these Core Data Types

| Type              | Best Used For         |
| ----------------- | --------------------- |
| String            | Caching, counters     |
| Hash              | Store objects         |
| List              | Queue                 |
| Set               | Unique elements       |
| Sorted Set (ZSET) | Leaderboard / Ranking |

### String 
* Binary-safe value (can store string, JSON, numbers)

Commands

``` 
SET user "kranthi"
GET user
INCR counter
```

* Cache full JSON response
* Store OTP
* Store access token
* Page view counters

Redis can store up to 512MB value.

### HASH 
Think of Hash like a java Map inside Redis

Instead of:
``` 
SET user:101 "{json}"
```
You can do
``` 
HSET user:101 name "
```