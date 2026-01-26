  ## Offset Commit Strategies & Delivery Guarantees
  
### Offsets != Processing Success
kafka only knows:
* What offset was commited
* Kafka does NOT know;
  * Whether your DB write succeeded 
  * Whether your API call failed
  * Whether your business logic crashed

Offset commit timing defines your delivery guarantee

### Where Offsets are stored
```java
__COnsumer_offsets
```
Kafka internal topic:
* once per consumer group
* Highly replicated
* Fault tolerant

Offsets are just Kafka messages too.

### Auto commit vs Manual Commit

Auto Commit
* Kafka commits periodically
* happens before or during processing
* Simple
* Risky

Used only for:
* Logs
* Metrics
* Non-critical streams

Manual Commit 
* Commit after processing
* Full control
* Safe
* Slightly more code

Real system almost always use manual commit

### Delivery Guarantees
1. At-Most-Once

Flow
``` 
Poll -> Commit -> Process
```
* No duplicates
* Messages can be lost

Use When:
* Data loss acceptable
* Speed matters

Example
* Monitoring metrics

2. At-Least-Once
``` 
Poll -> Process -> commit
```

*  No Data loss
* duplicates possible

If Crash happens:
* message reprocessed

Use When:
* Data loss NOT acceptable
* idempotency required

Example
* Orders
* payments
* Notifications

3. Exactly-Once

Message processed nce and only once

Requires:
* Idempotent producers
* Transactions
* Careful consumer logic

### Visual Timeline
Crash before Commit
``` 
Process ❌
Commit ❌
→ Message reprocessed
```
At-least-once

Crash AFTER commit
``` 
Process ❌
Commit ✅
→ Message lost
```
At-most-once

### Why At-least-once is preferred
Reason;
* Data los is usually worse than Duplicates

Solution to duplicates:
* Idempotent processing
* Deduplication logic
* unique business keys

### Idempotency
What is idempotent Processing?

Processing the same message multiple times results in : same final state

Example;
* Insert with unique key
* Update by ID
* check-before-insert

Idempotency turns at-least-once into effective-once

### Commit Types (Manual)
Synchronous Commit
* Blocks 
* Safer
* Slower

Asynchronous Commit
* Faster
* Might miss failures
* Used with caution

Production systems;
* Mostly synchronous
* Or hybrid approach

### Real-World Scenario

Flow
1. Consume OrderCreated
2. Save order in DB
3. Commit offset

If crash after step 2 but before commit:
* Order already exists
* Reprocessing safe due to idempotency

* No Data loss
* System consistent














































