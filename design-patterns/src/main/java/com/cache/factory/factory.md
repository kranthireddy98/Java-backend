# What is factory Pattern?

The Factory Pattern is a creational design pattern that hides object creation logic and 
lets subclasses or a factory class decide which object to create.

--> You ask factory for an object
--> You don't care how it is created
--> You only care what behavior it provides

## Problem without Factory

```
    Vehicle V;

        if(type.equals("CAR"))
        {
            v= new Car();
        }else if (type.equals("BIKE"))
        {
            v = new Bike();
        }
```
* Issues
1. Tight coupling 
2. Hard to extend
3. Violates Open/closed Principle

## Factory Pattern -- Creation logic is Centralized.

### Step 1. Common interface
```java
interface Vehicle{
    void drive();
}
```

### Step 2. Implementation

```java
class Car implements Vehicle{
    public void drive() {
        System.out.println("Driving a car");
    }
}

class Bike implements Vehicle{
    public void drive()
    {
        System.out.println("Riding a bike");
    }
}
```
### Step 3. Factory Class

```java

class VehicleFactory {
    public static Vehicle getVehicle(String type)
    {
        if (type.equalsIgnoreCase("CAR"))
        {
            return new Car();
        } else if (type.equalsIgnoreCase("BIKE")) {
            return  new Bike();
        }
        throw  new IllegalArgumentException("Unknown vehicle type");
    }
}
```

### Step 4: Client Code

```java
public class Main{
    public static void main(String[] args)
    {
        Vehicle v = vehicleFactory.getvehicle("CAR");
        v.drive();
    }
}
```

* Benefits
1. Loose coupling
2. Centralized creation logic
3. Easy to add new types
4. Cleaner & readable code
5. Easier testing (mock factory)

## Factory Method
Here, subclass decide which object to create

Abstract Creator
```java
abstract class VehicleFactory {
    abstract Vehicle createVehicle();
    
    public void startRide()
    {
        Vehicle v = createVehicle();
        v.drive();
    }
}
```
Concrete Factories
```java
class CarFactory extends VehicleFactory
{
    Vehicle createVehicle()
    {
        return new Car();
    }
}

class BikeFactory extends VehicleFactory {
    Vehicle createVehicle() {
        return new Bike();
    }
}
```

Usage 

```
VehicleFactory factory = new CarFactory();
factory.startRide();
```

Real World Examples 
1. LoggerFactory.getLogger()
2. Calendar.getInstance()
3. Executors.newFixedThreadPool()
4. BeanFactory / ApplicationContext (Spring)


## TODO
--> Abstract factory

# Interview Questions

1. why Factory Pattern?
2. Difference between Factory & Abstract Factory?
3. Is Factory pattern used in Spring?

## When SHOULD you use Factory?
✔ Object creation is complex 
✔ Many implementations
✔ Need lose coupling
✔ Framework / library design