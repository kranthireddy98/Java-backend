# Autowiring

## What is Autowiring?
* Autowiring is the mechanism by which Spring automatically resolves and injects bean dependencies into
other beans at runtime.

Instead of manually wiring beans, Spring: 
* Inspects the dependency
* Searches the ApplicationContext
* Selects the appropriate bean
* Injects it

## When Does Autowiring Happen?

Autowiring occurs during the bean creation phase, specially:
* After bean instantiation
* Before initialization callbacks (`@postConstruct`)

At this Stage:
* Bean Exists 
* Dependencies are still unresolved
* Spring performs injection

## How Spring Resolves a Dependency
````java
@Autowired
private PaymentGateway gateway;

````

Spring follows this resolution algorithm:
1. Type matching 
2. Qualifier
3. Primary bean selection
4. Name matching
5. Error if unresolved

### 1. Resolution by type

```java
import org.springframework.stereotype.Component;

interface PaymentGateway {
}

@Component
class PaytmGateway implements PaymentGateway {
}
```
* Single match injected
* Multiple implementations of PaymentGateWay --> Ambiguity error

### 2. multiple beans of same type

```java
import org.springframework.stereotype.Component;

interface PaymentGateway {
}

@Component
class PaytmGateway implements PaymentGateway {
}

@Component
class StripeGateway implements PaymentGateway {
}
```
Error:
`NoUniqueBeanDefinitionException`

### 3. `@Primary` --> Default choice

```java
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
class PaytmGateway implements PaymentGateway {
}
```
* Spring will inject `PytmGateWay`
Rule:
* Only one bean should be `@Primary`
* Acts as default choice

### 4. `@Qualifier` --> Explicit choice

```java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Autowired
@Qualifier("stripGateway")
private PaymentGateWay gateWay;
```
* Spring injects Bean with matching Qualifier name

Important

* `@Qualifier` overrides `@primary`


### Name-Based Resolution (Last Attempt)
If no Qualifier or primary
* Spring tries matching field name
* Matches bean name

```java
import org.springframework.beans.factory.annotation.Autowired;

@Autowired
private PaymentGateway stripeGateway;
```

* Spring matches `stripeGateway`

## Optional Dependencies

1. Using `required = false`

```java
import org.springframework.beans.factory.annotation.Autowired;

@Autowired(required = false)
private AuditService auditService;
```
* Bean may or may not exist
* Field becomes `null` if missing

2. Using `Optional<T>` (Recommended)
```java
public OrderServie (Optional<AuditService> auditService)
{
    
}
```
* null safe
* Cleaner design


## Autowiring Without `@Autowired` Spring4.3+

Constructor injection Shortcut

````java
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository repo;
    
    public OrderService (OrderRepository repo)
    {
        this.repo = repo;
    }
}
````

* `@Autowired` is optional
* Only if single  constructor

## Autowiring Collections

Spring can inject all beans of a type

```java

import org.springframework.beans.factory.annotation.Autowired;

@Autowired
private List<PaymentGateway> gateways;

```

Result
* All `PaymentGateway` implementations injected

Also Supported
* Set<T>
* Map<String,T> (bean name -> instance)


## Autowiring and Bean Scope

| Scope     | Autowiring Behavior               |
| --------- | --------------------------------- |
| Singleton | Same instance injected everywhere |
| Prototype | New instance per injection        |
| Request   | One per HTTP request              |

## Common pitfalls
❌ Multiple beans, no qualifier
❌ Field injection in production code
❌ Circular dependencies
❌ Relying on bean names


### Autowiring Summary

* Spring resolves dependencies by type first
* @Primary provides a default
* @Qualifier provides precision
* Constructor injection is preferred
* Avoid field injection