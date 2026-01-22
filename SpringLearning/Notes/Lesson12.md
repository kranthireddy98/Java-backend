## Spring Events & Application Listeners


### What are Spring Events?
Spring events provide a way of components to communicate without direct dependencies

Instead of:
* calling another service directly
* Creating tight coupling

you:
* Publish an event 
* Let listeners react

Events are about notification, not command

### Why Spring Events Exist

Without Events (Tight Coupling)
```java
@Service
public class OrderService {

    private final EmailService emailService;
    private final AuditService auditService;

    public void placeOrder() {
        // business logic
        emailService.sendMail();
        auditService.log();
    }
}
```

Problems:
* Too many dependencies
* Hard to add/remove behavior
* Business logic polluted with side effects

With Events (Loose Coupling)
```java
@Service
public class OrderService {

    private final ApplicationEventPublisher publisher;

    public void placeOrder() {
        // business logic
        publisher.publishEvent(new OrderPlacedEvent());
    }
}
```
Listener decide what to do, not the service.

### core components of Spring Events
Spring Events consist of three parts:
1. Events
2. Publisher
3. Listener

### Events in Modern Spring 
Older Spring;

```java
import org.springframework.context.ApplicationEvent;

class OrderPlacedEvent extends ApplicationEvent{}
```
Modern Spring
```java
public record OrderPlacedEvent(Long orderId) {}
```
* Any Object can be an event
* No inheritance required

### Publishing an Event
```java
@Service
public class OrderService {

    private final ApplicationEventPublisher publisher;

    public OrderService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void placeOrder(Long orderId) {
        publisher.publishEvent(new OrderPlacedEvent(orderId));
    }
}
```
What happens:
* event is published
* Spring finds all matching listeners
* Invokes them

### Listening to Events
Using `@EventListener`
```java
@Component
public class EmailListener {

    @EventListener
    public void handle(OrderPlacedEvent event) {
        // send email
    }
}
```
Spring automatically:
* Registers listener
* matches by event type
* Invokes method

### Execution Model
Default Behaviour: Synchronous
* Publisher thread executes listeners
* If listener fails --> publisher fails
* OrderService waits for all listeners

#### Problem 1: heavy Logic in Event Listener
```java
public void handle(OrderPlacedEvent event){
    sendEmail(); //slow
    callExternalApi(); //slower
}
```
Issue:
* Blocks business flow
* increases response time
* Cascading failures

**Solution Asynchronous Events**

```java

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@Async
@EventListener
public void handle(OrderPlacedEvent event) {
    sendEmail();
}
```

And enable async:

```java
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync

@Configurable
public class AsyncConfig {

}
```
Now:
* Listener runs in separate thread
* Publisher returns immediately

### Threading Model

| Type                | Execution    |
| ------------------- | ------------ |
| Sync listener       | Same thread  |
| Async listener      | Thread pool  |
| Transactional event | After commit |

Async listeners:
* use `TaskExecuotr`
* Must be thread-safe
* Do Not share request/session scope blindly

#### Problem 2: Event Fired But DB Not committed
```java
publisher.publishEvent(new OrderPlacedEvent(orderId));
```
Listener tries:
```java
orderRepository.findById(orderId);
```
sometimes data not found.

Why this happens
* Event fired inside transaction
* DB commit not done yet
* Listener reads stale data

**Solution**
```java
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
public void handle(OrderPlacedEvent event) {
    // safe DB access
}
```
Now:
* listener runs after commit
* Data is guaranteed to exist

### Transactional Event Phases

| Phase            | When                     |
| ---------------- | ------------------------ |
| BEFORE_COMMIT    | Before commit            |
| AFTER_COMMIT     | After commit (most used) |
| AFTER_ROLLBACK   | On rollback              |
| AFTER_COMPLETION | Always                   |

#### Problem 3: Event listener not triggered
```java
@EventListener
public void handle(OrderPlacedEvent event) { }
```
But Nothing Happens.

Common Causes
* Listener bean not managed by Spring
* Wrong event type
* ApplicationContext not started

**Solution checklist**
* Listener annotated with `@Component`
* Event class matches exactly
* Event published after context refresh

### Ordering Event Listener

```java
@EventListener
@Order(1)
public void first(OrderPlacedEvent e) { }

@EventListener
@Order(2)
public void second(OrderPlacedEvent e) { }
```
use only when order matters.

#### When not to use Spring Events
* For Core business flow
* For return values
* For guaranteed execution logic

### Internal Fow
When `publishevent()` is called:
1. ApllicationEventMultiCaster receives event
2. Finds matching listeners
3. Applies ordering
4. Executes listeners (Sync/async)
5. Handles exception

Async:
* wrapped in executor
* Exception NOT propagated to publisher

#### Use Cases
* email notifications
* Audit logging
* Cache eviction
* Metrics
* Search indexing
* Kafka publishing (bridge pattern)

