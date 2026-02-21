### What is caching?

Store frequently accessed data in fast storage to avoid hitting slow storage

With Redis, we reduce:
* DB load
* API latency
* Infrastructure cost

### Best architecture
``` 
Client -> App -> Redis -> databse
```
Redis sits between App and DB.

### Cache-Aside pattern

Also called: Lazy loading pattern

``` 
1. client request data
2. App checks redis
3. If found -> return
4. If not -> fetch from DB
5. Store in Redis with TTL
6. Return response
```
### #xample
```java
public User getUser   (Long id){
    User user = redis.get("User:" + id);
    
    if(user== null){
        user = userRepository.findById(id);
        redis.set("user:" + id,user,300);
    }
    
    return user;
}
```
* Pros
* Simple
* Efficient
* Most widely used

**Cons**
* Cache miss causes DB hit
* Possible stale data

### Write-Through Pattern

When updating DB:
``` 
write to DB
Write to Redis immediately
```
Flow:
``` 
Client -> App -> DB -> Update Redis
```
Pros:
* Cache always consistent
* No Stale Data

Cons:
* Extra write overhead
* Slightly slower writes

### Write-Back (Write - Behind)
Flow:

``` 
Write to Redis
Redis updates DB later asynchrounously
```
Risky in microservices unless designed properly

Rare in typical Spring Boot apps.

### Comparison

| Pattern       | Reads            | Writes        | Use Case                 |
| ------------- | ---------------- | ------------- | ------------------------ |
| Cache Aside   | Lazy             | Manual update | Most common              |
| Write Through | Immediate update | Consistent    | Financial systems        |
| Write Back    | Async update     | Risky         | High performance systems |

### Cache Breakdown (Hot key problem)
Scenario
``` 
Key "product:101" extremely popular
TTL expires
10,000 users request simultaneously
All hit DB
DB crashes
```
Solution:
* Use distributed lock
* Only one request reloads cache
* Other wait

### Cache Avalanche
many keys expire at same time.

Solution:

Add random TTL:

``` 
ttl = 300 + random(0,60);
```

### cache penetration
User requests non-existing Id repeatedly.

Every time

redis miss -> DB hit

Solution:

cache null values temporarily

### Spring boot
```java
@Cacheable(value ="users", key="#id")
public User getUser(Long id){
    retrun userRepository.findbyId(id);
}
```
```java
@CacheEvict(value="users", key ="#id")
public void updateUser(User user){
    userRepository.save(user);
}
```
Spring handles:
* Redis storage
* Serialization
* TTL (configured in config)

### TTL Strategy 
Naver cache forever:

Good TTL choices:

| Data Type    | TTL       |
| ------------ | --------- |
| User profile | 5–10 min  |
| Product list | 1–5 min   |
| Config data  | 30–60 min |
| OTP          | 1–2 min   |

### Monitoring Cache performance
Use:

``` 
INFO stats
```
look at:
* keyspace_hits
* Keyspace_misses

Hit ratio:
``` 
hits / (hits + misses)
```
Production goal:

Above 80%





































