1. Start the zookeeper
2. start kafka server
3. create a topic
4. Partitions

### Start Zookeeper (default port : 2181)
* bin/zookeeper-server-start.sh /config/zookeeper.properties

### start kafka server
* bin/kafka-server-start.sh /config/server.properties

### Create a topic
* bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic javalearning-topic 3 --replication-factor 1

### Producer
*bin/kafka-console-producer.sh --broker-list localhost:9092 --topic javalearning-topic

### Consumer
*bin/kafka-console-consumer.sh --broker-list localhost:9092 --topic javalearning-topic