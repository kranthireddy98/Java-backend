package com.springLearn.jdbc;

import com.springLearn.Main;
import com.springLearn.jdbc.model.User;
import com.springLearn.jdbc.repository.UserRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringJdbcApplication {
    public static void main(String[] args) {
        ApplicationContext contex=  SpringApplication.run(Main.class,args);

        User user1 = contex.getBean(User.class);
        user1.setAge(24);
        user1.setName("Kranthi reddy");
        user1.setTech("Spring");

        UserRepo repo = contex.getBean(UserRepo.class);
        //repo.save(user1);
        System.out.println(repo.findAll());
    }
}
