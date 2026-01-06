package com.kafkaLearn;

import com.kafkaLearn.dto.Customer;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;
import com.kafkaLearn.dto.Customer;
import com.kafkaLearn.service.KafkaMessagePublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ActiveProfiles;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.kafka.listener.auto-startup=false",
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}", // Placeholder
        "spring.main.allow-bean-definition-overriding=true"
})
@ActiveProfiles("test")
@Testcontainers
public class KafkaListenerIntegrationTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("apache/kafka:3.7.0")
    );

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        String bootstrapServers = kafka.getBootstrapServers();
        registry.add("spring.kafka.bootstrap-servers", () -> bootstrapServers);
        registry.add("spring.kafka.consumer.bootstrap-servers", () -> bootstrapServers);
        registry.add("spring.kafka.producer.bootstrap-servers", () -> bootstrapServers);
        registry.add("spring.kafka.listener.auto-startup", () -> "false");
        // Crucial: ensure JSON trusted packages are set
        registry.add("spring.kafka.consumer.properties.spring.json.trusted.packages", () -> "*");
    }

    @TestConfiguration
    static class KafkaTestConfig {
        @Bean
        public NewTopic customerTopic() {
            return TopicBuilder.name("java-topic-customer").partitions(3).build();
        }

        @Bean
        public TestListenerObserver observer() {
            return new TestListenerObserver();
        }
    }

    @Autowired
    private KafkaTemplate<String, Object> template;

    @Autowired
    private TestListenerObserver observer;

    @Test
    public void testListenerSuccessfullyConsumesCustomer() throws InterruptedException {
        Customer payload = new Customer(101, "John Doe", "john@example.com", "9999");

        template.send("java-topic-customer", payload);

        // Increased wait time slightly for slow CI environments
        boolean messageReceived = observer.getLatch().await(15, TimeUnit.SECONDS);

        assertThat(messageReceived).isTrue();
        assertThat(observer.getLastReceivedCustomer().getName()).isEqualTo("John Doe");
    }

    // This class now handles both the listening and the data capture
    public static class TestListenerObserver {
        private Customer lastReceivedCustomer;
        private final CountDownLatch latch = new CountDownLatch(1);
        private final Logger logger = LoggerFactory.getLogger(TestListenerObserver.class);

        @KafkaListener(
                id = "test-consumer",
                topics = "java-topic-customer",
                groupId = "group-test",
                autoStartup = "true" // Force this one to start
        )
        public void consume(Customer customer) {
            logger.info("Test observer received: {}", customer);
            this.lastReceivedCustomer = customer;
            latch.countDown();
        }

        public Customer getLastReceivedCustomer() { return lastReceivedCustomer; }
        public CountDownLatch getLatch() { return latch; }
    }
}