## Advanced Scope Handling in Spring


 The Core problem this lesson solves
 
Spring application commonly have this situation:
* Singleton beans (service, controllers)
* Non-singleton beans (prototype, request, session)

Problem :

Spring injects dependencies only once, at bean creation time.

So when a singleton depends on a prototype, the prototype is created once and reused -- which defeats the purpose of prototype scope.


The Bug :

```java
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ReportGenerator {

}
```

```java
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private ReportGenerator reportGenerator;
}

```
Expectation:

* New `ReportGenerator` for every use

Actually Happens:

* one `Reportgenerator` created
* Same instance reused forever

why?

Because:

* `OrderService` is singleton
* Dependencies are injected once at startup


Solution Strategies (What Spring Provides)

Spring provides four official solutions:

1. ObjectProvider
2. ObjectFactory
3. @Lookup
4. Scoped proxies

#### ObjectProvider

what it is

`ObjectProvider<T>` is a lazy, safe way to fetch beans on demand.

```java

import org.springframework.beans.factory.ObjectProvider;

public class OrderService {
    private final ObjectProvider<ReportGenerator> provider;

    public OrderService(objectProvider<ReportGenerator> provider)
    {
        this.provider = provider;
    }
    
    public void generateReport(){
        ReportGenerator rg = provider.getObject();
    }
}
```

What Happens Internally:

* Spring injects the provider
* Provider fetches a new prototype instance each time
* Dependency resolution happens at method call time

Why it's preferred
* No reflection
* Type-safe
* Clean
* Explicit

#### ObjectFactory

What it is 

A simpler predecessor of ObjectProvider

```java
import javax.naming.spi.ObjectFactory;

private ObjectFactory<ReportGenerator> factory;

ReportGenerator rg = factory.getObject();
```

Difference from ObjectProvider

| Feature          | ObjectFactory | ObjectProvider |
| ---------------- | ------------- | -------------- |
| Optional support | ❌             | ✅              |
| Stream support   | ❌             | ✅              |
| Recommended      | ❌             | ✅              |

* use `ObjectProvider` unless maintaining Legacy code

#### `@Lookup`

What it is

`@Lookup` tells Spring :

Override this method and return a new bean every time.

```java

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@Service
public abstract class OrderService {

    @Lookup
    protected abstract ReportGenerator getReportGenerator();
    
    public void process(){
        ReportGeneratort rg = getReportGenerator();
    }
}
```

How it works internally
* Spring creates a CGLIB proxy
* Overrides the method at runtime
* Returns a fresh bean per call

Downsides:
* Uses runtime bytecode enhancement
* Less readable
* Harder to debug

When to use:
* Legacy Systems
* When you cannot change constructor injection

#### Scoped Proxies (Web & Stateful Beans)

What it solves

used when:
* Injecting request/session scoped beans
* into singleton beans

```java
@Component
@Scope(
  value = WebApplicationContext.SCOPE_REQUEST,
  proxyMode = ScopedProxyMode.TARGET_CLASS
)
public class RequestContext {
}

```

Injected into:
```java
@Service
public class OrderService {

    @Autowired
    private RequestContext context;
}

```

How it works:
* Spring injects a proxy
* Proxy delegates to correct scoped instance at runtime

Real World use
* HTTP request metadata
* User context
* Correlation IDs

**Comparison table**

| Technique      | Best For               | Recommended |
| -------------- | ---------------------- | ----------- |
| ObjectProvider | Prototype in singleton | ✅ YES       |
| ObjectFactory  | Legacy code            | ⚠️          |
| @Lookup        | Framework-level code   | ⚠️          |
| Scoped Proxy   | Web scopes             | ✅ YES       |



