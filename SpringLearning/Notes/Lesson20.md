## Caching in Spring 

### Why Caching Exists
Databases are :
* Slow compared to memory
* Expensive under high load
* A bottleneck for read-heavy systems

Typical problems:
* Same data fetched repeatedly
* Read-heavy endpoints overloaded
* DB CPU Spikes
* High latency

caching trades memory for speed.

### What is caching in Spring?
Spring provides a cache abstraction that allows you to:
* cache method results
* Avoid repeated computation or DB calls
* Swap cache providers without changing code

Spring Cache is:
* Annotation-driven
* provider-agnostic
* Transparent to business logic

### Spring Cache Architecture
```text
Client
 ↓
Service Method
 ↓
Spring Cache Interceptor
 ↓
Cache Manager
 ↓
Cache (Redis / Caffeine / Ehcache)

```
Spring Uses AOP proxies (just like transactions).

No proxy --> no caching

### Enabling caching
```java
@EnableCaching
@Configuration
public class CacheConfig {
}
```
without this:
* All cache annotations are ignored

### The core Annotations

| Annotation    | Purpose                        |
| ------------- | ------------------------------ |
| `@Cacheable`  | Read from cache / store result |
| `@CachePut`   | Update cache                   |
| `@CacheEvict` | Remove from cache              |

### `@Cacheable`
```java
@Cacheable(cacheNames = "products", key = "#id")
public Product getProduct(Long id) {
    return repository.findById(id).orElseThrow();
}
```
Flow:
1. Check cache
2. if present -> return cached value
3. If absent -> execute method
4. Store result in cache


### Problem 1: Cache not working at All
Problematic Code
```java
public Product getProduct(Long id) {
    return repository.findById(id).orElseThrow();
}
```
Even after adding `@Cacheable`, nothing happens.

**Root Causes**
* Missing `@EnableCaching`
* Method called internally (self-invocation)
* Bean not managed by Spring

**Solution Checklist**
* Add `@EnableCaching`
* Method must be public
* Call must go through 
* Bean must be Spring-managed

### Problem 2: Cache Always Misses

Problematic Code
```java
@Cacheable(cacheNames = "products")
public Product getProduct(Long id) { }
```
Each call hits DB.

Why?

Default key = all method parameters

if:
* Object params without proper `equals/hashCode`
* Mutable parameters

Cache key becomes inconsistent.

### Solution: explicit Cache key
```java
@Cacheable(cacheNames = "products", key = "#id")
```

### `@CachePut` (Update Cache)
used when:
* You want to always execute method
* But update cache with result
```java
@CachePut(cacheNames = "products", key = "#product.id")
public Product update(Product product) {
    return repository.save(product);
}
```
Does NOT check cache before execution.

### `@CacheEvic` Invalidate Cache
```java
@CacheEvict(cacheNames = "products", key = "#id")
public void delete(Long id) {
    repository.deleteById(id);
}
```
Evict All
```java
@CacheEvict(cacheNames = "products", allEntries = true)
```

### Problem 3: stale Data Returned
Scenario
* Data updated in DB
* Cache still returns old Data

This is the cache consistency problem.


**Solution**
1. Evict cache on write
2. update cache using `@Cacheput`
3. Use TTl (time-to-live)
4. Event-based cache invalidation

There is no perfect solution, only trade-offs.

### Redis as Cache Provider
Redis is:
* In memory
* Distributed
* fast
* Production-proven

Spring Boot auto-configures Redis if:
* Redis dependency present
* Redis connection available

### Redis cache Configuration
```text
spring.cache.type=redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
```
Spring Boot automatically creates:
* redisCacheManager
* Redis-backend caches

### Serialization
Problem 4: Redis Serialization Errors
```text
SerializationException
```
Why?
* Default java serialization
* class version mismatch
* poor performance


**Solution: JSON Serialization**
```text
@Bean
public RedisCacheConfiguration cacheConfig() {
    return RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer())
        );
}
```
### Cache TTL 
Without TTL:
* cache grows forever
* Memory leaks
```text
RedisCacheConfiguration.defaultCacheConfig()
    .entryTtl(Duration.ofMinutes(10));
```
TTL must match:
* business freshness needs
* DB update frequency

### Problem 5: Caching null values

By default:
* null results may be cached
* causes permanent cache misses

**Solution**
```text
@Cacheable(cacheNames = "products", unless = "#result == null")
```

### When not to use caching
* Frequently changing data
* critical real-time data
* Write-heavy systems
* very small datasets

### Common Production Cache Patterns
* Read-through cache
* Cache-aside pattern
* Write-through
* Event-driven invalidation

Spring cache uses cache-aside by default


### Internal mechanics
* Spring created cache proxy
* Intercepts method calls
* Computes cache ke
* Delegates to Cache manager
* Serializes/deserializes value

Same proxy limitations as:
* @Transactional
* @Async

### Mental modal

Cache correctness is a business decision, not a technical one.

if data can be stale -- cache

if data can be fresh --> don't cache


