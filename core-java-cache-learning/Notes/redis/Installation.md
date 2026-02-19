
Redis is not officially supported on windows

OptionsL:
* Use WSL
* use Docker
* Use redis Stack

Docker
``` 
docker run --name myredis -p 6379:6379 -d redis
```
Default Port = 6379

### Redis CLI 

Start CLI:
``` 
redis-cli
```
Now you're inside Redis shell.

### Basic Commands

* SET - Stores a Key
``` 
SET name "kranthi"
```
Response:
``` 
Ok
```
* GET - Retrieve Value
``` 
GET name
```
Output:
``` 
"Kranthi
```
* DEL - Delete Key
``` 
DEL name
```
* EXISTS - Check Key

``` 
EXISTS name
```
Returns :
* `1` -> exists
* `0` -> does not exist

### TTL (Time To Live) - Auto Expiration

This is what makes Redis powerful as a cache

* Set with Expiry
``` 
SET session123 "LogedInUser" EX 10 
```
EX = seconds

After 10 Seconds -> Key disappears

* Check TTL
``` 
TTL session123 
```

Returns:
* Remaining seconds
* `-1` -> no expiry
* `-2` -> key doesn't exist

Internally

When you set TTL:

Redis does NOT immediately delete expired keys.

It uses:

1. Lazy deletion
* When you try to access expired key -> it removes it.
2. Active deletion
* Background process scans and removes expired keys.

This prevents memory explosion.

### Example: Real Backend Flow

you build:
``` 
GET /user/101
```

Flow:

first request
```
Check Redis → Not found
Query DB
Store in Redis with TTL
Return response
```
Second request;
``` 
Get directly from Redis
```
This is called:
* Cache Aside pattern 

### Redis Stores
* Redis stores everything in memory as:
``` 
Key -> Redis Object -> value 
```
Even string values are stored as optimized internal structures:

Redis optimizes memory automatically based on value size.

### Important
* Never store keys without TTL in cache use cases



































