### Redis Sentinel (Automatic Failover System)

### What is Redis sentinel

Redis Sentinel is:

A distributed monitoring and failover system for redis.

It:

* Monitors Redis instances
* Detects primary failure
* Promotes replica to primary
* Notifies applications

### Basic Architecture

``` 
           ┌──────────────┐
           │  Sentinel 1  │
           └──────────────┘
           ┌──────────────┐
           │  Sentinel 2  │
           └──────────────┘
           ┌──────────────┐
           │  Sentinel 3  │
           └──────────────┘

                 │
                 ▼
          ┌─────────────┐
          │   Primary   │
          └──────┬──────┘
                 │
          ┌──────┴──────┐
          ▼             ▼
      Replica 1      Replica 2
```
### Why multiple sentinels?
Sentinel uses voting(quorum)

If one Sentinel fails -> System still works.

Production minimum:

3 Sentinel instances

### How Failover Happens
let's say primary crashes.

Step 1 - Detection

Sentinels png primary

If no response:

Marked as SDOWN (Subjectively Down)

Step 2: Quorum Agreement

If majority of the Sentinels agree:

Marked as ODOWN (Objectively Down).

Step 3 -- Leader Election

Sentinels elect one sentinel as leader.

Step 4 -- Replica Promotion

Leader:
* Chooses best replica
* Promotes it to Primary
* Reconfigures other replicas

Step 5 -- Notify Clients

Applications connected via sentinel:

Automatically redirected to new primary.

### Important: Sentinel Does NOT Store Data
Sentinel is just a monitoring system.

It does not handle:
* Reads
* Writes
* Persistence

### Basic Sentinel Configuration
In `sentinel.conf`

``` 
sentinel monitor mymaster 192.168.1.10 6379 2
```
Meaning:
* Monitor primary at IP
* Quorum = 2 Sentinels must agree

### Real Failure Scenario
``` 
Primary crashes
Sentinel detects
Replica promoted in ~5-10 seconds
App reconects
System continues
```
Small downtime.

But no manual intervention.

### Replication + Sentinel Together
Replication gives:

* Data copy

Sentinel gives:

* Automatic failover

Together:

High availability system.

### Limitation of sentinel
Sentinel does NOT:
* Provide sharding
* Increase write capacity
* Scale horizontally

For that:

We need Redis Cluster (next lesson).

### Insight
if you're using Redis for:
* Sessions
* Distributed locks
* Rate limiting

Without Sentinel:

If primary crashes:

App breaks.

With Sentinel:

App reconnects automatically.

Spring Boot supports Sentinel configuration.

### Replication VS Sentinel vs Cluster
| Feature            | Replication | Sentinel         | Cluster |
| ------------------ | ----------- | ---------------- | ------- |
| Data copy          | ✅           | Uses replication | ✅       |
| Auto failover      | ❌           | ✅                | ✅       |
| Horizontal scaling | ❌           | ❌                | ✅       |




















