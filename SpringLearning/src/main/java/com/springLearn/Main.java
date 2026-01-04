package com.springLearn;


import com.springLearn.jdbc.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
      ApplicationContext contex=  SpringApplication.run(Main.class,args);

        /*Human human = contex.getBean(Human.class);

        human.code();*/


    }
}