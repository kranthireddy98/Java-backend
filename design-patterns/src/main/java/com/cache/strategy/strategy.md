# What is the Strategy Pattern?

Strategy pattern defines a family of algorithms, encapsulates ech one,
and makes them interchangeable at runtime.


--> Instead of writing multiple if-else or Switch blocks, you delegate behavior
to strategy objects.

* Behavior changes, Object stays the same.

## Problem

```
if (paymentType.equals("CREDIT")) {
    // credit card logic
} else if (paymentType.equals("UPI")) {
    // upi logic
} else if (paymentType.equals("NET_BANKING")) {
    // net banking logic
}

```
1. Violates Open/close Principle
2. Hard to maintain
3. Every new payment --> modify existing code
4. Testing becomes messy

# Strategy Pattern

## Step 1. Create a strategy interface
```java
public interface PaymentStrategy {
    void pay(int amount);
}
```

## step 2. Create Concrete Strategies
```java
public class CreditPayment implements PaymentStrategy
{
    public void pay(int amount)
    {
        System.out.println("Paid " + amount + " Using Credit Card");
    }
}

public class UpiPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using UPI");
    }
}

public class NetBankingPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using Net Banking");
    }
}

```

## Step 3 : Context Class (Uses Strategy)

```java
public class PaymentContext{
    private PaymentStrategy strategy;
    
    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }
    public void makePayment(int amount)
    {
        strategy.pay(amount);
    }
        
}
```

## Step 4 : Client Code (Runtime Decision)

```java
public class Main {
    public static void main(String[] args) {

        PaymentContext context = new PaymentContext();

        context.setStrategy(new CreditCardPayment());
        context.makePayment(1000);

        context.setStrategy(new UpiPayment());
        context.makePayment(500);
    }
}

```

```
+-------------------+        uses        +--------------------+
|     Context       |------------------>|     Strategy       |
|-------------------|                   |  (interface)       |
| - strategy        |                   | + execute()        |
| + setStrategy()   |                   +--------------------+
| + performAction() |                            ▲
+-------------------+                            |
-------------------------
|           |           |
+----------------+ +----------------+ +----------------+
| ConcreteStrategyA| | ConcreteStrategyB| | ConcreteStrategyC|
| execute()        | | execute()        | | execute()        |
+----------------+ +----------------+ +----------------+

```

| Strategy Pattern        | Inheritance           |
| ----------------------- | --------------------- |
| Composition             | Tight coupling        |
| Runtime behavior change | Compile-time behavior |
| Clean & extensible      | Hard to scale         |
| Open/Closed Principle   | Violated              |


### Real-World Examples (Very Important for Interviews)
✅ Java Standard Library
Comparator in sorting
Collections.sort(list, comparator);
✅ Spring Framework
PasswordEncoder
AuthenticationProvider
✅ Logging
File logging
Console logging
Cloud logging
✅ Pricing / Discount Engines
Festival discount
Loyalty discount
Coupon discount

## When to use Strategy Pattern?
1. Many related algorithms
2. Need runtime switching
3. Too many if-else blocks
4. Want clean SOLID desing