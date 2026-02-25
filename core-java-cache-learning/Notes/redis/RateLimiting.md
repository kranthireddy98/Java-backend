### Rate Limiting Using Redis (API Protection System)

### What is Rate Limiting?

Rate limiting means:

Restrict how many requests a user/client can make in a given time window.

``` 
Max 5 requests per minute per user
```
Why?
* prevent abuse
* Prevent DDoS
* Protect DB
* Avoid brute-force attacks

we'll use Redis because:
* Extremely fast
* Atomic operations
* TTL support

### Approach 1 - Fixed Window Counter (Simple)
Idea

Count requests in a time window.

Example:

``` 
Key: rate:user:101
Value: request count
TTL: 60 seconds
```

Implementation

``` 
INCR rate:user:101
EXPIRE rate:user:101 60
```

In java logic:

``` 
Long count = redis.incr(key);

if (count == 1) {
    redis.expire(key, 60);
}

if (count > 5) {
    rejectRequest();
}
```
Problem:

Edge case:
``` 
User sends 5 requests at 59th second
Window resets
user sends 5 more immediately
```

Total = 10 requests in 2 seconds

This is called burst issue.

### Approach 2 -Sliding Window 
Instead of simple counter.

store timestamps.

Use Redis Sorted Set (ZSET).

### Logic
For each request:

1. Remove timestamps older than 60 seconds
2. Add current timestamp
3. Count number of elements

### Commands
``` 
ZADD rate:user:101<timestamp> <timestamp>
ZREMRANGEBYSCORE rate:user:101 0 <current_time -60>
ZCARD rate:user:101
```
if ZCARD > limit -> reject.

### Advantages
* Accurate 
* No burst problem
* Production-friendly

### Approach 3 - Token Bucket 

Used in real API gateways.

concept:
* Bucket has tokens
* Each request consumes 1 token
* Tokens refill at fixed rate

if no tokens -> reject

implementation Idea.

Use Redis:
* Store remaining tokens
* Store last refill time

Lua script ensures atomic update


### Why Use Lua Scripts?

Because:

If you do:
``` 
GET tokens
UPDATE tokens
```

Two concurrent requests may bypass limit.

Lua ensures:

Entire rate limiting logic runs automatically

### Real Production Example

Login endpoint;
``` 
Allow 5 login attempts per minute per Ip
```
Key:
``` 
rate:login: 192:168.1.1
```
If limit exceeded:

Return HTTP 429 Too many Requests

### Comparison

| Method         | Accuracy  | Complexity | Production Use |
| -------------- | --------- | ---------- | -------------- |
| Fixed Window   | Low       | Easy       | Small apps     |
| Sliding Window | High      | Medium     | Good           |
| Token Bucket   | Very High | Advanced   | Enterprise     |


### Spring Boot Integration Example

```java
public boolean isAllowed(string userId){
    String key = "rate:user:" + userid;
    
    long count = redisTemplate.opsForValue().increment(Key);
    
    if(count == 1){
        redisTemplate.expire(key, Suration.ofSeconds(60));
    }
    
    return count<=5;
} 
```
For Production Use Lua.

### Real-World Enhancements
* Rate limit per Ip
* Rate limit per user ID
* Rate limit per API endpoint
* Different limits for premium users

```` 
rate:101:/payment
````












































