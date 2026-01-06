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
75. 