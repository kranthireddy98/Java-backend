## Microservices with Spring Boot

### What are microservices?
Microservices are an architectural style where:

* An application is split into small, independent services
* Each service:
  * Owns its data
  * Can be deployed independently
  * Communicates via network (HTTP / messaging)

Microservices are about independence, not just splitting code.

### Monolith vs Microservices
Monolith 
```text
Single application
Single database
Single deployment
```
Pros:
* Simple 
* Easy to debug
* Low operational cost

Cons:
* Hard to scale selectively
* Tight coupling
* Risky deployments

MicroServices
```text
Multiple services
Multiple databases
Independent deployments
```
Pros:
* Independent scaling
* Failure isolation
* Faster team autonomy

Cons:
* Network latency
* Distributed complexity
* Operational overhead
Microservices solve organizational problems, not just technical ones.

### When You SHOULD NOT Use Microservices
* small teams
* Early-stage products
* Simple CRUD apps
* No DevOps maturity

### Core Principle of Microservices
These are non-negotiable:
1. Single Responsibility per service
2. Database per service
3. Independent Deployment
4. Failure Isolation
5. Decentralized Data management

If you violate these, you don't have microservices - you have distributed monoliths.

### Database per Service
**Problematic Design (Distributed Monolith)**
```text
User Service
Order Service
Payment Service
     ↓
   Shared DB
```
Problems:
* Tight coupling
* Cross-service joins
* Impossible independent evolution

**Correct Design**
```text
User Service → User DB
Order Service → Order DB
Payment Service → Payment DB
```
Services communicated via:
* REST
* Messaging (Kafka)

### Communication Between Microservices
Two main models:

1. Synchronous (REST)
```text
Service A → HTTP → Service B
```
Pros:
* Simple
* Immediate response

Cons:
* Tight runtime coupling
* Cascading failures

2. Asynchronous (Messaging)
```text
Service A → Kafka → Service B
```
Pros:
* Loose coupling
* Resilient
* Scalable

Cons:
* Eventual consistency
* Complex debugging

Use REST for queries, messaging for events.

### Problem 1: Cascading Failures
```text
Order Service → Payment Service → Notification Service
```
If Payment is down:
* Order fails
* System unusable

**Solution: Failure Isolation**
* Timeout
* Retries
* Circuit breakers
* Async messaging 

A service must fail fast, not hang.

### Configuration Management
Problematic Approach
```text
Hardcoded URLs
Hardcoded credentials
Copied config files
```
Breaks:
* Security
* Maintainability
* Deployments

**Correct Approach**
* Externalized configuration
* Environment-based config
* Central config

Spring Boot already supports:
* Profiles 
* Environment variables
* Config overrides

### Problem 2: Tight Compile-Time Coupling
```java
import com.orderservice.Order;
```
Service A depends on Service B's classes.

**Solution**
* Communicate via:
  * JSON contracts
  * OpenAPI
* Share schemas, not code

### Data Consistency in Microservices
Problem

No Distributed transactions (XA is bad).

**Solution: Eventual Consistency**
```text
Order Created
 → Order Service commits
 → Event published
 → Payment Service reacts
```
No global transaction.

System becomes eventually consistent.

### Problem 3: Everything Must Be Consistent
trying to make:
* All Services transactional
* Strong consistency everywhere

Leads to:
* Complexity
* Deadlocks
* Latency

Strong consistency inside a service

Eventual consistency across services.

### Service Discovery
In real microservices:
* Services scale dynamically
* IPs change
* Hardcoding endpoints fails

Solutions:
* Service registry
* Load balancers
* DNS-based discovery

### Observability Is NOT Optional
In microservices you must have:
* Centralized logging
* Metrics
* Tracing

Without observability:
* You are blind in production

### Problem 4: Chatty Communication
```text
Service A → Service B (10 calls per request)
```
Problems:
* Latency
* Network overhead
* Fragility

**Solutions**
* API aggregation
* Coarser APIs
* Event-driven updates
* Caching

### Transaction Boundaries
In monolith:
```text
One transaction → many operations
```
In microservices:
```text
One transaction → one service only
```

cross-service consistency is not transactional.

### Realistic Spring Boot Microservices Stack
Typical setup:
* Spring Boot (each service)
* REST + Kafka
* Redis cache
* Centralized logging
* API Gateway
* Config management

### Common Microservices Anti-patterns
* Shared database
* chatty REST calls
* Distributed transactions
* Ignoring failure
* Treating microservices like monoliths



























































