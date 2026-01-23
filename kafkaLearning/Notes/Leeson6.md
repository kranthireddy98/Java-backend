## Rebalancing

Rebalancing is the process where:

Kafka stops all consumers in a group, redistributes partitions, and restarts consumption.

Triggered when:
* A consumer joins
* A consumer leaves
* A consumer crashes
* Partitions increase
* Topic subscription changes

### What Actually Happens During Rebalance
Step-by-step 
1. Consumers stop polling
2. Offsets may or may not be committed
3. Group coordinator kicks in
4. Partitions are reassigned
5. Consumers resume from last commited offset

During this time:
* No Messages are processed
* Lag increases
* SLAs may break

### Why Rebalancing is Dangerous
**Processing pause**
* Entire group pauses
* Even healthy consumers stop

**Duplicate Processing**
* if offsets not commited
* Messages reprocessed

**Long Rebalance Time**
* Large groups
* Large partitions
* Heavy startup logic.

### Consumer Liveness (Heartbeats)
Consumers send heartbeats to coordinator.

Key configs:
* `session.timeout.ms`
* `heartbeat.interval.ms`
* `max.poll.interval.ms`

Common Failure:

Long processing + no poll -> Kafka thinks consumer is dead

Triggers rebalance

### Eager Rebalancing (Old Default) 

Behavior:
* All consumer stop
* Full rebalance
* Heavy disruption

This was default for years

### Cooperative Rebalancing (Modern Kafka)
What Changed?

Kafka introduced incremental cooperative rebalancing

Benefits:
* Consumer keep processing unaffected partitions
* Ony revoked partitions are reassigned
* Minimal downtime

Requires:
* Compatible client versions
* Proper configuration

### Reduce Rebalancing
1. Avoid frequent consumer restarts
2. Tune poll configs
3. Use static membership
   ``` group.instance.id ```
4. Commit offsets safely
5. Use cooperative rebalancing

### Static Membership
Problem:
* Pod restarts
* Rolling deployments
* Kafka thinks consumer left

```java
group.istance.id = payment-consumer-1
```

Kafka:
* Remembers the consumer
* Avoids unnecessary rebalance

### Reals-World Scenario
Without static membership:
* Pod restart --> rebalance
* All consumers pause

With static membership:
* Pod restarts
* Minimal or no rebalance

