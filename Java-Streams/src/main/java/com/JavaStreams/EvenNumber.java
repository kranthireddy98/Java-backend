package com.JavaStreams;

import java.util.ArrayList;
import java.util.List;

public class EvenNumber {


    public static void main(String[] args) {
        List<Integer> numbers = List.of(10, 15, 20, 25, 30);
        System.out.println(evenNumberLoop(numbers));
        System.out.println(evenNumberStream(numbers));
    }

    public static List<Integer> evenNumberLoop(List<Integer> nums){
        List<Integer> evenNumbers = new ArrayList<>();

        for(Integer num : nums){
            if(num % 2 == 0){
                evenNumbers.add(num);
            }
        }
        return evenNumbers;
    }

    public static List<Integer> evenNumberStream(List<Integer> nums){
        return nums.stream()
                .filter(num -> num%2 ==0)
                .toList();
    }


}
