## JWT & Token-Based Security

### Why JWT Exists 
Default Spring Security uses sessions:
* Server stores session
* Client sends session Id (cookie)

Problems for APIs
* Not stateless
* Hard to scale horizontally
* Doesn't work well for mobile/ microservices
* session replication complexity

JWT Solves This buy:

Moving authentication state from server to token


### What is JWT?
JWT (JSON Web Token) is a self contained token that holds:
* User identity 
* Roles/autoritiees
* Expiry
* Signature (to prevent tampering)

JWT is:
* Stateless
* Signed (not encrypted by default)
* Sent every request

### JWT Structure 
A JWT has three parts:
``` 
HADER.PAYLOAD.SIGNATURE
```
HEADER
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```
payload
```json
{
  "sub": "user123",
  "roles": ["ROLE_USER"],
  "iat": 1700000000,
  "exp": 1700003600
}
```
Signature
```text
HMACSHA256(
  base64(header) + "." + base64(payload),
  secretKey
)
```

* Anyone can read header & payload 
* Only server can verify signature

### JWT Authentication FLOW 
```text
1. Client sends username/password
2. Server authenticates credentials
3. Server generates JWT
4. Client stores JWT
5. Client sends JWT in Authorization header
6. Server validates JWT
7. SecurityContext populated
8. Request allowed
```
### Where JWT Fits in Spring Security
JWT replaces:
* Session creation
* Session lookup

JWT works via:

Custom Security Filter

inserted into:
``` 
Security Filter Chain
(before UsernamePasswordAuthenticationFilter)
```

### Problem 1: JWT Generated but requests Still unauthorized

JWT Generated correctly, but every returns 401.

Why?

Because:
* Spring Security does NOT Know about JWT by default
* No filter is validating the token
* SecurityContext is empty


### The JWT Filter
What the filter Must Do

For every request:
* read `Authorization` header
* Extract token
* Validate signature & expiry
* Extract user + roles
* Create authentication
* Store in SecurityContext


### Problematic JWT Filter (incomplete)
```java
String header = request.getHeader("Authorization");
String token = header.substring(7); // ❌ unsafe
```
issues
* Header may be null
* Token may be invalid
* No validation
* no SecurityContext population

**Correct JWT Filter**

```java

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException{
        String header = request.getHeader("Authorization");
        
        if(header != null && header.startsWith("Bearer ")){
            String token = header.substring(7);
            
            if(jwtService.isValid(token)){
                String username = jwtService.getUserName(token);
                
                List<GrantedAuthorities> authoritiesList = jwtService.getAuthorities(token);
                
                Athentication auth = new UsernamePasswordAuthenticationToken(
                        username,null,authoritiesList
                );
                
                SecurityContextholder.getContex().setAuthetication(auth);
            }
        }
        filterChain.doFilter(request,response);
    }
}
```

### Registering the JWT Filter

JWT filter exists, but never runs.


```java
@Bean
SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(sess ->
            sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtFilter,
                UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

```
### Stateless Session Management
``` 
.sessionManagement(
    sess -> sess.sessionCreationPolicy(STATELESS)
)
```
Without this:
* Spring still creates sessions
* JWT loses its purpose

### Problem 2: JWT Works once, then fails

Cause

Token expired

JWT expiry is absolute.

**Solution: Proper Expiry Strategy**

Typical setup:
* Access Token -> short-lived (15-30 min)
* Refresh token -> long-lived (days)

### Problem 3: Roles Not working
``` 
hasRole("ADMIN")
```
JWT Payload
``` 
"roles": ["ADMIN"]
```
Why it fails:

Spring expects:
``` 
ROLE_ADMIN
```
**Solution**

Either:
* Store roles with ROLE_ prefix
* Or use hasAuthority("ADMIN")

### Where JWT Data Lives at Runtime
After Validation:
``` 
JWT → Authentication → SecurityContext → ThreadLocal
```
This context:
* Exists only for current request
* Cleared after response

### JWT vs Session

| Feature       | Session | JWT  |
| ------------- | ------- | ---- |
| Server state  | Yes     | No   |
| Scalability   | Low     | High |
| Revocation    | Easy    | Hard |
| Stateless     | ❌       | ✅    |
| Microservices | ❌       | ✅    |


### Common JWT Security Mistakes
* Storing JWT in LocalStorage (Xss Risk)
* Long-lived access tokens
* no token expiry
* No Signature validation
* Putting sensitive data in payload

### Mental model
JWT is proof of authentication, not authorization logic

JWT just answers:

Who is this request coming from
























