## Spring Profiles & Environment (Deep Dive)

### What is the spring Environment?

The Spring Environment is an abstraction that represents external configuration of an 
application.

It answers questions like:
* which environment am i running in?
* Which properties should be applied?
* Which beans should be created?

The Environment consists of:
* profile
* Property sources

### Why Environment & Profile Exist
without profiles:
```text
Hardcoded URLS
Hardcoded credentials
Manual code changes per environment
```

problems:
* unsafe deployments
* Accidental prod DB Usage
* Configuration drift
* Non-repeatable builds

Spring solves this by separating configuration from code

### What is Spring Profile?
A profile is a named logical group of configuration that can be activated or deactivated.

```text
dev
test
qa
prod
local
```
When a profile is active:
* certain beans load
* Certain properties apply
* Others are ignored

### Activating Profiles (All ways)
1. application.properties
```
spring.profiles.active = dev
```
2. JVM argument
```
-Dspring.profiles.active=prod
```
3. command line
``` 
--spring.profiles.active=test
```
4. Environment variable
``` 
SPRING_PROFILES_ACTIVE =prod 
```

Order matters - later sources override earlier ones.

### Profile-Specific Property Files
Spring Boot automatically load:
``` 
application.properties
application-{profile}.properties
```
Example
```text
application-dev.properties
application-prod.properties
```
Resolution rule:
* Common properties first
* Profile-specific overrides later


### problem 1: Wrong Database Used in production
Problematic Setup
``` 
# application.properties
spring.datasource.url=jdbc:h2:mem:testdb
```
No profile -- same DB everywhere

Correct setup
```text
# application-dev.properties
spring.datasource.url=jdbc:h2:mem:testdb
```
```text
# application-prod.properties
spring.datasource.url=jdbc:mysql://prod-db/app
```
And activate profile explicitly

### `@Profile` on Beans
`@Profile` controls bean creation, not property loading

```java
@Bean
@Profile("dev")
public DataSource devDataSource() { }
```
```java
@Bean
@Profile("prod")
public DataSource prodDataSource() { }
```
Only one will exist at runtime

### problem 2: Multiple Beans Loaded Accidentally
```java
@Bean
public DataSource ds1() { }

@Bean
@Profile("dev")
public DataSource ds2() { }
```
Active profile: `dev`

Result:
* Two Datasource beans conflict

Solution

Always guard all variants

```java
@Bean
@Profile("!prod")
public DataSource devDataSource() { }

@Bean
@Profile("prod")
public DataSource prodDataSource() { }
```
### Profile Expression
Spring supports logical expression
``` 
@Profile({"dev","test"})
@Profile("!prod")
@Profile("cloud & prod")
```
Used in:
* Multi-cloud deployments
* Feature toggles

### The Environmental Abstraction
Spring exposes `Environment` API:
```java
@Autowired
private Environment env;
```
usage:
```java
env.getProperty("spring.datasource.url");
env.acceptsProfiles("prod");
```
Use sparingly -- prefer injection.

### Property Resolution Order
highest priority wins:
1. Command-line arguments
2. JVM System properties
3. OS system properties
4. Profile-specific files
5. application.properties
6. Default values

This explains may why is my value ignored? bugs.

### Problem 3; Property Value not applied
``` 
server.port = 8081
```
But app runs on 8080.

Why?

* Command-line arg overrides
``` 
--server.port=808
```

Solution 

Check effective configuration, not file content.

Use;
``` 
debug =true
```

### `@Value` vs `@ConfigurationProperies`
Problematic Pattern

```java
import org.springframework.beans.factory.annotation.Value;

@Value("${db.url}")
private String url;
```
Issues:
* Scattered config
* No validation
* hard to test

Recommended Pattern
```java
@ConfigurationProperties(prefix="db")
public class DbProperties {
    private String url;
    private String user;
}
```
Benefits:
* Type-safe
* Centralized
* Validatable

### Profiles vs Build Tools

Profiles are runtime concepts, not build-time

Wrong thinking:

maven profile = Spring profile

They are different

Spring profiles:
* runtime behaviour

Maven profiles:
* Build-time behaviour

### internal flows

At startup;
1. Environment created
2. PROPERTY Sources load
3. Active profiles resolved
4. Bean definitions evaluated against profiles
5. Beans created or skipped

Profiles affect bean definition stage, not instantiation.


### Common Real-World Mistakes
* Forgetting to activate profile in prod
* Relying on default profile
* Mixing secrets in application.properties
* Using `@Value` everywhere
* Assuming profile = environment automatically

### Mental Model

profiles decide WHAT exists.

Properties decide HOW it is configured.

