package com.springLearn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Human {

    @Autowired
    Laptop laptop;

    public void code()
    {
        laptop.compile();
    }
}
