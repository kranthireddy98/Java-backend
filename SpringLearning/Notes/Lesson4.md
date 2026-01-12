# Spring Stereotype

Stereotypes are specialized annotation used to define the role of a class
within the application architecture.

**When a class is annotated with a stereotype:**
* Spring's component scanning detects it 
* Spring creates an instance (bean)
* The bean is registered in the ApplicationContext
* The bean's role is recorded as metadata

## Why Stereotypes exists
While all stereotypes ultimately register beans, they exist to:
* Separate code into logical layers 
* Enable layer-specific framework behaviour
* allow Spring and its tooling to treat different layers differently
* Improve readability, maintainability and test isolation.

Without stereotypes, Spring would have no reliable way to distinguish:
* business logic from utilities
* database code from services
* web endpoints from internal classes

## The 4 primary Spring Stereotypes
1. `@Component`
2. `@Controller`
3. `@Service`
4. `@Repository`

### `@Component`
Role :

Generic component with no specific architectural meaning

Purpose :

Used for classes that:
* Should be managed by spring
* Do not clearly belong too service, persistence,  or web layers
Common use Cases:

* Utility classes
* Helper classes
* Formatters
* Validators
* Adapters

```java
@Component
public class FileChecksumUtil {
}

```

### `@Service`
Role :

Marks a class as part of the service (business logic) layer.

Purpose:

used for the classes that:

* contains business rules
* coordinate multiple repositories
* define transactional boundaries
* Represent application use cases

```java
@Service
public class OrderService {
    public void placeOrder() {
        // business logic
    }
}

```
Important Note:
`@Service` does not add special runtime behaviour by itself.

its value is semantic and architectural:

* Acts as stable boundary for transactions
* common target for AOP (logging, security, retries)
* Signals "business logic lives here"

### `@Repository`
Role 

Marks a class as part of the persistence (data Access) layer.

Purpose

used for:

* DAOs
* Repositories
* Classes interacting with databases via JDBC, JPA, Hibernate

```java
@Repository
public class OrderRepository {
    public void save(Order order) {
        // database access
    }
}

```
Special Behaviour: Exception Translation
`@Repository` enables automatic exception translation.

What is this means:

* Low-level database exceptions (`SQLException`)
* Are converted into Spring's unchecked `DataAccessException` hierarchy.
* This happens through Spring’s internal `PersistenceExceptionTranslationPostProcessor`.

**Why this matter**
* Database-independent exception handling
* Cleaner service layer code
* No Vendor-specific exception leakage

### `@Controller`

Role 

Marks a class as a web controller in spring MVC

Purpose

Used for classes that:
* handle HTTP requests
* Act as entry points to the application
* Return views or data

```java
@Controller
public class HomeController {

    @RequestMapping("/home")
    public String home() {
        return "home";
    }
}

```

***Framework Behaviour***
* Detected by `DispatcherServlet`
* Scanned for `@RequestMapping` methods
* Integrated into spring MVC request handling flow

### Key Specialized variations

### `@RestController`

What it is

A Combination of
```
@Controller + @ResponseBody
```

Purpose

used for REST APIs where:

* Methods return data (JSON/XML)
* No View resolution is needed

```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/users")
    public List<user> getUser() {
        return List.of();
    }
}
```


### `@Configuration`

Role

Marks a class as a source bean definitions.

Purpose:

used to:

* Define beans using `@Bean` methods
* Replace XML Configuration
* Enable full java based configuration

```java
@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        return new DataSource();
    }
}

```
