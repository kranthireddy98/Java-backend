package com.kafkaLearn.consumer;

import com.kafkaLearn.dto.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

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


    //Specify specific partition  topicPartitions = {@TopicPartition(topic = "java-topic-customer",partitions = {"2"})}
    @KafkaListener(id = "customer-consumer0", topics = "java-topic-customer1",groupId = "group-customer")
    public void customerConsumer(Customer customer)
    {

        logger.info("Customer consumed Event : {}" ,customer.toString());
    }

    @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 3000,multiplier = 1.5,maxDelay =15000 ))
    @KafkaListener(id = "customer-consumer1", topics = "java-topic-customer",groupId = "group-customer")
    public void customerConsumerHandlingError(Customer customer, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                              @Header(KafkaHeaders.OFFSET) int offset  )
    {

        logger.info("Customer consumed Event : {} from {} topic and partition {} and offset {} " ,customer.toString(),topic,offset);
        if(customer.getId() == 101)
        {
            throw new RuntimeException("Invalid Customer Id");
        }
    }


    @DltHandler
    public void customerDltCustomer(Customer customer, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                              @Header(KafkaHeaders.OFFSET) int offset  )
    {

        logger.info("Customer consumed Event : {} from {} topic and partition {} and offset {} " ,customer.toString(),topic,offset);

    }
    /*///Specify specific partition  topicPartitions = {@TopicPartition(topic = "java-topic-customer",partitions = {"2"})}
    @KafkaListener(id = "customer-consumer0", topics = "java-topic-customer",groupId = "group-customer",
            topicPartitions = {@TopicPartition(topic = "java-topic-customer",partitions = {"2"})})
    public void customerConsumer1(Customer customer)
    {
        logger.info("Customer : {}" ,customer.toString());
    }*/



}
