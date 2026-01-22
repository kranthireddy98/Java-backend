package main.java.com.JavaStreams;

import java.util.stream.Stream;

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
