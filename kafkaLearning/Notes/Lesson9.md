## Kafka Storage & Log Internals

### Kafka is a Distributed Commit Log

At its Core, Kafka is:

A distributed, append-only commit log

Each Partition is:
* A sequence of records
* Written only at the end 
* Never updated or deleted individually

### Physical Storage Layout

Each partition is stored as:
```
/topic-name/partition-id/
```
Inside it:
* Multiple log segments files
* index files for fast lookup

Kafka does not keep one giant file

### Log Segments (Why not one huge file?)

A partition is split into segments.

``` 
orders-0/
 ├── 00000000000000000000.log
 ├── 00000000000000000000.index
 ├── 00000000000000000000.timeindex
 ├── 00000000000000012000.log
 ├── 00000000000000012000.index
```
Each segment:
* has fixed size 
* is immutable once closed

This enables:
* Easy deletion
* Fast recovery
* Predictable IO

### Append only writes (Secret of speed)
Kafka writes:
* Sequentially
* At the end of the active segment

Why this matters:
* Sequential disk IO is extremely fast
* OS page cache is heavily used
* No random writes
* No locks

Kafka can outperform databases even on HDDs


### How reads Work (offsets -> index-> Disk)
When a consumer asks:
```
Give me offset 12332
```
kafka:
1. Uses offset index to find the segment
2. uses file position
3. Reads sequentially

Lookup is O(1)

No full scan required

### Index Files (Fast Access)
Each segment has:
* `.index` -> offset -> file position
* `.timeindex` -> timestamp -> offset

Used for:
* Fast seeks
* Time-based consumption
* Retention cleanup

indexes are:
* Memory-mapped
* Very lightweight


### Retention Policies (When Data is Deleted)

Kafka deletes data based on retention, NOT consumption.

Time-based Retention
``` 
log.retention.hours = 168 (7 days)
```
Size-based Retention
```java
log.retention.bytes = 100 GB
```
Kafka deletes:
* Entire old segments
* Never individual messages

### Log Compaction
What is Compaction?

kafka keeps;
* Latest value per key
* Removes older duplicates

used for:
* Change Data Capture (CDC)
* User profile topics
* Configuration topics

### Why Kafka Doesn't Slow Down Over Time
Reasons:
* Old Segment deleted
* Active segment is always small
* Indexes keep lookups fast
* No fragmentation

Kafka performance is stable over years

### Real-World Scenario
* Topic `audit-events`
* Retention 30 days
* no consumer reads everything
* Kafka safely stores & deletes data

































