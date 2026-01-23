## Replication & Fault Tolerance (How Kafka Survives Failures)

### Why Replication Is mandatory

Imagine:
* Topic 1 partition
* Stored on 1 broker
* Broker crashes

* Data lost
* Producer fail
* Consumers stuck

Kafka solves this using replication.

### Replication Factor(RF)

How many copies of a partition Kafka maintains

```java
Replication Factor = 3
```
Means:
* 1 Leader replica
* 2 Follower replicas
* On different brokers

Rule:

Replicas are never placed on the same broker

### Leader & Follower Replicas

Leader Replica
* Handles:
  * All writes (producers)
  * All reads (consumers)

Follower Replicas
* Copy data from leader
* Serves as backups
* Do Not handle client traffic

This keeps writes simple & fast

### ISR -- In-Sync Replicas
What is ISR?

Replicas that are fully caught up with the leader

Kafka tracks:
* which replicas are healthy
* Which are lagging

```java
Replicas: [Broker1, Broker2, Broker3]
ISR:      [Broker1, Broker2]
```
Broker 3 fell behind -> removed from ISR.

### Why ISR Matters
when producer uses:
```java
acks =all
```

Kafka guarantees:

Message is written to ALL replicas in ISR

* Data durability
* Safe leader selection
* No stale replicas acknowledged

### What Happens When Leader Broker Dies?

1. Leader broker crashes
2. Kafka detects failure
3. New leader elected from ISR
4. Producers & consumer continue

Critical Rule:

Only  ISR members can become leader

This avoids data inconsistency.

### min.insync.replicas

```java
min.insync.replicas =2
```
Means:
* At least 2 ISR replicas must be alive
* Otherwise --> producer write fails

Used with
```java
acks =all
```

### failure scenarios

Scenario 1: One Broker Down
* ISR Shrinks
* System continues
* No data loss

Scenario 2: ISR < min.insync.replicas
* Producers fail fast
* Data safety preserved

Scenario 3: All Brokers Alive
* Full throughput
* Maximum safety

### Trade-offs 

| Higher Replication | Lower Replication |
| ------------------ | ----------------- |
| Safer              | Faster            |
| More disk          | Less disk         |
| Slower writes      | Faster writes     |

### Payments
* Topic: payment
* Replication Factor: 3
* acks : all
* min.insync.replicas: 2

Guarantees:
* No payment loss
* safe failover
* Predictable behavior

