# Bean Lifecycle Flow
1. Bean definition loaded
2. Bean instantiated
3. Dependencies injected
4. Aware interfaces
5. BeanPostProcessor (before init)
6. Init methods
7. BeanPostProcessor (After init)
8. Bean ready to use
9. Container shutdown
10. Destroy methods

# Why Bean Lifecycle Matters
1. You open DB connections
2. Initialize caches
3. Load configuration
4. Release resources on shutdown
## Bean lifecycle hooks tell spring:
1. What to do after creation
2. What to do before destruction

## Dependency Injection Vs Bean Instantiation
Instantiation
```java
    new OrderService();
```
Dependency Injection;

```java
    new OrderService(orderRepository);
```
# LifeCycle Callback Options

### 1. @PostConstruct
```java
    @component
    class CacheLoader{
    @postconstruct
    public void init()
    {
        System.out.println("cache initialized");
    }
}
```
* Called After dependency injection
* Clean & recommended
* JSR-250 standard

### 2. @PreDestroy

```java
    @PreDestroy
    public void destroy()
    {
        System.out.println("Cleanup before shutdown");
    }

```
* Called when container shuts down
* used to close resources

## InitializingBean & DisposableBean
* Tightly Coupled to spring
* Less preferred in modern apps
```java
@Component
class MyBean implements InitializingBean, DisposableBean {

    public void afterPropertiesSet() {
        System.out.println("Init");
    }

    public void destroy() {
        System.out.println("Destroy");
    }
}

```
## Custom Init & Destroy Methods
* Useful when you don't own the class
```java
@Bean(initMethod = "start", destroyMethod = "stop")
public Server server() {
    return new Server();
}

```

## BeanPostProcessor
Used to modify beans before and after initialization

Spring internally uses this for:
* @AutoWired
* @Transactional
* @Async
* AOP proxies

## What happens on Application Shutdown?
Spring
1. Calls @PreDestroy
2. calls destroy methods
3. release resources
4. JVM exits