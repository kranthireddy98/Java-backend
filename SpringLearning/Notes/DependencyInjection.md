
Dependency Injection (DI) is a core principle of the Spring Framework, where an external container (the Spring IoC container) is responsible for providing the dependencies that an object needs, rather than the object creating them itself. This promotes loose coupling, makes code easier to test, and improves overall application modularity and maintainability. 


## Setter injection
injecting through the  setter method of field
```java
public class Computer {

    private int model;
  

    public int getModel() {
        return this.model;
    }

    public void setModel(int model) {
        System.out.println("model assigned by spring");
        this.model = model;
    }

    public  Computer(){
        System.out.println("Computing...");
    }
}
```

```xml
    <bean id="computer" class="com.SpringFrameIntro.Computer" scope="singleton">

        <!-- default values-->
        <property name="model" value="2025"></property>
        <!--For reference types use ref not value-->
        <property name="laptop" ref="laptop"></property>
    
    </bean>
```
## Constructor injection
Injection through the Argument Constructor of the class
```java
    public class Laptop {

    private String make;
    private Chip chip;

    public Laptop(String make,Chip chip)
    {
        this.make = make;
        this.chip=chip;
    }

    public void complie()
    {
        System.out.println("code complied");
    }
}
```
```
    <bean id="laptop" class="com.SpringFrameIntro.Laptop" scope="singleton">
        <!--Constructor injection-->
        <constructor-arg name="make" value="Hp"></constructor-arg>
        <constructor-arg name="chip" ref="chip"></constructor-arg>
    </bean>

    <bean id="chip" class="com.SpringFrameIntro.Chip" >

```

### Autowired
**By Name** 
```java
public class Kid {
    private int age;
    private Vehicle vehicle;

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }


}
```
```xml
  <bean id="kid" class="com.SpringFrameIntro.Vehicles.Kid" autowire="byName">
        <property name="age" value="16"></property>
    </bean>
   <!-- id vehicle matched with the field name in the Class-->
    <bean id="vehicle" class="com.SpringFrameIntro.Vehicles.Bus" ></bean>
    <bean id="bike" class="com.SpringFrameIntro.Vehicles.Bike" ></bean>
```
**By Type**
Object Type of the field
```xml
    <bean id="kid" class="com.SpringFrameIntro.Vehicles.Kid" autowire="byType">
        <property name="age" value="16"></property>
    </bean>
        <!--bus and bike is extending the Vehicle -->
    <bean id="bus" class="com.SpringFrameIntro.Vehicles.Bus" ></bean>
    <bean id="bike" class="com.SpringFrameIntro.Vehicles.Bike" ></bean>
```
* if more than one Bean of same type gives Qualifying type error
* Use primary property
```xml
   <bean id="kid" class="com.SpringFrameIntro.Vehicles.Kid" autowire="byType">
        <property name="age" value="16"></property>
    </bean>

    <!--bus and bike is extending the Vehicle -->
    <bean id="bus" class="com.SpringFrameIntro.Vehicles.Bus" primary="true"></bean>
    <bean id="bike" class="com.SpringFrameIntro.Vehicles.Bike" ></bean>
```