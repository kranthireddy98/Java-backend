## Exactly-Once Semantics (EOS) & Kafka Transactions

### Kafka guarantees exactly-once only within Kafka
(Producer -> Kafka -> Consumer -> Kafka)

Once you involve:
* Databases
* REST calls
* External systems

EOS becomes your responsibility

### The Three LEVELS of "Once"

let's cleanly separate them:

1. At-Most-Once
    * Possible loss
    * No duplicates
2. At-Least-Once
    * No loss
    * Duplicates possible
3. Exactly-Once (Kafka EOS)
    * No loss
    * No duplicates
    * Only inside kafka pipeline

Kafka EOS != end-to-end exactly-once across all systems.

### What Kafka EOS Actually guarantee?
* Producer does not write duplicates
* Consumer does not reprocess commited messages
* Writes + offset commits are atomic

### Two pillars of Kafka EOS

Pillar 1: Idempotent Producer

Enabled by default in modern Kafka:

``` 
enable.idempotence = truee
```

Guarantees:
* Retries do NOT Create duplicates
* Producer sequence numbers tracked
* Broker discards duplicates

This alone != EOS

It only handles producer-side duplicates

Pillar 2: Transactions

Transactions guarantee atomicity across:
* consuming messages
* Producing new messages
* Commiting offsets

This is a big leap.

### Kafka Transactions 

Without Transaction
``` 
Consume → Produce → Commit Offset
❌ If crash happens midway → duplicates or loss
```

With transaction 
``` 
Begin Transaction
   ↓
Consume messages
   ↓
Produce output messages
   ↓
Commit consumer offsets (as part of txn)
   ↓
Commit Transaction
```

* All or nothing
* No partial state

### What Happens on Failure

If crash occurs:
* Transaction is aborted
* Produced messages are invisible
* Offsets are NOT commited
* Messages reprocessed safely

This is true exactly-once

### Transactional IDs 

Each transactional producer has:
``` 
transactional.id = prder-service-txn
```
Kafka uses it to:
* Track producer state
* Fence zombie producers
* Prevent split-brain writes

trap:

transactional.id must be stable, not random

### Where EOS is Commonly Used
**Good Use Cases**
* Kafka streams
* Stream-to-stream transformations
* Kafka -> Kafka pipelines
* Aggregations

**Bad Use Cases**
* Kafka -> Database 
* Kafka -> REST APIs
* Simple CRUD events

Because:
* External systems aren't transactional with Kafka

### EOS vs Idempotency

| Aspect      | EOS        | Idempotency |
| ----------- | ---------- | ----------- |
| Complexity  | High       | Low         |
| Scope       | Kafka-only | Any system  |
| Performance | Slower     | Faster      |
| Usage       | Rare       | Very common |

* Mostly prefer idempotency
* EOS is used only when strictly required



































