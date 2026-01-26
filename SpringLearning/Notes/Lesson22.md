## Messaging with Spring & Kafka

### Why Messaging Exists
Synchronous systems fail when:
* One service is slow
* One dependency is down
* Load spikes suddenly
* Tasks take long time

Example (tight coupling)
```
Order Service → Email Service → Notification Service
```
Problems:
* Cascading failures
* High latency
* Poor Scalability

### What Messaging solves
Messaging introduces asynchronous, decoupled communication.
``` 
Order Service → Kafka → Email Service
                         Analytics Service
                         Audit Service

```
Benefits:
* Loose coupling
* Failure isolation
* Horizontal scalability
* Event-driven architecture

messaging turns systems from call-based to event-based

### What is Kafka
Kafka is;
* Distributed event streaming platform
* Append-only log
* High-throughput, low latency

Kafka is not a queue in the traditional sense.

### Core Kafka Concepts
Topic
* Named stream of events

Partition
* Ordered, immutable log
* Unit of parallelism

Producer
* publishes messages

Consumer;
* Reads messages

Consumer Group:
* Group of consumers sharing load

Offset 
* Position of consumer in partition

### kafka Delivery Semantics

| Guarantee     | Meaning                              |
| ------------- | ------------------------------------ |
| At-most-once  | No retries, possible loss            |
| At-least-once | Retries allowed, duplicates possible |
| Exactly-once  | No loss, no duplicates (complex)     |

Most systems use at-least-once

### Kafka vs Async 

| Aspect            | Async      | Kafka       |
| ----------------- | ---------- | ----------- |
| Scope             | Single JVM | Distributed |
| Durability        | ❌          | ✅           |
| Retry             | Manual     | Built-in    |
| Scaling           | Limited    | Excellent   |
| Failure tolerance | Low        | High        |

use:
* Async in-process work
* Kafka inter-service communication

### Spring Kafka Architecture
```text
Producer → KafkaTemplate → Kafka Broker → Consumer → @KafkaListener
```

Spring Kafka:
* Abstracts Kafka APIs
* Handles serialization
* manages consumer lifecycle


### Producing Messages 

Producer Example

```java
@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void publish(OrderEvent event) {
        kafkaTemplate.send("order-events", event.getOrderId(), event);
    }
}

```
What happens:
* Message serialized
* Sent to Kafka
* Stored drably

### Problem 1: Message sent but consumer not received
Causes:
* Wrong topic name
* Consumer group mismatch
* Serialization failure
* Consumer not started

**Debug checklist**
* Topic exists
* Correct group-id
* Same Serialization on both sides
* Consumer logs show partition assignment

### Consuming Messages(`@kafkaListener`)
```java
@KafkaListener(
    topics = "order-events",
    groupId = "email-service"
)
public void handle(OrderEvent event) {
    // process event
}
```
Spring 
* Creates consumer
* Assigns partitions
* Polls messages
* Invokes method

### Problem 2: Message Processed Multiple Times
Why it happens
* At-least-once delivery
* Consumer crash before commit
* Retry after failure

**Solution: Idempotent Consumers**
```java
if (processedEventRepo.exists(event.getId())) {
    return;
}
process(event);
```
OR Use:
* Unique DB constraints
* Deduplication logic

### Offset Management
Offsets represent:
* How far consumer has read

Commit Types
* Auto-commit
* Manual commit

Spring default:
* Commit after listener success

If listener throws exceptions:
* Offset not commited
* Message reprocessed

### Problem 3: Poison Message (Stuck Consumer)
Message always fails:
* Consumer retries forever
* Partition blocked
```java
@KafkaListener(...)
public void handle(OrderEvent event) {
    try {
        process(event);
    } catch (Exception e) {
        throw e;
    }
}
```
Spring Kafka supports
* Retry topics
* Dead-letter topics

Failed messages moves aside, not blocking stream.

### Serialization 
Problem 4: serializationException

Causes:
* Producer uses JSON
* Consumer expects String
* Class version mismatch

**Solution**
Use consistent serialization
```java
spring.kafka.producer.value-serializer=JsonSerializer
spring.kafka.consumer.value-deserializer=JsonDeserializer
```
And trust packages
```java
spring.kafka.consumer.properties.spring.json.trusted.packages=*
```


### Ordering Guarantees
Kafka guarantees:
* Ordering within a partition
* NOT across partitions

To preserve order:
* Use same key for related messages
``` 
 kafkaTemplate.send("orders", orderId, event);

```

### Transactions in Kafka 
Kafka supports:
* producer transactions
* Exactly-once semantics

But:
* Complex
* Slower
* rarely needed

Most systems prefer:

At-least-once idempotent consumers

### When Not to Use Kafka
* Simple CRUD apps
* Low traffic systems
* Strict real-time requirements
* If operational overhead is unacceptable

Kafka is powerful but Heavy



















