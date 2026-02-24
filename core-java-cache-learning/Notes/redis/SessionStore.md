### what is a session?

When user logs in:
``` 
username + password -> server validates -> server creates session
```
Session stores:
* User id
* roles
* Login time
* auth status

Traditionally stored in:
* Application memory

### Problem with in-memory sessions

Imagine:

``` 
Load Balancer
    ↓
App Server 1
App Server 2
App Server 3
```
User logs in -> session stored in Server 1.

Next request goes to server 2.

Session not found -> user logged out.

### Solution 1 - Sticky Sessions

Load balancer sends same user always to same server.

Problem :
* If server crashes -> session lost
* Hard to scale

Not recommended in modern microservices.

### Solution 2: Centralized Session Store

Use Redis as shared session storage.

Architecture
``` 
Load Balancer
      ↓
App Server 1
App Server 2
App Server 3
      ↓
      Redis
```
Now:
* Any server can read session
* Horizontal scaling possible
* Fault tolerant

### How Session Stored in Redis?
Example:
``` 
spring:session:sessions:abc123
```
Value (stored as Hash):
```
creationTime
lastAccessedTime
maxInactiveInterval
userDetails
```

### Spring Boot Implementation
Add dependency:

``` 
spring-session-data-redis
```

Configuration;
``` 
spring.session.store-type=redis
spring.redis.host=localhost
spring.redis.port=6379
```
That's it.

Spring automatically:
* Stores session in Redis
* Manages TTL
* Handles expiration

### Session expiry 
If session timeout = 30 min:

Redis Key will have;
``` 
TTL = 1800 seconds
```
After 30 minutes:

Session auto-deleted.

### What happens when Redis Restarts?

If persistence enabled:
* Session restored

If no persistence:
* All users logged out

Production tip:

Enable AOF for session storage.

### Sticky vs Redis Sessions Comparison

| Feature        | Sticky | Redis |
| -------------- | ------ | ----- |
| Scalable       | ❌      | ✅     |
| Fault tolerant | ❌      | ✅     |
| Cloud ready    | ❌      | ✅     |
| Recommended    | No     | Yes   |


### Real Production issue:

If redis goes down:

All sessions unavailable:

Solution:
* redis replication
* Redis Cluster
* Fallback authentication

### Distributed Systems Angle

Session stored in Redis:
* Makes app stateless
* Enables aut-scaling
* Enables blue-green deployment
* Enables Kubernetes scaling


### Design a session management for scalable microservices?

* Avoid sticky sessions
* Use Redis-backed centralized session
* Enable AOF for durability
* Use replication for high availability
* Monitor Redis health



























