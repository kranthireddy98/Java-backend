
### Creating a Stream from a List

```java
List<String> names = List.of("Kranthi","Ravi","Anil");

names.stream()
    .forEach(System.out::println);
```
key points
* stream() is defined in collection interface
* Original list remains unchanged
* Stream is consumed once

### Creating a Stream from a Set

```java
Set<Integer> numbers = Set.of(10,20,30);

numbers.stream()
    .map(n -> n * 2)
        .forEach(System.out::println);
```
### Creating a stream from a Map
Important: Map itself is NOT a Collection

1. Stream from entrySet()
```java
Map<Integer,string> map = Map.of(1,"HR",2,"IT");

map.etryset().stream().
        .forEach(e -> System.out.println(e.getkey() + " : " + e.getValue()));
```
2. Stream from keySet()
```java
map.keySet().stream().forEach(System.out::println);
```
3. Stream from values()
```java
map.values().stream().forEach(System.out::println);
```

### Creating a Stream from an Array
```java
int[] arr = {1,2,4,5,6};

Arrays.stream(arr)
.filter(n -> n%2 ==0)
        .forEach(System.out::println);
```
**Arrays.stream() is preferred over stream.of(array)**

### Creating a Stream using Stream.of()

```java
Stream<String> stream = Stream.of("A", "B", "C");

stream.forEach(System.out::println);

```
Single use only

### Empty Stream
```java
Stream<String> empty = Stream.empty();
```
Used when
* Avoiding null
* Returning a stream from methods safely

### Stream.generate() && Stream.iterate()
Infinite Stream (must limit)
```java
public class GenerateOf {
    public static void main(String[] args) {
        Stream.generate(() -> "hello")
                .limit(3)
                .forEach(System.out::println);

        Stream.iterate(1,n-> n+1)
                .limit(5)
                .forEach(System.out::println);
    }
}
```
### Common Mistakes
* Using Stream twice
* Expecting modification of source

### Real Example
```java
public List<String> getActiveUserNames(List<User> users) {
    return users.stream()
                .filter(User::isActive)
                .map(User::getName)
                .toList();
}
```