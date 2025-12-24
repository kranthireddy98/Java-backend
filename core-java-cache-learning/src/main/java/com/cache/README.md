# Core Java Caching Learning

This module contains a progressive exploration of caching strategies in Core Java using a real backend scenario with SQL Server.

The goal is to show how caching evolves from naive approaches to production-ready implementations using modern caching libraries.

---

## 🧠 Learning Journey

The code in this module is organized to show step-by-step evolution:

### 📍 1. Naive Cache (HashMap)
- Simple cache with `HashMap`
- Demonstrates basic memoization
- Fails under concurrency

### 📍 2. Thread-Safe Cache (ConcurrentHashMap)
- Uses `ConcurrentHashMap` with `computeIfAbsent`
- Fixes thread safety
- Still lacks eviction and expiry

### 📍 3. Hand-Rolled TTL + Eviction
- Attempts to add time-based expiry and size limits manually
- Shows common pitfalls:
    - Cache stampede
    - Incorrect eviction
    - Memory leaks

### 📍 4. Caffeine Implementation (`CaffeineUserService`)
- Production-grade cache based on Caffeine
- Supports:
    - TTL (`expireAfterWrite`)
    - Refresh ahead (`refreshAfterWrite`)
    - Size eviction (`maximumSize`)
    - Async loading
    - Metrics (`recordStats`)

---

## 🧪 How to Run

You can run experiments from a main class or test runner.

For example:

```java
public static void main(String[] args) throws Exception {
    CaffeineUserService service = new CaffeineUserService();
    System.out.println(service.getUser(1));

    service.printCacheStats();
}
