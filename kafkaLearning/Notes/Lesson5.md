## Kafka Consumers

### What is a kafka Consumer
A consumer is an application that:
* Subscribed to one or more topics
* Pulls records from Kafka
* Processes them
* Tracks progress using offset

Kafka never pushes data to consumers

Consumers always ask for data

### Pull Model vs Push Model
Push Model (traditional MQ)
* Broker pushes messages
* Consumer can get overwhelmed
* hard to control backpressure

kafka Pull Model
* Consumer asks for data
* Consumer controls speed
* Natural backpressure handling

Kafka Chose Pull to let consumer process at their own pace and avoid overload

### Consumer Poll Loop (Core Mechanism)
Simplified Consumer Logic
```java
while (true) {
   records = consumer.poll()
   process(records)
   commit offsets
}
```
Key points:
* `poll()` is mandatory
* polling too slow --> consumer considered dead
* Polling too fast --> empty response

### Offset Tracking (Consumer Responsibility)
Very Important Concept

kafka:
* stores messages
* Sores offsets

But

Kafka does not know if your business logic succeeded

Consumer decide:
* When to commit offset
* What offset to commit

### offset Commit Strategies

Auto Commit
* kafka commits periodically
* simple
* risky if processing fails

manual Commit
* commit after processing
* Safer
* Used in real systems

real production uses manual

### Consumer Lag
Latest offset in partition - consumers commited offset

```java
Latest offset = 500
Consumer offset = 480
Lag = 20
```
High lag means:
* Consumer is slow
* system under pressure
* Potential outage soon

### What Happens When Consumer Crashes?
scenario:
* Consumer processed message
* Did NOT commit offset
* Crashed

Result:
* kafka re-sends messages
* Duplicate processing possible

This is at-least-once delivery

### Multiple Consumers Reading same Topic
Important distinction:
* Same topic
* Same consumer group -- partitions shares
* Different consumer group -- fully copy of data

Kafka supports fan-out naturally?

### Consumer Configuration
Key configs
* max.poll.records
* max.poll.interval.ms
* session.timeout.ms

These control:
* Batch size
* Rebalance safety
* consumer liveness

