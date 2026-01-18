## Why Java Streams were introduced
Problem before streams (Java 7 and earlier)

you had loops everywhere:
```java
public class EvenNumber {


    public static void main(String[] args) {
        List<Integer> numbers = List.of(10, 15, 20, 25, 30);
        System.out.println(evenNumberLoop(numbers));
    }

    public static List<Integer> evenNumberLoop(List<Integer> nums) {
        List<Integer> evenNumbers = new ArrayList<>();

        for (Integer num : nums) {
            if (num % 2 == 0) {
                evenNumbers.add(num);
            }
        }
        return evenNumbers;
    }
}
```
Problems with this approach:
* Too much boilerplate
* Business logic mixed with looping logic
* hard to read when logic grows
* Nested loops become messy
* Parallel processing is difficult

**What Streams Solve**
```java
public class EvenNumber {


    public static void main(String[] args) {
        List<Integer> numbers = List.of(10, 15, 20, 25, 30);
        System.out.println(evenNumberStream(numbers));
    }
    public static List<Integer> evenNumberStream(List<Integer> nums){
        return nums.stream()
                .filter(num -> num%2 ==0)
                .toList();
    }
}

```
**Benefits.**
* Declarative
* More Readable
* Easier to chain operations
* Easy parallelization
* Functional programming style

### External Iteration vs Internal Iteration

External Iteration (OLD)
```java
for (Integer n : numbers){
    System.out.println(n);
}
```
* You control how iteration happens.

Internal Iteration (Streams)

```
number.stream().forEach(System.out::println)
```
* java control how iteration happens

**Key Concept**
* A Stream is not a Data structure
* Stream doe not store data
* Stream processes data from a source
* original collection is NOT modified
```java
numbers.stream().filter(n -> n > 20);
System.out.println(numbers); // unchanged
```
* A Stream can be consumed ONLY ONCE
```java
Stream<Integer> stream = numbers.stream();
stream.forEach(System.out::println);
stream.forEach(System.out::println); // ❌ IllegalStateException
```