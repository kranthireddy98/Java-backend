* byte array to  producer -- Serialize
* byte array is consumed in the consumer -- deserialize

Producer and consumer need to serialize and deserialize the data
```
     producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
    consumer:
      group-id: group-1
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring:
          json:
            trusted:
              packages:
                com.kafkaLearn.dto
```