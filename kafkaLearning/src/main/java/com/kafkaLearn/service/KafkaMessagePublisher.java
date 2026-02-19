package com.kafkaLearn.service;

import com.kafkaLearn.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class KafkaMessagePublisher {

    @Autowired
    private KafkaTemplate<String,Object> template;

    public void sendMessageToTopic(String message)
    {
       CompletableFuture<SendResult<String,Object>> future = template.send("java-topic",message);
       future.whenComplete((result,ex) ->{
           if(ex==null)
           {
               System.out.println("Sent Message=[" + message+"] with offset" +
                       "=["+result.getRecordMetadata().offset()+"]");
           } else {
               System.out.println("Unable send message=[" +
                       message + "] due to : " + ex.getMessage());
           }
       });

    }

    public void sendCustomerMessageToTopic(Customer customer)
    {
        ExecutorService s  = Executors.newFixedThreadPool(2);
        s.shutdown();
        Executor ext = Executors.newFixedThreadPool(3);
        ((ExecutorService) ext).shutdown();
        // Send overloaded method - can pass specific partition
        CompletableFuture<SendResult<String,Object>> future = template.send("java-topic-customer",customer);
        future.whenComplete((result,ex) ->{
            if(ex==null)
            {
                System.out.println("Sent Message=[" + customer+"] with offset" +
                        "=["+result.getRecordMetadata().offset()+"]");
            } else {
                System.out.println("Unable send message=[" +
                        customer + "] due to : " + ex.getMessage());
            }
        });

    }

}
