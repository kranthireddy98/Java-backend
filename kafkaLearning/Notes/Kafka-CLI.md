1. Start the zookeeper
2. start kafka server
3. create a topic
4. Partitions

**Docker local**
```dockerfile
version: '3.1'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    ports:
      - "2181:2181"
    restart: unless-stopped

  kafka:
    image: wurstmeister/kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INSIDE:PLAINTEXT,OUTSIDE:PLAINTEXT
      KAFKA_LISTENERS: INSIDE://0.0.0.0:9093,OUTSIDE://0.0.0.0:9092
      KAFKA_ADVERTISED_LISTENERS: INSIDE://kafka:9093,OUTSIDE://localhost:9092
      KAFKA_INTER_BROKER_LISTENER_NAME: INSIDE
```
### Start Zookeeper (default port : 2181)
* bin/zookeeper-server-start.sh /config/zookeeper.properties

### start kafka server
* bin/kafka-server-start.sh /config/server.properties

### Create a topic
* bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic javalearning-topic --partitions 3 --replication-factor 1
*  kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test-topic (docker)

### Producer
* bin/kafka-console-producer.sh --broker-list localhost:9092 --topic javalearning-topic
* kafka-console-producer.sh --topic test-topic --bootstrap-server localhost:9092  (docker)

### Consumer
* bin/kafka-console-consumer.sh --broker-list localhost:9092 --topic javalearning-topic
* kafka-console-consumer.sh --topic test-topic --from-beginning --bootstrap-server localhost:9092  (docker)
  cd opt/ kafka_2.13-2.8.1/bin