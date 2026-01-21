## What is a Stream? (Deep Dive into Stream Lifecycle)

### What Exactly is a java Stream?

A stream is a pipeline of operations that processes data from a source.

**What a stream is NOT**
* Not a data structure
* Does not store elements
* Does not modify the source

**What a Stream is**
* A sequence of elements
* Supports functional-style operations
* Processes data lazily
* Consumed only once

### The stream pipeline

Every stream has 3 stages:
```text
source -> intermediate operations -> terminal operation
```

Example :
```text
List<Integer> numbers = List.of(10, 15, 20, 25, 30);

numbers.stream()                 // Source
       .filter(n -> n > 15)      // Intermediate
       .map(n -> n * 2)          // Intermediate
       .forEach(System.out::println); // Terminal
```
### Stage 1:source

A stream must start from a source.

common Sources:
```java
list.stream()
set.strem()
map.entrySet().stream()
Arrays.stream(array)
Stream.of(1,2,3)
```

Source does nothing by itself.

```java
Stream<Integer> stream = numbers.stream(); // no execution yet
```
### Stage 2: Intermediate Operations
Characteristics:
* Return a Stream
* Can be chained
* Lazy

**Common Intermediate Ops:**
* filter
* map
* flatMap
* distinct
* sorted
* limit
* skip
```java
Stream<Integer> filtered = 
        numbers.stream()
                .filter(n -> n > 20);                
```

Nothing runs yet

### Stage 3: Terminal Operation
Characteristics:
* Triggers execution
* Produces a result or side-effect
* Ends the Stream

Common Terminal Ops:
* forEach
* collect
* reduce
* count
* findFirst
* anyMatch
```java
numbers.stream()
.filter(n -> n > 20)
        .count(); // execution happens here
```
### Lazy Evaluation
Streams execute ONLY when a terminal operation is called
```java
numbers.stream()
.filter(n-> {
    System.out.println("filtering " + n);
    return n>20;
        });
```

Output: Nothing

Why?

No Terminal operation.

**Add terminal operation:**
```java
numbers.stream()
.filter(n-> {
    System.out.println("filtering " + n);
    return n>20;
        })
        .count();
```

### Streams Process Elements ONE BY ONE 

```java
numbers.stream()
.filter(n->{
    System.out.println("filter " + n);
    return n > 15;
        })
        .map(n -> {
            System.out.println("map " + n);
            return n * 2;
        })
        .forEach(System.out::println);
```

Execution Order:
```text
filter 10
filter 15
filter 20
map 20
40
filter 25
map 25
50
filter 30
map 30
60
```
Not:
* filter all -> map all -> forEach all

Instead:
* Element -> filter -> map -> terminal -> next element

### Common mistakes
1. Stream without Terminal Operation
2. Reusing Stream

