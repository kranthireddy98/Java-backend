1. Why was Spring framework created?
2. What problem existed before Spring?
3. Why is tight coupling bad?
4. Why is the new keyword dangerous in large applications?
5. Is new always bad?
6. Where is new still acceptable?
7. What is Inversion of Control (IoC)?
8. Who controls object creation in IoC?
9. Is IoC specific to Spring?
10. What is Dependency Injection (DI)?
11. Types of dependency injection?
12. Which is preferred and why?
13. Difference between IoC and DI?
14. How does Spring improve testability?
15. How do you inject mocks?
16. Have you used Mockito with Spring?
17. What does Spring container do?
18. Name two Spring containers
19. When are beans created?
# Spring Container
20. What is Spring Container?
21. What are the types of containers?
22. Which one is used in Spring Boot?
23. Difference between Bean factory and Application Context?
24. What is a Spring Bean?
25. When are Spring Beans Created?
26. How to make Lazy?
27. What about prototype beans?
28. How does Spring resolve dependencies?
29. Why Application Context is Preferred Over Bean Factory?
# bean Lifecycle
30. Explain Spring Bean Lifecycle
31. When is @PostConstruct called?
32. Difference between @PostConstruct and InitializingBean?
33. How do you  execute logic before application shutdown?
34. Are Lifecycle methods called for prototype beans?
35. What is BeanPostProcessor?
36. Which lifecycle hook is most recommended?
# Stereotypes
37. what are Spring stereotype annotations?
38. Difference between `@Component` and `@Service`?
39. What special behaviour does `@Respository` provides?
40. Why does Spring need different stereotypes?
41. can you use `@Component` instead of `@Service` or `@Repository`
42. Difference between `@Controllr` and `@Restcontroller`
43. How spring MVC detect controller classes?
# Dependency Injection
44. What is Dependency Injection?
45. How Does Spring implement Di?
46. What problems does DI solve?
47. What are the types of dependency injection Spring Supports?
48. Which one is preferred and why?
49. Why is constructor injection recommended?
50. what happens if a dependency is missing?
51. can constructor injection cause circular dependency?
52. why is field injection discouraged?
53. is field injection ever acceptable?
54. How does spring handle circular dependency?
55. Why constructor injection fails?
56. How would you redesign to avoid circular dependency?
57. What is @Qualifier and why is it used?
58. Difference between @Primary and @Qualifier?
59. Can Spring inject optional dependencies?
60. when should dependencies be optional?
# Autowiring
61. How does Spring resolve dependencies during autowiring?
62. Which takes precedence `@Qualifier` or `@Primary`?
63. Difference between `@Qualifier` and `@Primary`?
64. when you would prefer `@Qualifier`?
65. is `@Autowired` mandatory for constructor injection?
66. What if there are multiple constructors?
67. How do you inject optional dependencies?
68. which approach is recommended?
69. How does spring inject multiple implementations of an interface?
70. How are beans ordered in a list?
71. What happens if Spring cannot resolve a dependency?
72. why is failing fast important?
73. can autowiring cause circular dependencies?
74. why constructor injection fails for circular dependencies?
# Scope
75. What is default scope in Spring?
76. When is the singleton bean created?
77. Difference between singleton and prototype scope?
78. Does Spring manage the full lifecycle of prototype beans?
79. Does Spring calls destroy methods for prototype beans?
80. Who is responsible for cleanup?
81. What are request and session scopes?
82. Are these scopes available in non-web apps?
83. Are singleton beans thread-safe?
84. How do you design thread-safe singleton beans?
85. What happens if a prototype bean is injected into a singleton?
86. How can it be solved?
87. When would you use prototype scope?
88. Why is prototype rarely used?
# Scope Solutions
89. Why does prototype injection into singleton not work as expected?
90. When exactly does dependency injection happen?
91. How do you get a new prototype bean everytime in a singleton?
92. Which approach is preferred and why?
93. What is `objectProvider`?
94. Difference between `ObjectProvider` and `ObjectFactory`?
95. What is `@Lookup` how does it work?
96. why it is not commonly recommended?
97. What are scoped proxies
98. When are scope proxies required?
99. Which solution would you use in production for prototype beans?
100. can it handle optional beans?
# Configuration
101. Difference between @Configuration and @Component?
102. What happens if @Bean methods call each other?
103. What is Full vs Lite configuration?
104. Which one guarantees singleton behavior?
105. What is proxyBeanMethods?
106. When should it be set to false?
107. Why should you avoid calling @Bean methods directly?
108. When should you use @Bean instead of @Component?
# Auto-Configuration
109. What is Spring Boot auto-Configuration
110. is auto-configuration mandatory?
111. How does Spring know what to auto-Configure?
112. What was used before spring boot 2.7?
113. What is `@EnableAutoConfiguration`?
114. What does `@ConditionalOnMissingBean` do?
115. why is this important?
116. How do you debug auto-Configuration issue?
117. Can auto-configuration be disabled?
# main()
118. What happens when Spring boot application starts?
119. When are beans actually created?
120. When does auto-configuration happen?
121. When are `@PostConstruct` methods called?
122. When are AOP proxies created?
123. Where do circular dependency errors occur?
124. How do you run logic after application fully ready?
# Events
125. What are Spring events?
126. Are Spring events synchronous or asynchronous by default?
127. How does Spring event publishing work internally?
128. How are listeners discovered?
129. What is the default execution model of Spring events?
130. How do you make them asynchronous?
131. What problem does @TransactionalEventListener solve?
132. What happens if the transaction rolls back?
133. Difference between @EventListener and @TransactionalEventListener?
134. When should you avoid Spring events?
# Profiles
135. What is a Spring profile?
136. How do you activate a profile?
137. Difference between profiles and properties
138. Can profile affect property loading?
139. What id the property resolution order in Spring Boot?
140. why is this important?
141. What happens if no profile is active?
142. What is the default profile?
143. When should you use `@profile` vs property-based config?
144. Difference between `@VALUE` and `@ConfigurationProperties`?
# Transaction
145. How does `@Transactional` work internally?
146. What happens if there is no proxy
147. Why doesn't `@Transactional` work on private methods?
148. What about self-invocation?
149. What is transaction propagation?
150. Difference between REQUIRED and REQUIRED_NEW?
151. When doe spring rollback a transaction?
152. How do you roll back on checked exceptions?
153. Can transactions work with async methods?
154. How do you handle such cases?
# Spring JPA
155. What is persistence context?
156. How many persistence context per transaction?
157. What is dirty checking?
158. Why does LAzyInitializationException occur?
159. Because lazy-laoded associations are accessed after the persitence context is closed
160. How do you solve it properly?
161. What is the N+1 select problem?
162. ho do you prevent it?
163. What is Open Session in View?
164. should it be enabled in production?
165. 