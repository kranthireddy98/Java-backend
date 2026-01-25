## Spring MVC Internals

### What is Spring MVC?
spring MVC is Spring's web framework for building HTTP-based applications (REST APIs, web apps).

Its Job is to:
* Accept HTTP request
* Route them to the correct handler
* Convert inputs
* Execute business logic
* Convert outputs to HTTP response

The central brain of Spring MVC is:
`DipatchServlet`

### High-Level Request Flow
```
HTTP Request
 ↓
Servlet Container (Tomcat)
 ↓
DispatcherServlet
 ↓
HandlerMapping
 ↓
HandlerAdapter
 ↓
Controller Method
 ↓
Return Value Handling
 ↓
ViewResolver / HttpMessageConverter
 ↓
HTTP Response
```
Everything in spring MVC fits into this pipeline

### DispatchServlet (The Front Controller)
What it is

`DispatchServlet` is a single entry point for all HTTP requests.

Why it exists
* centralized routing
* Cross-cutting concerns
* Consistent request handling

This is the Front Controller pattern.

Spring Boot:
* Auto-registers DispatcherServlet
* Maps it to `/`

### Step-by-Step Internal Flow

#### step 1: Request Enters Servlet Container
Tomcat receives:
``` 
GET /orders/1
```
Tomcat forwards it to
``` 
DispatchServlet
```

#### Step 2: HanlderMapping(URL -> Controller)
Spring checks HandlerMapping to find:

which controller method should handle this request?

```java
@GetMapping("/orders/{id}")
public Order getOrder(@PathVariable Long id) { }
```

HandlerMapping finds:
* Controller class
* Method
* Path variables
* HTTP methods

No Controller execution yet

#### Step 3: HandlerAdapter (Invoke controller)
controller methods are not simple java methods.

Spring Uses:
`HandlerAdapter`

It:
* Resolves method parameters
* Injects path/query/body/header values
* Calls the method via reflection

### Argument Resolution
Controller parameters like:
```java
@GetMapping("/orders/{id}")
public Order getOrder(
    @PathVariable Long id,
    @RequestParam String status,
    @RequestHeader String token
) { }
```
Are resolved by:

HandlerMethodArgumentResolver

Each annotation has a resolver.

### Problem 1: Parameter Not Resolved
Problematic code
````java
@GetMapping("/orders")
public Order getOrder(Long id) { }
````
Request:
```http request
GET /orders?id=1
```
Result:

id = null

**Solution**
```java
@GetMapping("/orders")
public Order getOrder(@RequestParam Long id) { }
```
Because;
* Spring resolves only explicitly annotated parameters

### Controller Method Execution
Now:
* Business logic runs
* Service layer called
* Data fetched

Controller should
* Coordinate
* Not contain business logic

### Return Value Handling 
Controller return values are handled by:

| Return Type      | Handler                   |
| ---------------- | ------------------------- |
| `String`         | ViewResolver              |
| `ResponseEntity` | HttpEntityMethodProcessor |
| Object           | HttpMessageConverter      |
| `void`           | No content                |

### REST Controllers & Message Conversion

```java
@RestController
public class OrderController {
    @GetMapping("/orders/{id}")
    public Order getOrder() { }
}
```
Flow:
* Return `Order`
* Jackson converts it to JSON
* Writes to response body

This uses:

HTTPMessageConverters

### Problem 2: 415 Unsupported Media Type
problematic Code
```java
@PostMapping("/orders")
public void create(@RequestBody Order order) { }
```
Client sends:
```h
Content-Type: text/plain
```
why it fails 
* No message converter for `text/plain -> order`

**Solution**

Client must send

``` 
Content-Type: application/json
```
Or Configure custom converters.

### Filters vs Interceptors

**Filters (Servlet Level)**
* Part of servlet API
* Executed before DispatcherServlet
* Work on raw request/response

Examples:
* authentication
* Logging
* Compression

**Interceptors (Spring MVC Level)**
* Executed inside Spring MVC
* After DispatcherServlet
* Before controller

Examples:
* Authorization
* Locale handling
* Request context

Execution Order

``` 
Filter
 → DispatcherServlet
   → Interceptor (preHandle)
     → Controller
   → Interceptor (postHandle)
 → Filter
```

### Problem 3: Interceptor not triggering

problematic code
```java
@Component
public class MyInterceptor implements HandlerInterceptor { }
```
but nothing happens

**Solution**
Register it:
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor());
    }
}
```

### Exception Handling Flow
When exception occurs:
* DispatcherServlet catches it
* Delegates too

HandlerExceptionResolver

Includes:
* @ExceptionHandler
* @ControllerAdvice
* Default resolvers

### Problem 4: Exception Returns HTML Instead of JSON

Cause:
* No Global exception handler
* Default error page rendered

**Solution**

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ErrorResponse handle(Exception e) { }
}
```
### Internal MVC Components Summary

| Component          | Responsibility        |
| ------------------ | --------------------- |
| DispatcherServlet  | Front controller      |
| HandlerMapping     | URL → handler         |
| HandlerAdapter     | Invoke controller     |
| ArgumentResolver   | Resolve method params |
| ReturnValueHandler | Process return        |
| MessageConverter   | JSON/XML              |
| ViewResolver       | Views (MVC)           |


