## SpringApplication

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
      ApplicationContext contex=  SpringApplication.run(Main.class,args);

        Human human = contex.getBean(Human.class);

        human.code();
    }
}
```

* SpringApplication.run returns an ConfigurableApplicationContext which extends ApplicationContext
* ApplicationContext.getBean(class Name)  -> returns the bean
* @Component on Class makes the class as Spring managed bean

```java
package com.springLearn;

import org.springframework.stereotype.Component;

@Component
public class Human {
    public void code()
    {
        System.out.println("i am coding");
    }
}

```


## Spring container

* spring creates object even if the object is not used(single ton)
* getBean() --> returns object from the container
* Spring returns same object every time (Singleton)
```xml
<bean id="computer" class="com.SpringFrameIntro.Computer" scope="singleton">
   <!-- default values-->
    <property name="model" value="2025"></property>
    <!--For reference types use ref not value-->
    <property name="laptop" ref="laptop"></property>
    
    
</bean>
```
* Scope defines the scope of bean singleton /prototype
* in case of prototype spring won't create bean unless asked
* 
