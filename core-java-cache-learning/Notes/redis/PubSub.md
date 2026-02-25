### Redis Pub/Sub & Streams (Messaging Patterns)

Redis Supports:
1. Pub/Sub (simple real time messaging)
2. Streams (persistent event log, Kafka-like)

But they are very different

### Redis Pub/Sub

What is Pub/Sub?

Publisher sends message -> All subscribers receive it.

``` 
publisher -> channel -> subscribers
```
**Commands**
* Subscribe Orders
* Publish orders "order_created"

Internally

``` 
Publisher sends message
       │
Redis pushes to all active subscribers
```
Important:

Pub/Sub messages are NOT stored.

If subscriber is offline:

Message is lost.

### When to Use Pub/Sub?

Good for:
* Real-time notifications
* Chat apps
* Live dashboards
* Websocket broadcasting

Not Good for:
* Order processing
* Payment events
* Critical workflows

Because:

No durability.

### Redis Streams 
Streams introduced in Redis 5.

Think of Streams as:

Basic Commands

Add event
``` 
XADD orders * useer 101 amount 500
```

Creates entry with ID like:
``` 
1678923456-0
```

Read events
``` 
XREAD COUNT 10 STREAMS orders 0
```
### Stream vs Pub/Sub

| Feature           | Pub/Sub | Streams |
| ----------------- | ------- | ------- |
| Persistence       | ❌       | ✅       |
| Message history   | ❌       | ✅       |
| Consumer groups   | ❌       | ✅       |
| Reliable delivery | ❌       | ✅       |

### Consumer Groups
Streams support:

Consumer Groups

``` 
XGROUP CREATE orders group1 0
```
Multiple consumers in group:
* Each message processed by only one consumer
* Load balancing supported

### Streams Flow
``` 
Producer → Stream (stored) → Consumer Group → Consumers
```
If consumer crashes:

Message remains pending

Another consumer can claim it.

### Real Example

Order Service emits event:
``` 
XADD orders *
  orderId 101
  status CREATED
```
Inventory Service consumes.

Payment Service consumes.

Reliable event delivery.

### Redis Streams vs Kafka

Let's compare with Apache Kafka

| Feature      | Redis Streams        | Kafka                  |
| ------------ | -------------------- | ---------------------- |
| Performance  | Very fast            | Very scalable          |
| Partitioning | Limited              | Strong                 |
| Retention    | Limited              | Configurable long-term |
| Best For     | Lightweight eventing | Enterprise streaming   |

### When to use What?

use Pub/Sub when;
* Fire-and-forget
* Real-time UI updates

Use Streams when:
* Need persistence
* Need replay
* Need consumer groups

Use Kafka when;
* Very high throughput
* Large-scale event streaming
* Multi-datacenter

### Production Limitation
Redis Streams:
* Still single-node (unless cluster)
* Not ideal for massive event volume
* Memory-based storage

Kafka:
* Designed for event streaming at scale

### Backend Design Insight (For you)

Redis Streams can:

* Act as event for small systems
* Support Saga orchestration in lightweight systems

But in enterprise:

Kafka preferred



























