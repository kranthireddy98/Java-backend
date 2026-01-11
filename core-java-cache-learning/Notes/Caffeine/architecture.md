
### The core components
Caffeine's architecture is split into three main logical areas:
1. **The Admission window (LRU)** : New entries enter here first. this keeps the recent data available immediately.
2. **The Main Space (Segmented LRU):** This stores the bulk of the data. it's split into "Probation" and "protected" Segments.
3. **The TinyLFU Admission policy:** This is the bouncer at the door. when the cache is full,
TinyLFU compares the frequency of the new item against the item slated for eviction.
if the new item isn't likely to be used more often, it is rejected immediately.

### Why is it "Tiny" ?

Tracking the frequency of every key in a massive system would eat up all your RAM.
Caffeine uses a Count-Min Sketch (a probabilistic data structure). it stored frequencies in 4-bit counters,
allowing it to track millions of keys with only a few megabytes of overhead.

```java

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class CacheIntroduction {
    public static void main(String[] args) {
        //1. Configure and Build the cache
        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(10_000) // Triggers W-TinyLFU
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats()
                .build();
        
        //2. put a value
        cache.put("order_123", "OrderDetails_Obj");
        
        //3. Get a value (returns null if missing)
        String value = cache.getIfPresent("order_123");

        System.out.println("value for order_123: " + value);
    }
}
```