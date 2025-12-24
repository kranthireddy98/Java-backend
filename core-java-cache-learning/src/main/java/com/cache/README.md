This module is a learning project that explores caching evolution:

1. Naive cache using HashMap
    - Fails under concurrency

2. ConcurrentHashMap
    - Thread-safe but no eviction or TTL

3. Hand-rolled TTL + eviction
    - Introduces cache stampede and complexity

4. CaffeineUserService
    - Production-grade cache with TTL, refresh-ahead, eviction, metrics
