## Consumer Groups & Parallelism 

### What is Consumer Group?

A Group of consumers that work together to consume a topic.

Each partition is consumed by only one consumer within a group

### Why Consumer Groups exists
Without consumer groupd:
* One consumer --> slow
* No parallelism
* Scaling = painful

With consumer groups:
* add consumers
* Kafka automatically balances load
* Parallel processing achieved

### Partition <--> Consumer Mapping
Scenario 1:

* Topic : 3 partitions
* Consumer group : 3 consumers
``` 
C1 → P0
C2 → P1
C3 → P2
```
* Maximum parallelism
* No conflicts

Scenario 2: More consumers the partitiond

* Topi: 3 partitions
* Consumers: 5
``` 
 C1 → P0
C2 → P1
C3 → P2
C4 → idle
C5 → idle
```
* Idle consumers
* No benefit adding more consumers

Scenario 3: More Partitions than Consumers
* Topic: 6 partitions
* Consumers : 3

```
C1 → P0, P1
C2 → P2, P3
C3 → P4, P5
```
* Works fine 
* Each consumer processes multiple partitions

### Consumer Groups vs Broadcast

Same Group ID
* Consumers share partitions
* Each message processed once per group

Different Group Id
* Each group gets full copy of data
* Used for
  * Analytics
  * Auditing
  * Notifications

Kafka supports fan-out natively


### Group ID (How Kafka identifies groups)
A consumer group is identified by:
``` 
group.id = "payment-service-group"
```
Kafka stores:
* Group metadata
* Partition assignments
* Offsets

Group Id defines who shares work and who gets full data

### Group Coordinator
Kafka Chooses:
* One broker as group Coordinator
* Handles
  * Consumer joins
  * Partition assignment
  * Rebalancing

Cosumers:
* Send heartbeats
* Coordinator tracks liveness

### Parallelism Math

Maximum Parallelism = Number of partitions

NOT:
* Number of consumers
* Number of pods
* Number of threads

Want more parallelism?

Increase partitions

### Real-World Scenario

Topic: orders

Consumer Groups:
* payment-service-group
* inventory-service-group
* email-service-group

Each group:
* gets all order events
* Scales independently
* Does not affect others

### Very common mistakes

Using same group Id across different services

Result:
* Messages get split
* Services miss data
* Bugs that are VERY hard to debug

