## Kafka Performance Tuning (Throughput vs Latency)

### Performance Starts with the right Question

Before touching configs, ask:

Do I want throughput or latency?

You usually cannot maximize both.

* Analytics, logs -> throughput
* payments, alerts -> latency

### producer-Side Tuning 

**Batch Size**
* Size of one batch (bytes)
* Larger batch - higher throughput
* Smaller batch -> lower latency

**Linger.ms**
* How long producer waits to fill batch
* Higher linger -> better batching
* Lower linger -> faster send

longer.ms -> trades latency for throughput


**Compression.type**

Options
* snappy (balanced)
* lz4 (fast)
* gzip (smallest, slowest)

benefits:
* Less network IO
* Faster replication

Most Used:
``` 
compression.type=snappy
```

**acks & retries**
* acks = all -> safety
* More retries -> reliability
* Slight latency increase

### Consumer-side Tuning (Avoid Bottlenecks)

**max.poll.records**
* Number of records per poll
* Too high -> long processing -> rebalance
* Too low -> inefficient polling

**fetch.min.bytes**
* Minimum data broker sends
* higher -> better throughput
* Lower -> faster response

**fetch.max.wait.ms**
* Max wait time to fill fetch
* Works with `fetch.min.bytes`

### Polls interval vs processing Time
key config:

``` 
max.poll.interval.ms
```
Rule:

processing time MUST be < max.poll.interval.ms

if not:
* Kafka assumes consumer is dead
* Triggers rebalance

### Threading & parallelism
* Kafka parallelism = partition 
* Threads != more parallelism
* Use:
  * More partitions
  * More Consumers

Don't just increase threads blindly

### Broker-side Tuning
Common broker configs;
* num.network.threads
* num.io.threads
* Disk type (SSD vs HDD)

usually handled ny platform/DevOps teams

### End-to-End Performance Thinking

Performance is:

```
Producer → Network → Broker → Disk → Consumer
```
Bottleneck can be anywhere:
* slow serialization
* small batches
* Under-partitioned topics
* Slow consumers

### Safe Production Defaults
``` 
linger.ms=5
batch.size=16384
compression.type=snappy
acks=all
enable.idempotence=true
```
These give:
* Good throughput
* Safe delivery
* Acceptable latency

### Real-World Scenario
* 100K orders/min
* JSON payload - 1kb
* Goal : stable throughput

**Tuning**
*  Increase batch.size
* Enable compression
* Increase partition
* Monitor consumer lag

Result:
* Stable system
* Predictable latency
























