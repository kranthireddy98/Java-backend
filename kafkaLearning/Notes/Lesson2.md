### Topic -- Not a queue 
In Apache Kafka:

A Topic is a logical stream of events, not a queue.
* kafka Stores messages
* Message stays until retention period
* Many Consumers can read the same message

`Topic: bulk Events`

Contains
``` 
TXN1|HIRE
TXN1|ADC
TXN1|WDC
TXN2|HIRE
TXN2|ADC
```

All Actions, all transactions -- one stream.

### Partition 
Partitions are the unit of parallelism in kafka.

Key :
* Topic is split into partitions
* Each partition is an ordered log
* Kafka writes sequentially inside a partition 

``` 
bulk-events
 ├── Partition 0
 ├── Partition 1
 └── Partition 2
```

**Kafka guarantees ordering only within a partition, never across partitions.**

### Message Key

When Producing:
``` new ProducerRecord<>("bulk-events",transactionId,value)```
What kafka internally does 
```Partition = hash(transactionId)% numberOfPartitions```

Result:
* All events of TXN1 will be into same partition
* Order preserved
``` HIRE -> ADC -> WDC```

### Offset
Offset = position of a message inside a partition.
``` 
Partition 0
Offset 0 → TXN1|HIRE
Offset 1 → TXN1|ADC
Offset 2 → TXN1|WDC

```
Important : 
* kafka does not track who consumed 
* consumer track their own offsets


### Consumer Group

A consumer group is a logical name that kafka uses to distribute partitions.

Rule : 

One Partition can be consumed by only one Consumer in the group


``` 
Topic: bulk-events (3 partitions)

Group: hire-group
 ├── Consumer-1 → Partition 0
 ├── Consumer-2 → Partition 1
 └── Consumer-3 → Partition 2

```
* Parallel Processing

``` 
Group: hire-group → reads ALL partitions
Group: adc-group  → reads ALL partitions
Group: wdc-group  → reads ALL partitions

```
Each group gets its own copy of data.

### Parallelism vs Ordering

Let's say:
* 3 partitions
* 3 consumers in same group

**What you gain**

* Parallelism
* High throughput

**What you loose**
* Global Ordering

### Rebalancing 

* Consumer joins
* Consumer leaves
* Partition count changes

Kafka Will :
1. stops consumption
2. Reassign partitions
3. Resume from last commited offset


