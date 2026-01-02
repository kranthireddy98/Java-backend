## 1. What is Spring Container?
The Spring Container is responsible for creating, configuring, wiring, and managing 
the lifecycle of beans.

Spring decides how to create it, when to create it, and how long it lives.


## Types of Containers
1. BeanFactory
   * Basic and lightweight
2. ApplicationContext
   * Advanced, Enterprise-ready

## BeanFactory
### characteristics
* Lazy initialization 
* Minimal features
* Used in rarely in modern apps
```java
    BeanFactory factory = new ClassPathXmlApplicationContext("beans.xml");
```
## Application Context
### Features
* Eager bean Initialization
* Dependency injection
* Bean lifecycle management
* Event publishing
* AOP support
* Internationalization
```java
    @SpringBootApplication
    public class App {
        public static void main(String[] args) {
            SpringApplication.run(App.class, args);
        }
    }
```

## 2. What is Bean?
--> Bean is object managed by the Spring container.
```java
    @Component
    class OrderService{
    
}
```
This class becomes:
* Managed by the spring
* Stored in the container
* injected wherever needed

## 3. How Spring knows What to Create (Bean Definition)
### Spring Creates Bean Definition using:
* @Component
* @Service
* @Repository
* @Configuration
* XML (Old)

## Internal View (Conceptual)
```
    BeanDefinition
     ├── Class name
     ├── Scope
     ├── Dependencies
     ├── Init method
     └── Destroy method
```
## Bean Lifecycle (High -level)
```
    1. Read configuration
    2. Create bean
    3. Inject dependencies
    4. Call init methods
    5. Bean ready to use
    6. On shutdown → destroy
  
```

## Dependency Resolution (How Injection Works)
```
    @Service
    class OrderService {
        private final OrderRepository repo;
    
        public OrderService(OrderRepository repo) {
            this.repo = repo;
        }
    }

```
### Spring:
1. Sees @Service
2. Sees Constructor dependency
3. Finds OrderRepository
4. Creates repository first
5. Injects into Service

