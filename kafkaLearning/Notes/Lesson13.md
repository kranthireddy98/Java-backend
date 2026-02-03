## Consumer Failures, Retries & Idempotent Processing 

### The Uncomfortable Truth

Failures WILL Happen. Duplicates WILL happen.

Kafka guarantees delivery semantics, not business correctness

Your job as an engineer is to design for failure

### types of Consumer failures
1. Transient Failures (Retryable)
* DB temporarily down
* Network glitch
* Downstream service timeout

**Retrying makes sense**

2. Permanent Failures (Non-Retryable)
* Invalid data
* Schema mismatch
* Business rule violation

**Retrying forever is pointless**

3. Consumer Crash
* App Killed
* Pod restarted
* JVM OOM

Kafka will re-deliver messages


### What Kafka does on failure
Kafka's behavior is simple:
* If offset NOT commited -> message is retried
* If offset committed -> message is gone (for that group)

Kafka does not know why you failed

### Retry Flow (Spring Kafka View)
Typical 

``` 
Consume message
   ↓
Process fails 
   ↓
Exception thrown
   ↓
Offset not committed
   ↓
Message re-polled

```
* If not controlled -> infinite retry loop

### Controlled Retries
Use Backoff + retry Limit
```java
@Bean
public DefaultErrorHandler errorHandler() {
    return new DefaultErrorHandler(
        new FixedBackOff(2000L, 3) // 3 retries, 2 sec gap
    );
}
```
Behavior:
* Retry 3 times
* If still failing -> give up
* Offset still not commited

next?  --> Dead Letter Topic

### Poison Messages (Production Killer)

What is a Poison Message?

A message that:
* Always fails
* Blocks partition forever
* Increases lag endlessly

Example:
* corrupted JSON
* Invalid enum
* Missing mandatory field

Without handling --> system stalls

### Why Idempotency is mandatory
Because Kafka guarantees at-least-once, your consumer must handle duplicates.

Idempotency definition

processing the same message multiple times produces the same final result.

### Idempotent Processing Patterns

Pattern 1: Database Unique Constraint

``` 
UNIQUE (order_id)
```
* Duplicate insert -> ignored / rejected safely

Pattern 2: Deduplication Table
``` 
processed_events(event_id)
```
* Check before processing
* Process only once

Pattern 3: Update by Business Key

``` 
UPDATE orders SET status='PAID' WHERE order_id=?
```
* Same update twice -> same result

### Exactly-Once Vs Effectively-Once

* Exactly-Once is expensive & complex
* Idempotent + at-least-once = effective once

This is what most systems use.

### Real-World Scenario
* Message : `paymentCompleted`
* DB write succeeds
* App crashes before offset commit
* Message reprocessed

Without idempotency:
* Duplicate payment

With idempotency:
* Safe reprocessing









































