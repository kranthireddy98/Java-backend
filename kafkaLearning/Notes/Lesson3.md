## Kafka Producers

### What is Kafka Producer
A producer is an application that:
1. creates events
2. Sends them to topic
3. Optionally chooses keys
4. Decides reliability vs performance

Producer never talks to consumers

Producers only talk to brokers

### Producer -> Kafka (Step-by-step Flow)
Internal Producer Flow
```text
Application
   ↓
Serializer
   ↓
Partitioner
   ↓
Record Accumulator (batching)
   ↓
Kafka Broker
```
Each step exists for performance and reliability.

### Serialization (First Gate)
Kafka only understands bytes.

So Producer must convert:
```
Object --> byte[]
```
Common Serializers:
* StringSerializer
* IntegerSerializer
* JSON (via custom/ Spring Kafka)
* Avro / Protobuf

Serialization cost directly impacts throughput.

### Partition Selection
How Kafka chooses a Partition

Case 1: Message WITH Key
```text
partition = has(key) % number_of_partitions
```
* Same Key --> same partition
* Order preserved for that key
```text
orderId = 101 → Partition 1
orderId = 101 → Partition 1
```
Case 2: Message WITHOUT Key
* Kafka uses round-robin (sticky partitioner)
* No ordering guarantee for related events

### Acknowledgements (acks) -- Reliability vs Performance

This setting defines:

When is a message considered "successfully sent"?

acks = 0
* Producer does not wait
* Fastest 
* Data can be lost

USE CASE:
* logs 
* Metrics
* Non-critical data

acks = 1(Default)
* Leader broker acknowledges
* Follower may lag
* Possible data loss if leader crashes

Balanced choice.

acks=all
* Leader + all ISR replicas acknowledge
* safest
* slightly slower

acks =all + replication + ISR = durability

### Producer Retries & Idempotency
Problem
* Network glitch
* Producer retries
* Duplicate messages possible

Solution
* enable.idempotency = true

Kafka guarantees:
* No duplicates
* Even with retries

### Batching (Why Kafka is Fast)
Producers do NOT send one message at a time.

They batch messages:
* batch.size
* linger.ms

linger.ms
* Time to wait before sending batch
* Higher value --> better throughput
* Lower value -> lower latency

Trade-off
* Throughput vs latency

### Compression
Producer can compress batches:
* gzip
* snappy
* iz4
* zstd

Benefits:
* Lower network IO
* Faster replication

Compression happens before sending.

### Failure Scenarios (Real world)
**Scenario 1: Broker Down**
* producer retries
* Metadata refreshed
* Leader  re-elected

**Scenario 2: Network Fluctuation**
* Retries happen
* Idempotence prevents duplicates

**Scenario 3: Wrong acks**
* Data loss possible

