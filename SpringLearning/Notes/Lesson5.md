# Dependency Injection

### What is Dependency Injection (DI)

Dependency Injection is a design pattern where an object's dependencies are provided externally
rather than the object creating them itself.

In Spring:

* The Spring Container creates objects 
* it injects required dependencies
* The Object focuses only on it's logic 

This Achieves loose coupling, testability and maintainability.

#### Why DI is necessary
Without DI
```java
class OrderService{
    private OrderRepository repo = new OrderRepository();
    
}
```

Problems:
* Tight coupling
* hard to test
* hard to replace implementation
* Violates Single Responsibility principle

With DI
```java
class OrderService {
    private final OrderRepository repo;

    OrderService(OrderRepository repo) {
        this.repo = repo;
    }
}

```
The class
* Does not control creation
* Depends on abstraction
* is easy to test and extend

## Types of Dependency Injections in Spring
Spring supports three types of DI:

1. Constructor Injection
2. Setter Injection
3. Field Injection

### 1. Constructor Injection

Dependencies are provided through the constructor

```java
@Service
public class OrderService {

    private final OrderRepository repo;

    public OrderService(OrderRepository repo) {
        this.repo = repo;
    }
}

```

Key Characteristics
* Dependency is mandatory
* Object is always in a valid state
* Supports final fields
* Preferred

Internal Behaviour

* Spring resolves dependencies before creating the bean
* if dependency is missing --> Application fails at startup

### 2. Setter Injection

Dependencies are injected through setter methods.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private EmailClient emailClient;

    @Autowired
    public void setEmailClient(EmailClient emailClient) {
        this.emailClient = emailClient;
    }
}
```

Key Characteristics

* Dependency is optional
* Bean can exist without dependency
* Allows configuration after creation


Internal Behaviour

* Bean is created first
* Dependencies injected later via setter

### 3. Field Injection
Dependencies are injected directly into fields.

```java
@Service
public class PaymentService {

    @Autowired
    private PaymentGateway gateway;
}

```
Key Characteristics
* No Constructor
* Uses reflection
* Hidden dependencies

Problems

* Cannot use Final
* Hard to unit test
* violates immutability
* Not recommended


### What is Circular dependency

```
A → depends on B  
B → depends on A
```
```java
@Service
class A {
    A(B b) {}
}

@Service
class B {
    B(A a) {}
}

```

| Injection Type | Circular Dependency |
| -------------- | ------------------- |
| Constructor    | ❌ Not supported     |
| Setter         | ✅ Supported         |
| Field          | ✅ Supported         |


### Optional Dependencies
Using `@Autowired(required=false )`
```java
@Autowired(required = false)
private AuditService auditService;

```
Using `Optinal<T>`
```java
public OrderService(Optional<AuditService> auditService) {
}

```

### Multiple Implementations & `@Qualifier`

```java
interface PaymentGateway { }

@Component
class PaytmGateway implements PaymentGateway { }

@Component
class StripeGateway implements PaymentGateway { }

NoUniqueBeanDefinitionException;
```
```java
@Autowired
@Qualifier("paytmGateway")
private PaymentGateway gateway;

```