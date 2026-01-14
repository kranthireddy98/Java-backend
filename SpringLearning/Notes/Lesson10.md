# Auto-Configuration

### 1. What is Auto-Configuration?

Auto-Configuration is Spring Boot's mechanism to:

Automatically configure Spring beans based on the classpath, existing beans, and application properties.
* you add dependency
* Spring Boot detects it
* Spring Boot configures it
* You override only when needed

### 2. Why Auto-configuration Exists?

Before Spring Boot:
``` 
XML Config
Java Config
Bean Config
Environment-specifc setup
```

Problems:
* Too much boilerplate
* Error-prone
* Slow startup of projects
* Difficult onboarding

Spring Boot solved this by saying:

We will make smart defaults, but never lock you in.


### 3. The core Annotation `@SpringBootApplication`

```java
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyApp {

}
```
This annotation is NOT a single thing.

Internally it is:

```java
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
```

### 4. `@EnableAutoConfiguration` (The Heart)

This Annotation tells Spring Boot:

Look at the classpath and automatically apply configuration classes.

This is where auto-Configuration actually starts

### 5. Where Auto-Configuration classes come from?

This is important internal detail.

Spring Boot Scans
```text
META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports

```

Older Versions used spring.factories

This file contains:
```text
org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
...
```
Each Entry is a normal `@Configuration` class.

Spring does not generate beans magically

it imports configuration classes conditionally.

### 6. how Auto-Configuration Decides to Activate (Conditions)
Every auto-Configuration class is guarded by conditions.

Example:

```java

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

@AutoConfiguration
@ConditionalOnClass(DataSource.class)
@ConditionalOnMissingBean(DataSource.class)
public class DataSourceAutoConfiguration {

}
```

This Means:
* Activate only if `DataSorce` is on the classpath
* Activate only if user hasn't defined one.

### 7. Most Important Condition Annotations

| Condition                      | Meaning                   |
| ------------------------------ | ------------------------- |
| `@ConditionalOnClass`          | Class exists on classpath |
| `@ConditionalOnMissingBean`    | No user-defined bean      |
| `@ConditionalOnProperty`       | Property is set           |
| `@ConditionalOnWebApplication` | Web environment           |
| `@ConditionalOnBean`           | Another bean exists       |

These Conditions are the rules engine of Spring Boot.

### 8. Example: DataSource Auto-Configuration

What you do
```xml
<dependecy>
    spring-boot-starter-jdbc
</dependecy>
```
What Spring Boot Sees
* JDBC on classpath
* No DataSource bean defined
* DB properties present

What Spring Boot Does
* Creates DataSource
* Configures connection pool
* Wore JdbcTemplate

You Wrote Zero config code.

### 9. Problem 1: Why is MY Bean Not Being Created?

Scenario

you defined your own bean:

```java

import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@Bean
public DataSource dataSource() {
    return new CustomeDataSource();
}
```
Auto-configured DatSource disappears.

Why?

Because:
```
@ConditionalOnMissingBean(DataSource.class)
 ```

Spring Boot backs off when you define your own bean.

This is Called:

Auto-Configuration back-off

Spring Boot Rule:

user configuration always wins.


#### Problem 2: Auto-Configuration Runs but App Fails

you add:
```xml
spring-boot-starter-data-jpa
```
But forget DB properties.

Result:
``` Failed to configure a DataSource```

Why ?

Conditions passed:
* JPA on class path
* Web App detected

But:
* No DB URL

Auto-Configuration starts but cannot complete.

**Solution**
```properties
spring.datasource.url=jdbc:mysql://localhost/db
spring.datasource.username=root
spring.datasource.password=secret

```

OR disable auto-config
```java
@SpringBootApplication(
  exclude = DataSourceAutoConfiguration.class
)

```
#### Problem 3 : Unexpected Auto-Configuration

Example:

You include:
```xml
spring-boot-starter-web
```
suddenly:
* Embedded Tomcat starts
* DispatcherServlet created

Why?

Because:

```java
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;

@ConditionalOnWebApplication
```

**Solution (Disable Selectively)**
```properties
spring.main.web-application-type=none
```
OR exclude auto-config class.

#### Debugging Auto-configuration 

Enable Conditional Report
```properties
debug=true
```

Startup log shows

```text
Positive matches
Negative matches
```
This is called:

Condition Evaluation Report

It tells:

Why a config was applied

Why it was skipped


### 10. Internal Execution Order (Deep)
At Startup:
1. ApplicationContext Created
2. Auto-Configuration classes imported
3. Conditions evaluated
4. Beans registered
5. user beans override auto-beans
6. Context refreshed

Auto-Configuration happens before your beans are fully initialized.

