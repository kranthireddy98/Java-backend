## Kafka Consumer with Spring Boot


### Spring Boot consumer

``` 
Kafka Broker
   ↓
Consumer Group
   ↓
KafkaListenerContainer
   ↓
@KafkaListener
   ↓
Business Logic
   ↓
Offset Commit

```
Spring Kafka:
* Manages poll loop
* Manages threads
* Manages rebalancing callbacks
* You focus on business logic


### Basic Consumer Configuration

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: order-service-group
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      auto-offset-reset: earliest
      enable-auto-commit: false
      properties:
        spring.json.trusted.packages: "*"
```
Why these settings matter:
* Enable-auto-commit = false -> Manual control
* group-id -> defines consumer group
* auto-offset-reset=earliest -> safe startup behior

Manual commit = production default.

### @KafkaListener (core annotation)
Basic Consumer

```java
import org.springframework.kafka.annotation.KafkaListener;

@KafkaListener(
        topics = "orders",
        groupId = "order-service-group"
)
public void consumer(OrderEvent event) {
    log.info("Received order {}", event.getOrderId());
}
```
Spring automatically:
* Deserialized JSON -> object
* Polls Kafka
* Handles threading

### Manual Offset Commit (Acknowledgment)
Acknowledgement-Based Commit

```java
import org.springframework.kafka.annotation.KafkaListener;

@KafkaListener(
        topics = "orders",
        groupId = "order-service-group"
)
public void consumer(OrderEvent event) {
    processOrder(event);
    
    ack.acknoeledge(); // commit offset AFTER processing
}
```
* At-least-once guarantee
* Safe for critical data

If app crashes before `acknowledge()` -> message reprocessed

### Consumer concurrency (Scaling Consumers)
concurrency setting

```java
import org.springframework.kafka.annotation.KafkaListener;

@KafkaListener(
        topics ="orders",
        groupId = "order-service-group",
        concurrency = "3"
)
```
Meaning:
* 3 consumer threads
* max parallelism = min (partitions, concurrency)

Concurrency > partitions =  wasted threads

### Listener Container 

Spring creates:
* One KafkaMessageListener per thread
* One Kafka Consumer per container

Spring handles:
* Heartbeats
* Rebalancing 
* Poll loop
* Error propagation

This is why Spring Kafka is production-friendly


### Error handling
Default Behavior
* Exception thrown
* Offset NOT commited
* Message retried
* Potential infinite loop

Recommended: Error Handler
```java
@Bean
public DefaultErrorHandler errorHandler() {
    return new DefaultErrorHandler(
        new FixedBackOff(2000L, 3)
    );
}
```
Meaning:
* Retry 3 times
* 2 seconds apart
* Then fail

We'll extend this to DLT

### What Happens During Rebalancing
Spring:
* Pauses listeners
* Commits offsets
* Reassigns partitions
* Resumes listeners

Business logic should be:
* Idempotent
* Stateless where possible

### Scenario
* Topic : `orders`
* Consumer group: `payment-service-group`
* Concurrency: 3
* Manual commit
* Error handler enabled

Behavior;
* Orders processed in parallel
* Failures retried
* No data loss
* Safe rebalancing









































