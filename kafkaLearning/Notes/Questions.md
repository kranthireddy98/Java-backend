1. Explain Kafka
2. What is the difference between producer and consumer?
3. Can kafka work with only one broker?
4. if multiple consumers read the same topic, will data be duplicated?
5. Why are kafka messages immutable?
6. Why does kafka use partitions?
7. is message ordering guaranteed in kafka?
8. what is an offset?
9. How does kafka achieve parallelism?
10. What happens if consumers are more than partitions?
11. Why are keys important in kafka?
12. How does kafka decide partition a message goes to?
13. What happens if a producer sends messages without a key?
14. Explain acks=all. why is it important?
15. Can Kafka producer create duplicate messages?
16. How does Kafka achieve high throughput on producer side?
17. When would you choose acks=0?
18. Why does Kafka use pull instead of push?
19. what is consumer lag and why is it important
20. Who manages offsets in kafka?
21. What happens if a consumer crashes before committing offset?
22. can multiple consumer read the same data?
23. What happens if consumer doesn't call poll() frequently?
24. Why can only one consumer read a partition in a group?
25. What happens if consumers > partitions?
26. How does kafka support multiple services reading same data?
27. what is group id used for?
28. How do you increase consumer parallelism
29. What happens if two different applications use the same Group ID?
30. What is Kafka rebalancing?
31. Why is rebalancing harmful?
32. What triggers a rebalance?
33. How do you reduce rebalance impact?
34. What happens if consumer processing takes too long?
35. What is static membership?
36. How does Kafka prevent data loss?
37. What is ISR and why is it important?
38. What happens if leader broker crashes?
39. Why combine acks=all with min.insync.replicas?
40. Can Kafka lose data even with replication?
41. What determines Kafka delivery semantics?
42. Why is at-least-once preferred?
43. Explain at-most-once vs at-least-once
44. Where are Kafka offsets stored?
45. how do you handle duplicate messages?
46. Can Kafka guarantee exactly-once?
47. How does Kafka store messages on disk?
48. Why is Kafka fast even though it uses disk?
49. Does kafka delete messages after consumption?
50. What is log compaction?
51. What happens when retention expires?
52. Why did kafka originally use Zookeeper?
53. What problems did Zookeeper introduce?
54. What is KRaft?
55. Is Zookeeper still required for Kafka?
56. What role does the Kafka controller play?
57. How do you produce Kafka messages in Spring Boot?
58. Why should you always send a key?
59. How do you publish JSON messages?
60. Is KafkaTemplate synchronous?
61. How do ou handle producer failures?
62. How does spring boot consume Kafka messages?
63. How do you control offset commits in Sping Kafka?
64. What does `concurrency` mean in @KafkaListener?
65. What happens if an exception occurs in @kafkaLsitenet?
66. Why is idempotency important for consumers?
67. What happens if a consumer crashes before committing offset?
68. What is a poison message?
69. How do you prevent infinite retries?
70. Why is idempotency important in Kafka consumers?
71. Is exactly-once necessary in all systems?
72. What is a Dead Letter topic?
73. Why is DLT important?
74. When should messages go to DLT?
75. Does Kafka automatically support DLT?
76. What happens to offsets when a message is sent to DLT?
77. What does Kafka's exactly-once guarantee actually mean?
78. Can Kafka guarantee exactly-once when writing to a database?
79. What is the role of transactional.id?
80. Is idempotence enough for exactly-once?
81. When should you use Kafka transactions?
82. How do you increase Kafka throughput?
83. How do you reduce Kafka Latency?
84. What happens if processing takes longer than max.poll.interval.ms?
85. Why is compression useful?
86. Does increasing threads always improve Kafka performance?
87. 






































