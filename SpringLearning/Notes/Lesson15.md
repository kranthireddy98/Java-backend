## Spring + JPA Internals

### What is JPA in a Spring Application?

JPA (Java Persistence API) is a specification for:
* Mapping Java objects to database tables
* Managing object-relational state
* Handling persistence operations

Spring does not replace JPA

Spring integrates and manages JPA 

In Spring Boot:
* JPA implementation --> Hibernate
* Transaction manager --> `JpaTransactionManager`
* Core runtime object --> EntityManager

### The Most important concept: EntityManager

The EntityManager is the heart of JPA.

it is responsible for:

* Persisting entities
* Tracking entity changes
* Synchronizing state with the database
* Managing the persistence Context

Think of EntityManager as a unit-of-work manager.

### What is Persistence Context?
The Persistence Context is;
*  A first-level cache
* A map of `Entity -> Managed Instance`

Result:
* One Persistence context per transaction (usually)
* Same entity Id -> same java object
* tracks all changes automatically

### Entity LifeCycle States
An entity can be in one of these states:

```text
NEW (Transient)
↓ persist()
MANAGED
↓ commit / flush
DETACHED
↓ remove()
REMOVED
```

Example
``` 
Order order = new Order();   // NEW
entityManager.persist(order); // MANAGED

```

### Why you rarely see EntityManager in Spring
In Spring Boot:
* EntityManager is injected automatically
* Repositories abstract it away
* Transactions bind EntityManager to the thread

So When you write:
```java
orderRepository.save(order);

```
Internally:

```text
Repository → EntityManager → Persistence Context → DB
```

### Automatic Dirty Checking 
What Happens

```java
import org.springframework.transaction.annotation.Transactional;

@Transactional
public void updateOrder(Long id) {
    Order order = repo.findById(id).get();
    
    order.setStaus("Shiiped");
}
```
!No Save() call.

Why it still Updates DB
* Entity is managed
* Persistence Context detects changes
* Changes flushed at transaction commit

This is called:

Dirty checking

### Problem 1: Update Not Saved to DB
Problematic code
```java
public void updateOrder(Long id) {
    Order order = repo.findById(id).get();
    order.setStatus("SHIPPED");
}
```
No transaction -> no persistence context

Result:
* changes lost
* No updates executed

**Solution**
```java
@Transactional
public void updateOrder(Long id) {
    Order order = repo.findById(id).get();
    order.setStatus("SHIPPED");
}
```

### Fetch Types: LAZY vs EAGER
* EAGER -> load immediately
* LAZY -> load on demand

```java
@OneToMany(fetch = FetchType.LAZY)
private List<OrderItem> items;
```

Default
* `@ManyToOne` -> EAGER
* `@OneToMany` -> LAZY

### Problem 2: LAZYInitializationException
Problematic code
```
@GetMapping("/orders/{id}")
public Order getOrder(@PathVariable Long id) {
    return orderService.getOrder(id);
}
```
Service:
```java
public Order getOrder(Long id) {
    return repo.findById(id).get();
}
```
Later:
```java
order.getItems().size(); // 
```
Why it fails:
* Transaction closed
* Persistence Context closed
* Lazy proxy cannot load data

**Solution 1: Fetch Inside Transaction**
````java
@Transactional(readOnly = true)
public Order getOrder(Long id) {
    Order o = repo.findById(id).get();
    o.getItems().size(); // initialize
    return o;
}
````
**Solution 2: Fetch Join(BEST)**
```java
@Query("""
select o from Order o
join fetch o.items
where o.id = :id
""")
Order findWithItems(Long id);
```
### N + 1 Select Problem
Problem
```java
List<Order> orders = repo.findAll();
for (Order o : orders) {
    o.getItems().size();
}
```
```text
1 query → orders
N queries → items
```
This is N+1 problem

**Solution 1: Fetch join**
```java
@Query("""
select distinct o from Order o
join fetch o.items
""")
List<Order> findAllWithItems();
```
**Solution 2: batch Fetching**
```java
hibernate.default_batch_fetch_size=50
```

### Open Session in View (OSIV)
what is OSIV?

OSIV keeps:
* Persistence context open
* until HTTP response is rendered

Enabled by default in spring Boot.

**Problem with OSIV**
* DB access in controller
* Hidden N+1 queries
* Performance issues

Disable OSIV:
```java
spring.jpa.open-in-view=false
```
And :
* fetch everything needed in service layer
* Return DTOs

### Problem 3: Infinite JSON Serialization
```text
Order → Customer → Orders → Customer → ...
```
Result:
* StackOverflowError
* huge payloads

**Solution**
* Use DTOs
* Or `@JsonIgnore`
* Or projection queries

### Read-nly Transactions
```java
@Transactional(readOnly = true)
public List<Order> getOrders() { }
```
Benefits:
* skips dirty checking
* improves performance
* Clear intent

### EntityManager Flush Vs Commit
* Flush --> sync to DB
* Commit -> final transaction end

Flush happens:
* Automatically before commit
* Before certain queries

### Common real world mistakes
* No transaction around entity logic
* Using entities directly in APIs
* ignoring N + 1 issues
* relying on OSIV
* Misusing EAGER fetch

