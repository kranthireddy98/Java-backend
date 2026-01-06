package com.kafkaLearn;


import com.kafkaLearn.dto.Customer;
import com.kafkaLearn.service.KafkaMessagePublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class KafkaProducerExampleApplicationTests {

    @Container
    static  KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("apache/kafka:3.7.0"));


    @DynamicPropertySource
    public static void initKafkaProperties(DynamicPropertyRegistry registry)
    {

        registry.add("spring.kafka.bootstrap-servers",kafka::getBootstrapServers);
        registry.add("spring.kafka.consumer.bootstrap-servers", kafka::getBootstrapServers);

    }


    @Autowired
    private KafkaMessagePublisher messagePublisher;

    @Autowired
    private KafkaTemplate<String,Object> template;

    @Test
    public void testSendEventsToTopic(){
        messagePublisher.sendCustomerMessageToTopic(new Customer(101,"test customer","test@t.com","4578"));
        await().pollInterval(Duration.ofSeconds(3))
                .atMost(10, TimeUnit.SECONDS).untilAsserted(() ->{
                    assert (true);
                });
    }

    @Test
    public void testSendCustomerEventsToTopic(){
        Customer customer = new Customer(101,"test customer","test@t.com","4578");
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
        await().pollInterval(Duration.ofSeconds(3))
                .atMost(10, TimeUnit.SECONDS).untilAsserted(() ->{

                });
    }



}
