## Kafka Producer with Spring Boot

### Spring Boot -> Kafka
``` 
Controller / Service
      ↓
KafkaTemplate
      ↓
Serializer
      ↓
Kafka Broker
```
Spring Kafka:
*  Wraps Kafka java client 
* Handles boilerplate
* Gives annotations & templates.

under the hood, it's still pure Kafka

### Required Dependency
``` 
<dependency>
  <groupId>org.springframework.kafka</groupId>
  <artifactId>spring-kafka</artifactId>
</dependency>

```
That’s it.

No separate Kafka client needed — Spring brings it.

### Basic Producer Configuration
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      acks: all
      properties:
        enable.idempotence: true
```
Why these settings?

* `acks=all` -> durability
* `enable.idempotentce = true` -> no duplicates
* JSON Serializer -> easy object publishing

### Kafka Template
`KafkaTemplate<K,V` is Spring's abstraction for producing messages

It:
* Handles serialization
* Handles retries
* Sends asynchronously

### Simple Produce Code

```java
public class OrderEvent {
    private Long orderId;
    private String status;
}
```

Producer Service
```java
@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void sendOrderEvent(OrderEvent event) {
        kafkaTemplate.send(
            "orders",
            event.getOrderId().toString(), // key
            event
        );
    }
}
```
**Key Points:**
* Topic = `orders`
* key = `OrderId`-> ordering guaranteed
* Value = `JSON`

### Why Key matters 
``` 
kafkaTemplate.send("orders", orderId, event);
```
Guarantees:
* same orderId -> same partition
* Order of events preserved
* No race conditions

No Key -> random partition -> bugs later


### Asynchronous Send & callbacks

Kafka sends asynchronously.
```java
kafkaTemplate.send("orders", key, event)
    .addCallback(
        result -> log.info("Sent successfully"),
        ex -> log.error("Send failed", ex)
    );
```
Use callbacks to:
* Log failures
* Trigger retries
* Alert systems

### Common Production configs

You'll see these in real projects:
```yaml
linger.ms: 5
batch.size: 16384
compression.type: snappy
retries: 10
```
Meaning:
* Batch messages
* Improve throughput
* Compress network traffic
* Retry safely

### Real world scenario (Order service)
When order is created:
1. REST API receives request
2. DB transaction completes
3. Kafka event published
4. Downstream services react

Kafka becomes:
* Event backbone
* Async communication layer
* Decoupling mechanism































