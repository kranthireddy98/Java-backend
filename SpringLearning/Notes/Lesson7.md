## What is Bean Scope?
A bean scope defines how long a Spring bean lives and how many instances of that bean Spring creates.

In simple terms, Scope Answers:

* How many objects?
* When are they created?
* When are they destroyed?
* who shares them?

Spring manages these rules inside the ApplicationContext.

### Why Bean Scope Matter
Choosing the wrong scope can lead to:
* Memory leaks
* Thread-safety issues
* Unexpected shared state
* Performance problems

In Real applications, most bugs related to Spring beans are scope-relateed

#### Default Scope : Singleton

Definition;

Only one instance of the bean is created per Spring container.

Default Behavior
* Created at application startup (eager)
* same instance shared everywhere

```java

import org.springframework.stereotype.Component;

@Component
public class UserService {

}
```
Spring Internally
``` 
ApplicationContext
|___ UserService (one instance)
```

Key Characteristics:
* Default Scope
* Thread-safe only if bean is stateless
* Destroyed when container shuts down

When to use:
* Services
* Repository
* Stateless components
* Configuration

#### Prototype Scope

Definition:
A new instance is created every time the bean is requested.

```java
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ReportGenerator {

}
```

Each injection or getBean() call:
``` 
new ReportGenerator()
```

Key Characteristics
* Not managed after creation
* Spring does not call destroy methods
* client code responsible for cleanup

Important Limitations:

If a prototype bean is injected into a singleton:
* it is created once
* then behaves like singleton

When to use :
* Stateful objects
* Temporary processing logic:
* Per-use data holders

### Web-aware Scopes (Spring MVC)

These scope exists only in web applications.

**`Request` scope**

Definition:

One Bean instance per HTTP request:

```java
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value ="WebApplicationContext.SCOPE_REQUEST")
public class RequestContext {

}
```
Characteristics:
* Created at request start
* Destroyed after response
* Safe for Request-specific data

Common Use:
* Request metadata 
* Correlation IDs
* Per-request caches

**`Session` Scope**

Definition:

One bean instance per HTTP Session

```java
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class UserSession {

}
```

Characteristics
* Lives across multiple requests
* Destroyed when session expires
* Not Suitable for large Objects

Common Use:
* Logged-in user info
* Session preferences

**`application` Scope**

Definition:
* One Bean across all sessions
* Similar to singleton but web-bound

Bean Summary Table

| Scope       | Instances     | Lifetime     | Typical Usage   |
| ----------- | ------------- | ------------ | --------------- |
| singleton   | 1             | App lifetime | Services, repos |
| prototype   | Many          | Until GC     | Stateful logic  |
| request     | 1 per request | HTTP request | Request data    |
| session     | 1 per session | HTTP session | User state      |
| application | 1 per app     | App lifetime | Shared web data |


Scope & Thread Safety

Singleton Scope
* Shared across threads
* Must be stateless
* Mutable fields 

Prototype/ Request / Session
* Safer for state
* Higher memory usage


**Injecting Non-Singleton into singleton

```java

import org.springframework.stereotype.Component;

@Component
public class OrderService {

    private ReportGenerator reportGenerator; // prototype
}
```

Result:
* prototype created once
* Acts like singleton

Solution:
* objectprovider
* @Lookup

Lifecycle Callbacks Vs Scope

| Scope     | Init Called | Destroy Called |
| --------- | ----------- | -------------- |
| singleton | ✅           | ✅              |
| prototype | ✅           | ❌              |
| request   | ✅           | ✅              |
| session   | ✅           | ✅              |


Summary:
* singleton is default and most common 
* prototype is not fully managed
* Web Scopes exists only in web apps
* Scope affects lifecycle and thread safety
* Wrong scope choice causes production bugs

