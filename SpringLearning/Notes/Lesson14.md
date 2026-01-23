## Spring Transactions (@Transactional)

### What is a Transaction?
A transaction is unit of work that must follow ACID properties:
* Atomicity -- all or nothing
* Consistency -- data remains valid
* Isolation -- concurrent transactions don't interfere
* Durability -- commited data is permanent

In Simple terms;

Either everything succeeds, or everything is rolled back.

### Why Spring Transactions Exists

without Spring:

```java
import java.sql.Connection;

Connection con = dataSource.getConnection();
try{
    con.setAutoCommit(false)
    //DB operations
    con.commit();
        }catch(Exception e){
    con.rollback();
        }
```
Problems:
* Boilerplate code
* Easy to forget rollback
* Hard to manage across layers
* Impossible to scale cleanly

Spring solves this using declarative transactions.

### What is `@Transactional`?
`@Transactional` tells Spring:

Execute this method inside a transaction boundary

```java
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Transactional
    public void placeOrder() {
        // multiple DB operations
    }
}
```
Spring handles:
* Transaction begin
* Commit
* Rollback
* cleanup

### How Spring Implements Transactions
Spring does not add transactions magically.

it uses:

AOP + Proxies

FLOW:
```text
client -> proxy -> Transaction manager -> Actual Method
```
Steps:
1. Proxy intercepts method call
2. Starts transaction
3. Calls actual method
4. Commits or rolls back
5. returns result

No proxy --> no transaction

### Where `@Transactional` should Be used
Recommended:
* Service layer
* Business logic boundaries

Avoid:
* Controllers 
* Repositories
* private methods

Transactions define business operations, not infrastructure calls.

### Problem 1: `@Transactional` Not Working
Problematic Code

```java
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Transactional
    public void createUser() {
        saveUser();
    }
    
    @Transactional
    public void saveUser(){
        // DB logic
    }
}
```
calling:
```java
userService.createuser();
```
What goes wrong
*  `saveUser()` is called inside same class
* Call does not go through proxy
* Transaction on `saveuser()` is Ignored

This is called:

**Self-invocation problem**

**Solution Move method to another Bean**
```java
@Service
public class UserRepositoryService {

    @Transactional
    public void saveUser() {
        // DB logic
    }
}
```
Inject and call it.

**Solution 2: Put `@Transactional` on public Entry Method**
```java
@Transactional
public void createUser() {
    saveUser();
}
```

### Transaction propagation

What happens when a transactional method calls another transactional method

Common Propagation Types

| Propagation  | Meaning                               |
| ------------ | ------------------------------------- |
| REQUIRED     | Join existing or create new (default) |
| REQUIRES_NEW | Suspend existing, start new           |
| NESTED       | Nested transaction (savepoint)        |
| MANDATORY    | Must have existing                    |
| NEVER        | Must NOT have transaction             |


### Problem 2: partial Data Commit
Problematic code
```java
@Transactional
public void placeOrder() {
    saveOrder();
    saveAudit(); // fails
}
```
But 
```java
@Transactional(propagation = REQUIRES_NEW)
public void saveAudit() {
    throw new RuntimeException();
}
```
Result
* `saveAudit()` rolls back
* `saveOrder()` still commits

**Correct**

`Requires_new`:
* creates independent transaction
* commits/rolls back separately

Use only when absolutely required.

### Transaction isolation Levels

isolation controls data visibility between concurrent transactions.

| Level            | Problem Prevented    |
| ---------------- | -------------------- |
| READ_UNCOMMITTED | None                 |
| READ_COMMITTED   | Dirty reads          |
| REPEATABLE_READ  | Non-repeatable reads |
| SERIALIZABLE     | Phantom reads        |

Default:
* DB-specific (often READ_COMMITTED)

### Problem 3: Inconsistent Reads Under Load

symptoms:
* Same query returns different results
* Data anomalies under concurrency

**Solution**
```java
@Transactional(isolation = Isolation.REPEATABLE_READ)
public void process() { }
```
Higher isolation = lower concurrency.

### Rollback Rules
By default, Spring:
* Rolls back unchecked exceptions
* Does not roll back on checked exceptions

### Problem 4: Transaction Not Rolled Back
```java
@Transactional
public void process() throws Exception {
    throw new Exception(); // checked
}
```
transaction commits

**Solution**
```java
@Transactional(rollbackFor = Exception.class)
public void process() {
    throw new Exception();
}
```

### `@Transactional` on Class vs Method
```java
@Transactional
@Service
public class OrderService {
}
```
* Applies to all public methods
* Method-level annotation overrides class-level 

### Transactional Manager
Spring delegates to:
* `platformTrasactionManager`
Examples:
* `DataSourceTransactionManager`
* `JpaTransactionManager`

Spring Boot auto-configures this.

### Problem 5: using Transactions in Async code
```java
@Async
@Transactional
public void processAsync() { }
```

Transaction does NOT propagate across threads

**Solution**
* Transactions are thread-bound
* Redesign logic
* Or Use messaging/eventual consistency

### Common Real-World Mistakes
* Expecting private methods to be transactional
* Self-invocation
* Overusing `REQUIRED_NEW`
* Ignoring rollback rules
* using transactions in async code

## Mental Model
Transactions exist only at proxy boundaries.

No proxy -> no transaction.

