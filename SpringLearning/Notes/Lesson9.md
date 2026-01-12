# Configuration

In Spring, configuration is the process of defining how beans are created and wired.

Spring supports multiple configuration styles:
* XML
* Java-based configuration

Java-based configuration revolves around:
* `@Configuration`
* `@Bean`


### What is @configuration?

`@Configuration` marks a class a source of bean definitions.

It tells Spring:

This class contains factory methods (`@bean`) that define how objects should be created.

```java

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        return new DataSource();
    }
}
```
Spring:
* Detects the class via component scanning.
* Enhances it sing CGLIB
* Ensures correct bean lifecycle and singleton behaviour.
* Executes `@Bean` Methods
* Registers returned objects as beans

### What is `Bean` ?
`@Bean` is a method-level annotation.

it tells Spring:

The object returned by this method is a spring bean.

```java
import org.springframework.context.annotation.Bean;

@Bean
public OrderService orderService() {
    return new OrderService();
}
```
Important:
* Method name = bean name (by default)
* Method body controls object creation;
* Spring manages lifecycle of returned object.

### `@Configuration` Is Not Just `@Component`

At first glance:
```java
@Configuration
public class AppConfig { }

```
looks same as
```java
@Component
public class AppConfig { }

```
they are not the same.


### `@Configuration` (CGLIB Proxy)

When spring sees `@Configuration`, it:
* Creates a CGLIB subclass of the config class
* Overrides `@Bean` methods
* Intercepts method calls
* Returns the same singleton bean from the container

This ensures:

Even if you call `@Bean` method multiple times, you still get the same bean instance.

This behaviour is called:
**Full configuration Mode**


#### Configuration class

```java

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Engine engine() {
        return new Engine();
    }
    
    @Bean
    public Car car(){
        return  new Car(engine());
    }
}
```
Spring Ensures:
* `engine()` is intercepted 
* Spring returns the managed singleton
* No duplicate objects

### What happens without `@Configuration`

```java
@Component   //  NOT @Configuration
public class AppConfig {

    @Bean
    public Engine engine() {
        return new Engine();
    }

    @Bean
    public Car car() {
        return new Car(engine()); //  new Engine()
    }
}

```
What Goes Wrong
* engine() is plain java method
* Called directly
* Creates new Engine()
* Spring cannot intercept it

Result:
```
Engine #1 (registered bean)
Engine #2 (inside Car)

```
This breaks :
* Singleton guarantee
* Memory usage
* Expected wiring

**Correct Solution**

```java
import org.springframework.context.annotation.Configuration;

@Configuration   //   @Configuration
public class AppConfig {

    @Bean
    public Engine engine() {
        return new Engine();
    }

    @Bean
    public Car car() {
        return new Car(engine()); 
    }
}

```

### Full Vs Lite Configuration Mode

| Mode | Annotation             | Proxying   |
| ---- | ---------------------- | ---------- |
| Full | `@Configuration`       | ✅ CGLIB    |
| Lite | `@Component` + `@Bean` | ❌ No proxy |

Rule -- > if `@Bean` methods call each other --> use `@Configuration`

### `ProxyBeanMethods`

Spring added a performance optimization:

```java
@Configuration(proxyBeanMethods = false)
public class AppConfig {
}

```
What this means:
* Disables CGLIB proxy
* `@Bean` methods behave like normal methods
* Faster startup

#### Using `proxyBeanMethods=false` Incorrectly

Problematic Code

```java

import org.apache.catalina.Engine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

    @Bean
    public Engine engine() {
        return new Engine();
    }
    
    @Bean
    public Car car(){
        return new Car(engine());
    }
}
```
Issue:
* same issue as `@Component`
* Multiple instances created

**Correct Usage**
```java

import org.apache.catalina.Engine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

    @Bean
    public Engine engine() {
        return new Engine();
    }
    
    @Bean
    public Car car(Engine engine){ // Dependency injection
        return new Car(engine);
    }
}
```
Now:
* No method calls between beans
* Dependencies injected by spring
* Safe + performant

### When should you use `@Bean`
use `@Bean` when:
* you don't control the class
* Complex construction logic is needed
* Conditional creation is required
* Configuration-based wiring is clearer

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Bean
public ObjectMapper objectMapper() {
    return JsonMapper.builder()
            .findAndAddModules()
            .build();
}
```

### `@Bean` Lifecycle & Scope
* Default scope : singleton
* Fully lifecycle-managed
* Supports:
  * `@PostConstruct`
  * `@PreDestroy`
  * Custom int/destroy methods

```java
@Bean(initMethod = "start", destroyMethod = "stop")
public Server server() {
    return new Server();
}

```

### Summary
* `@Configuration` enables method interception
* CGLIB ensures singleton correctness
* `@Bean` methods are factory methods
* `ProxyBeanMethods=false` is an optimization, not default
* Wrong configuration causes silent, dangerous bugs

