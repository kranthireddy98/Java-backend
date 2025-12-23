# What is Singleton?
--> Creational design pattern
--> Singleton ensures that a class has only ONE instance and provide a global access point to it

### intent
1. Exactly one object
2. Controlled access
3. Share state


## Classic Definition (GOF)

Ensure a class has only one instance and provide a global point of access to it

## Why do we need singleton?

Common real-world use cases:
1. Configuration manager
2. Cache
3. Logger
4. Database connect Pool
5. Thread pool
6. Spring Beans (by default)

# Core Rules of singleton
1. Private constructor
2. Private static instance
3. Public static getter

## Eager Initialization
```java
public class Singleton {
    private static final Singleton INSTANCE = new Singleton();

    private Singleton() {}

    public static Singleton getInstance() {
        return INSTANCE;
    }
}

```
* Pros
1. Thread-safe
2. Simple 
3. No Synchronization overhead
* Cons
1. Instance created even if never used

## Lazy Initialization (NOT Thread-safe)
```java
public class Singleton {
    private static Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}

```
* Problem
1. Race Condition
2. Multiple threads can create multiple objects

## Synchronized method (Thread-safe but slow)

```java
public class Singleton
{
    private static Singleton instance;
    
    private Singleton(){}
    public synchronized static Singleton getInstance()
    {
        if(instance == null)
        {
            instance = new Singleton();
        }
        return instance;
    }
}
```

* Pros
1. Thread-safe
* Cons
1. Synchronization on every call(Performance hit)

## Double-checked Locking 
```java
public  class Singleton {
    private static volatile singleton instance;
    
    private Singleton() {}
    
    public static Singleton getInstance()
    {
        if(instance == null)
        {
            synchronized (Singleton.class)
            {
                if (instance ==null)
                {
                    instance =new Singleton();
                }
            }
        }
        return instance;
    }
}
```
* Why volatile?
1. Prevents instruction reordering
2. Ensures visibility across threads 

# Bill pugh Singleton 
```java
public class Singleton {
    private Singleton() {}
    
    private static class Holder {
        private static final Singleton INSTANCE = new Singleton();
    }
    
    public static Singleton getInstance()
    {
        return Holder.INSTANCE;
    }
}
```
* Advantages 
1. Lazy initialization
2. Thread-safe
3. No synchronization
4. Clean & efficient

## Enum Singleton 

```java
public enum Singleton {
    INSTANCE;
    
    public void doSomething() {
        System.out.println("Single via Enum");
    }
}
```
* Handles
1. Serialization
2. Reflection
3. Thread safety
* Downside
1. Less flexible
2. Not always liked in enterprise cde


# How Singleton can be Broken



# Interview Questions You’ll Face
1. Why volatile is required?
2. How does JVM ensure thread safety in Bill Pugh?
3. Enum vs Bill Pugh?
4. Difference between Spring Singleton & Java Singleton?
5. Can Singleton be garbage collected?
6. How Singleton behaves in multi-classloader environment?