## Spring Security Fundamentals

### Why Spring Security Exists

Web application face:
* unauthorized access
* Credentials theft
* Session hijacking
* API abuse

Spring Security exists to:
* Authenticate users
* Authorize access
* Protect endpoints
* Secure data flows

Spring Security is not optional

### Authentication vs Authorization

Authentication

Who are you?
* username/password
* Token
* Certificate

Authorization

what are you allowed to do?
* Roles 
* Permissions
* Access rules

Authentication happens first. Authorization depends on it

### High-level Security Flow
```text
HTTP Request
 ↓
Security Filter Chain
 ↓
Authentication
 ↓
SecurityContext populated
 ↓
Authorization checks
 ↓
Controller execution

```
Everything in Spring security resolves around this flow.

## Security Filter Chain (The Core)
Spring Security works using a chain of servlet filters

Example of filters
* Authentication filter
* Authorization filter
* CSRF filter
* Exception translation filter

Filters execute:
* Before Dispatch servlet
* For every request

No filter --> No security

### Default behavior in spring boot
if you add
```text
spring-boot-starter-security
```
Spring Boot automatically:
* secures all endpoints
* Generates a default user
* Enables form login & basic auth

Result:
* /login page
* 401/403 responses

This is auto configuration, not magic

### The most important Object: Security context

What is security context?

SecurityContext holds
* Authentication object
* User identity
* Authorities (roles)

It is stored in
```text
SecurityContextHolder
```
And is
* Thread-bound
* Cleared after request completes

### Authentication Object
Authentication contains:
* Principal (user)
* Credentials (password/token)
* Authorities (roles)
* Authenticated flag

If:
```text
authentication.isAuthenticated() == true
```
user is authenticated

### How Application Works internally
when request arrives:
1. Authentication filter intercepts
2. Extracts credentials
3. Calls AuthenticationManager
4. Delegates to AuthenticationProvider
5. On success --> Authentication created
6. Store in SecurityContext

This is pluggable, Not hard coded

### Problem 1: All Endpoints secured unexpectedly
Scenario

you add security dependency.

Suddenly:
* /health
* /login
* /public

All require authentication

** Solution: Define Security Rules
````java

SecurityFilterChain securityFilterChain(HtttpSecurity http) throws Exception{
    http.authorizehttpRequests(auth -> auth
            .requestMatchers("/public/**").permitAll()
            .anyRequest().authenticated()
    ).httpBasic();
    
    return http.build();
}
````

### Authorization Rules (How Access is Decided)
Authorization uses:
* Roles (ROLE_ADMIN)
* Authorities (READ_USER)
```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
)
```
hasRole("ADMIN) --> internally checks ROLE_ADMIN

### Problem 2: Access Denied Even after login
Cause 

Role mismatch
``` 
hasRole("ADMIN")
```
but use has
```java
ADMIN (missing ROLE_ prefix)
```
Solution

Either 
* Store roles with ROLE_ prefix
* Or use hasAuthority(ADMIN)

### Where Security Rules Are Enforced
Authorization checks happen:
* After authentication
* before controller execution

If access denied:
* Controller is never called

this is why;

my controller break point not hit


### CSRF (Cross-Site Request forgery)

What it is

An attack where
* browser automatically sends cookies
* Malicious site triggers actions

Spring security:
* Enables CSRF by default for browser apps
* Requires CSRF token for POST/PUT/DELETE

### Problem 3: 403 on POST request

CSRF enabled but token missing.

**Solution (StateLess APIs)** 
```java
http.csrf(csrf -> csrf.disable());
```
Disables only for stateless REST APIs

### Session-Based Security 
Spring Security:
* Stored SecurityContext in HTTP session
* Session ID identifies user
* Good for browser apps

limitations:
* Not scalable for APIs
* Not stateless

replace this with JWT

### Common Spring Security Misconfiguration
* Disabling security entirely
* Overusing permitAll()
* Mixing roles and authorities incorrectly
* Disabling CSRF blindly
* Assuming controller-level security is enough

### mental model
Spring security is a filter pipeline, not annotations.

Annotations are just hints to the pipeline.

If you think filters -> everything makes sense

