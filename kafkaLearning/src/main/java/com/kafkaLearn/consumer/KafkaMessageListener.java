package com.kafkaLearn.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListener {

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    Logger logger = LoggerFactory.getLogger(KafkaMessageListener.class);

    @KafkaListener(id = "consumer0", topics = "java-topic",groupId = "group-1")
    public void consumer(String message)
    {
        if (message.equals("user: 200"))
        {
            System.err.println("Critical error! Stopping consumer...");
            registry.getListenerContainer("consumer0").stop();
            return;
        }
     logger.info("consumer0 consumed the message {} ",message);
    }
    @KafkaListener(id = "consumer1", topics = "java-topic",groupId = "group-1")
    public void consumer1(String message)
    {
        if (message.equals("user: 200"))
        {
            System.err.println("Critical error! Stopping consumer...");
            registry.getListenerContainer("consumer1").stop();
            return;
        }
        logger.info("consumer1 consumed the message {} ",message);
    }

    @KafkaListener(id = "consumer2",topics = "java-topic",groupId = "group-1")
    public void consumer2(String message)
    {
        if (message.equals("user: 200"))
        {
            System.err.println("Critical error! Stopping consumer...");
            registry.getListenerContainer("consumer2").stop();
            return;
        }
        logger.info("consumer2 consumed the message {} ",message);
    }

    @KafkaListener(id = "consumer3",topics = "java-topic",groupId = "group-1")
    public void consumer3(String message)
    {
        if (message.equals("user: 200"))
        {
            System.err.println("Critical error! Stopping consumer...");
            registry.getListenerContainer("consumer3").stop();
            return;
        }
        logger.info("consumer3 consumed the message {} ",message);
    }

    @KafkaListener(id = "consumer4", topics = "java-topic",groupId = "group-1")
    public void consumer4(String message)
    {
        if (message.equals("user: 200"))
        {
            System.err.println("Critical error! Stopping consumer...");
            registry.getListenerContainer("consumer4").stop();
            return;
        }
        logger.info("consumer4 consumed the message {} ",message);
    }
}
