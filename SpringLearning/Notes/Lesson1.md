# Why Spring Was Created & What Problem it Solves

### 1. The World Before Spring 

#### Typical Java Code (No Spring)

```java
class OrderService {
    private OrderRespository repo = new OrderRespository();
    
    public void placeOrder()
    {
        repo.save();
    }
}
```

### What's Wrong here

| Problem        | Explanation                                      |
| -------------- | ------------------------------------------------ |
| Tight Coupling | `OrderService` **creates** `OrderRepository`     |
| Hard to test   | You cannot replace `OrderRepository` with a mock |
| Hard to change | Switching DB → file → API means code change      |
| No flexibility | Business logic tied to implementation            |

### Key issue
* Class depend on concrete implementations

## 2. The core problem Spring Solves
* Traditional Java
  * Object create other Objects
  * High coupling
  * Low Flexibility
* Spring Approach
  * Object do NOT create dependencies
  * Dependencies are given to them
### This is called 
* Inversion of Control (IoC) 

## 3. What is Inversion of Control (IoC)?
### Normal control
``` 
    Your code → controls object creation
```
### Inversion of Control
``` 
    Spring container → controls object creation
```
## 4. Dependency Injection - How IoC Happens
```java
class OrderService
{
    private final OrderRepository repo;
    public OrderService(OrderService repo)
    {
        this.repo = repo;
    }
}
```
### OrderService does not know:
* Which implementation
* How it's created
* How long it lives
#### --> Spring injects the dependency at the runtime

## 5. Why this is a big deal 
* Easy unit testing
* Repace implementations without touching business code
* cleaner architecture
* Scales well in the large teams

