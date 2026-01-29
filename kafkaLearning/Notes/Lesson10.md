## ZooKeeper vs KRaft

### Why kafka Needed Zookeeper
Early Kafka clusters needed a way to:
* Track brokers
* Elect leaders
* Store metadata (topic,partitions,ISR)
* Detect failures

Kafka did not implement this logic itself

It delegated coordination to Zookeeper.

### What Zookeeper Did for Kafka
Zookeeper handled:
* Broker registration
* Controller election
* Partition leadership
* ISR tracking
* Cluster metadata storage

kafka Brokers:
* Were stateless regarding metadata
* Relied on Zookeeper for coordination

### Problems with Zookeeper ( WHy it Had to Go)
As Kafka scaled, Zookeeper becomes bottleneck

**Operational Complexity**
* separate cluster to manage
* Different configs
* Different failure modes

**Scalability Limits**
* Large clusters -> heavy Zookeeper load
* Metadata updates become slow

**Split-brain**
* Kafka health tied to Zookeeper health
* Zookeeper outage = kafka outage

### Kafka Controller 
What is the Controller?

One broker is elected as controller.

Controller responsibilities;

* Leader election
* Partition reassignment
* ISR changes
* Broker failure handling

Before KRaft:
* Controller election info lived in Zookeeper

### KRaft (Kafka Raft)
KRaft = Kafka Raft-based metadata Mode

Kafka manages its own metadata using Raft consensus algorithm.

Zookeeper no longer required.

### how KRaft Works
Kafka introduces:
* Controller quorum
* Metadata log
* Raft protocol

Instead of Zookeeper:
* Metadata is stored inside Kafka
* Controllers form a quorum
* Changes are replicated vie Raft

Kafka now:
* Stores metadata like it stores messages
* uses logs + replication for consistency

### Benefits of KRaft
**Simple Architecture**
* No zookeeper cluster
* Fewer moving parts

**Better Scalability**
* Faster metadata operations
* Large cluster friendly

**Stronger Consistency**
* Raft guarantees
* Cleaner leader elections

**Cloud & Kubernetes Friendly**
* Easier deployments
* Better automation

### Zookeeper vs KRaft

| Aspect                 | ZooKeeper Mode | KRaft Mode   |
| ---------------------- | -------------- | ------------ |
| External dependency    | Yes            | No           |
| Metadata storage       | ZooKeeper      | Kafka itself |
| Consensus              | ZooKeeper Zab  | Raft         |
| Operational complexity | High           | Lower        |
| Future support         | Deprecated     | Default      |


Zookeeper mode is deprecated in modern kafka

### Migration Reality 
* Existing clusters may still run Zookeeper
* New cluster should use KRaft
* migration is non-trivial
* Requires planning & downtime strategy

































