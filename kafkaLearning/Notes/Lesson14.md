## Dead Letter Topic (DLT) & Failure Isolation

### The Core Problem DLT Solves

Without DLT:
* One bad message
* Infinite retries
* Partition blocked
* Lag grows
* System appears down

With DLT:
* Bad message isolated
* Consumer continues
* System stays healthy

DLT = failure containment

### What is Dead Letter Topic?

A Dead Letter Topic is:

A special Kafka topic where messages that failed processing are sent after retry attempts.

DLT messages usually include:
* original payload
* Error reason
* Stack trace
* Topic, partition, offset
* Timestamp

DLT is for humans & recovery systems, not normal flow.

### When Should a Message Go to DLT?

After:

* Retry limit exceeded
* Non-retryable exception
* Data validation failure
* Schema mismatch
* Business rule violation

NOT for:
* Temporary DB outage
* Short network glitch

### DLT Flow (Spring Kafka)

```
Main Topic
   ↓
Retry (n times)
   ↓
Dead Letter Topic

```

Main consumer:
* Skips bad message
* Commits offset
* Continues processing

### Spring Kafka DLT Configuration

```java
@Bean
public DefaultErrorHandler errorHandler(
        KafkaTemplate<Object, Object> template) {

    DeadLetterPublishingRecoverer recoverer =
        new DeadLetterPublishingRecoverer(template);

    return new DefaultErrorHandler(
        recoverer,
        new FixedBackOff(2000L, 3)
    );
}

```

Meaning:
* Retry 3 times
* Then publish to DLT
* then commit offset

### DLT Topic Naming Convention

common patterns
* order.DLT
* order.dead
* order.error

```
<topi>.DLT
```

Naming consistency matters for ops teams.

### What happens After Message Goes to DLT?
options:
1. Manual inspection
2. Fix data & reply
3. Ignore permanently
4. Alert & audit

Kafka enables:
* Reply from DLT
* Debug without blocking production

### Retry Topics vs DLT 

| Retry Topics       | DLT                 |
| ------------------ | ------------------- |
| Temporary failures | Permanent failures  |
| Delayed retries    | Final destination   |
| Auto retry         | Manual intervention |


``` 
main → retry → retry → DLT
```

### Real-World Scenario
* Message has invalid email
* Retry won't fix
* Sent to DLT
* Customer support fixes data
* Message replayed

System never stops





































