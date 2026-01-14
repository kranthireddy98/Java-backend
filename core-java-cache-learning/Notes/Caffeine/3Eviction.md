# Eviction, Expiration, adn the "Ghost" Entries

#### Size based Eviction
In production, you must set a limit. without it, you cache is just a HashMap that will eventually cause an OutOfMemoryError.

* `maximumSize(long)`: Evicts based on the number of entries.
* `maximumWeight(long)`: Evicts based on the Cost. Useful if some cache objects are 1KB and others are 1MB.

#### Time Based Expiration
1. `ExpireAfterWrite`: Best for data that becomes wrong after certain time (e.g., a stock price).
2. `expireAfterAccess`: Best for Session data. if the user stops using it, let it dies.
3. `refreshAfterWrite`: the pro-choice. instead of deleting the value and making the next user wait for a reload, Caffeine returns the old value and reloads the new one in the background.

#### Why `rerfreshAfterWrite` is "production Secret"

In High-load systems, `expiryAfterWrite` can cause a laten