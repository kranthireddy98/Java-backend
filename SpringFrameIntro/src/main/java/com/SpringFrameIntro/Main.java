package com.SpringFrameIntro;


import com.SpringFrameIntro.Vehicles.Kid;
import com.SpringFrameIntro.Vehicles.Vehicle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {

        //BeanFactory factory = new XmlBeanFactory(new FileSystemResource("SpringFrameIntro/spring.xml"));

        //Interface
        ApplicationContext factory = new ClassPathXmlApplicationContext("spring.xml");

        Computer obj = (Computer) factory.getBean("computer");
        System.out.println(obj.getModel());
        obj.getLaptop().complie();

        Kid kid = (Kid) factory.getBean("kid");
        kid.getVehicle().start();
    }
}