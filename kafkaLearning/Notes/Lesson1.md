# What is Kafka?
* Kafka is a distributed, append-only, persistent log that allows multiple 
Produces and consumers to write and read events independently.

## Difference from Queue

| Queue                      | Kafka                 |
| -------------------------- | --------------------- |
| Message removed after read | Message retained      |
| One consumer               | Many consumer groups  |
| Broker tracks state        | Consumer tracks state |

# Core Building Blocks 

```
Producer → Topic → Partition → Offset → Consumer

```
Important:
* Kafka does sequential disk writes
* Consumers pull, not push
* Offsets are consumer-controlled

## Why Kafka is Fast
1. Sequential I/O (no random disk access)
2. Zero - copy transfer
3. Partition parallelism
4. No Per-message acknowledgements

