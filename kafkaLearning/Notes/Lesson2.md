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


### 1. Why Topics Alone Are not Enough?
Imagine this:
* One topic Orders
* Millions of orders per day
* Hundreds of consumers

Single topic file = bottleneck

One consumer = slow

No Parallelism

### 2. Partition 
**What is partition**

A partition is a physical, ordered, append only log inside a topic.

```text
Topic: orders
 ├── Partition 0
 ├── Partition 1
 └── Partition 2

```
Important:
* A topic always has >= 1 partition
* Partition enable parallelism
* Each partition is stored on a broker

### 3. Ordering Guarantee
Kafka guarantees:

Order id preserved only within a partition

Not ordering guarantee across partitions
```text
Partition 0: order-1 → order-3 → order-5
Partition 1: order-2 → order-4

```

### 4. Offest (Kafka's cursor/Pointer)
What is offset?

An offset is:
* A sequential number
* Assigned to each message within a partition
* Starts from 0

```text
Partition 0:
offset 0 → OrderCreated
offset 1 → PaymentDone
offset 2 → OrderShipped

```
Offset is:
* unique per partition
* Immutable
* Never reused

### 5. how consumers use offsets

A consumer:

* Reads messages
* Tracks last processed offset.
* Commits offset after processing

kafka does not track business logic

Kafka only tracks offset numbers

### 6. Parallelism
Scenario:
* Topic has 3 partitions
* Consumer group has 3 consumers

Result:
```text
Consumer 1 → Partition 0
Consumer 2 → Partition 1
Consumer 3 → Partition 2

```

What if:
* 5 consumers but only 3 partitions?

Result:
* 2 consumers sit idle

### 7. Keyed vs Non-keyed Messages

without key
* Kafka distributes messages round-robin
* No Ordering guarantee for same entity

With Key
* Same Key --> same partition
* Ordering guaranteed per key

```text
Key = orderId
orderId=101 → always Partition 1
orderId=101 → processed in order

```
This is how kafka preserves entity level ordering

### 8. Why Kafka is FAST 
Kafka:
* writes sequentially to disk
* Avoids random IO
* Uses OS page cache
* Reads by offset (no search)

Offset based reads = O(1)

### 9. Retention != Consumption
Kafka keeps data based on:
* Time --> eg: 7 days
* Size --> eg:100 gb

Even if consumer reads data:
* kafka does NOT delete it
* Another consumer can replay it.

### Real-World Scenario (Order System)
Topic: orders (3 partitions)

* Order Service publishes events 
* Payment Service consumes 
* Inventory Service consumes 
* Analytics Service consumes

Each service:
* Has its own offsets 
* Reads at its own speed 
* Can replay data independently