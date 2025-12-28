package com.cache.service;

import com.cache.model.User;
import com.cache.repository.UserRepository;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * L1 + L2 Cache implementation.
 *
 * L1 -> Caffeine (fast, JVM-local)
 * L2 -> Redis (distributed)
 * DB -> SQL Server (source of truth)
 */
public class L1L2UserService {

    private final UserRepository repository = new UserRepository();

    // Reuse existing services
    private final CaffeineUserService l1Cache = new CaffeineUserService();
    private final RedisUserService l2Cache = new RedisUserService();

    /**
     * Read flow:
     * 1. L1 (Caffeine)
     * 2. L2 (Redis)
     * 3. DB
     */
    public User getUser(int userId) throws SQLException {

        // 1️⃣ L1 Cache
        User userFromL1 = l1Cache.getIfPresent(userId);
        if (userFromL1 != null) {
            System.out.println("L1 CACHE HIT");
            return userFromL1;
        }

        // 2️⃣ L2 Cache
        User userFromL2 = l2Cache.getIfPresent(userId);
        if (userFromL2 != null) {
            System.out.println("L2 CACHE HIT → PROMOTE TO L1");
            l1Cache.put(userId, userFromL2);
            return userFromL2;
        }

        // 3️⃣ DB (Last resort)
        System.out.println("CACHE MISS (L1 + L2) → DB CALL");
        User userFromDb = repository.getUserById(userId);

        // Populate both caches
        l2Cache.put(userId, userFromDb);
        l1Cache.put(userId, userFromDb);

        return userFromDb;
    }

    /**
     * Write / update flow.
     * DB is source of truth → evict caches.
     */
    public void updateUser(User user) {

        // Update DB first
        repository.updateUser(user);

        // Evict caches
        l1Cache.evict(user.getId());
        l2Cache.evictUser(user.getId());

        System.out.println("CACHE INVALIDATED (L1 + L2)");
    }

    /**
     * Graceful shutdown.
     */
    public void shutdown() {
        l1Cache.shutdown();
        l2Cache.close();
    }
}
