### Redis Replication (Master-Replica architecture)

### Why do we need replication?

if you have
``` 
App -> Redis (single instance)
```

Problems:
* If Redis crashes -> App fails
* No read scaling
* Single point of failure

Solution:

Use replication.

### What is Redis Replication?
Redis Supports:

Primary-Replica (Master-Slave) architecture

Modern Terminology:
* Primary 
* Replica

Architecture
```
             ┌─────────────┐
             │  Primary    │
             └──────┬──────┘
                    │
          ┌─────────┴─────────┐
          ▼                   ▼
     Replica 1           Replica 2
```
How it works:
1. All writes go to primary
2. Primary asynchronously replicates to Replicas
3. Replicas are read-only

### How To Configure Replica
On replica server:
``` 
replicaOf <primary-ip> 6379
```
Or in redis.conf
``` 
replicas 192.168.1.10 6379
```

### Replication Process Internals
When replica connects:
1. full synchronization
2. Primary sends RDB snapshot
3. Replica loads snapshot
4. Continuous replication via command stream

``` 
Replica connects
       │
Primary forks
       │
Send RDB snapshot
       │
Start streaming new write commands
```
### Important: Replication is Asynchronous

Primary does NOT wait for replicas to confirm

So:
``` 
write acknowledge to client
But replica may not have received it yet
```
This means:

Possible data loss if primary crashes before replica sync.

### Real Failure Scenario
``` 
Primary receives write
Before replicating -> crashes
Replica promoted
Recent write lost
```
This is called:

Replication lag issue.

### Read scaling
you can configure app:
``` 
writes -> primary
Reads -> replicas
```
Useful when:
* Heavy read traffic
* Large product catalog
* Analytics queries

### How to Check replication Status
Run:
``` 
INFO Replicaton
```
On primary:
``` 
role: master
connected_salves:2
```
On replica:
``` 
role:slave
master_host:2389
```

### Partial Resynchronization (PSYNC)
If replica disconnects briefly:

Redis does NOT resend full RDB.

Instead:
* Sends missign commands
* Faster recovery

Efficient mechanism.

### Major Limitation
Replication alone does NOT provide
* Automatic failover
* Leader election
* Automatic promotion

If primary crashes:
You must manually promote replica.

### Advantages
* High availability
* Read scaling
* Backup redundancy
* Fast replication

### Limitations
* Async replication
* Possible data loss
* No automatic failover
* Not strong consistency

### Best Practice
If using replication:
* Always combine with Sentinel
* Monitor replication lag
* Avoid huge writes
* Use AOF + RDB 





























