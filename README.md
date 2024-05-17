### Coding Challenge - Order Management

### Part 1 - Order Management Application

The task is to build an Order Management system able to calculate instrument prices and trade with clients.

Please see the attached PDF under /resources/CodingChallenge-OrderManagement.pdf for the requirement

### Assumptions made on top of the given requirements
1. "Orders with the same symbol can come from many different sources" - an additional field source would be necessary
2. "the application should maintain some state"
        - the application should return to the same or previous state upon restart and should be recoverable - example if a price is calculated for a client, upon restart ofg the application the trade processing should continue from that step (nice to have)
        - individual unique requests should be served only once: example 1 new order should only create 1 order (even after retrials/restarts)

### Getting Started
1. Clone and gradle clean build the repository
2. Using any IDE and after Setting up SDK you can open the project and run OrderManagementApplication.java as an application

### In Memory Database
I have used H2 as in memory persistent storage. I am writing to a file so that when application is restarted the state is maintained.

![Alt text](/images/H2InMemory.png?raw=true "H2 In Memory")

### Helpful commands
java -cp <location>\h2-2.2.224.jar org.h2.tools.Shell -url "jdbc:h2:file:./src/test/resources/db/testOrderManagementDb" -user "developer" -password "<replace_password>"

### TestCases

![Alt text](/images/TestCasesExecution.png?raw=true "TDD Approach")

### REST API Request Responses

![Alt text](/images/AddNewOrder.png?raw=true "Add New Order")

![Alt text](/images/RemoveOrder.png?raw=true "Remove Order")

![Alt text](/images/ModifyOrder.png?raw=true "Modify Order")

![Alt text](/images/OrderNotFound.png?raw=true "Order Not Found")

![Alt text](/images/CalculatedPrice.png?raw=true "Calculate Price")

![Alt text](/images/PlacedTrade.png?raw=true "Placed Trade")

![Alt text](/images/BalanceAfterTrade.png?raw=true "Balance After Trade")

### Future Improvements
1. This app takes on an average ~10-20s to serve the different requests. There is scope for performance improvements by introducing a caching layer.
2. Order Book itself should be persisted
3. Fix the issues with Mock in TestOrderManagerController

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.5/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.5/gradle-plugin/reference/html/#build-image)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)


### Part 2 - Order Management Application Infrastructure on AWS

![Alt text](/images/CCIntHighLevelDesign.png?raw=true "Order Management AWS Infrastructure")

This design considers single region multi-AZ setup. To serve requests across the globe under < 5ms we can provision multi-region multi-AZ model supported by Network Load Balancer.

1. On Premise applications can push in order file(s) using S3 API into specific raw S3 Bucket via S3 Interface Endpoint. An alternative is Direct Connect. Using specific IAM roles and STS tokens we can secure the on premise interactions with the S3 (and other required services in general). This also helps with online load of Orders to S3 and eventually load these orders into Order Management Application.
2. Using Private Link we can provision the S3 interface Endpoint to facilitate on premises interaction with S3 buckets.
3. We can define and use KMS keys to encrypt data onto S3 and across other AWS services. The KMS keys aLong with S3 bucket policies help us secure the data in transit and at rest.
4. Users or On Premise apps can invoke the Order Management System application rest end points via API Gateway. API Gateway helps expose the Order Management APIs to internet.
5. The requests go through an authorizer lambda to ensure only authenticated and authorized requests go through. With API Gateway API Caching (including authorizer) enabled, the endpoint responses are cached to help reduce the number of calls.  
6. The application load balancers across the Availability Zones help distribute the load across multiple AZs. Also, in the event of downtime of one AZ, the traffic gets routed to the other AZ and helps us avoid any application downtime. Having distributed application across multiple AZs also helps us perform updates with zero downtime.
7. The ElastiCache helps accelerate application performance. It can also be integrated with AWS RDS. 
8. We can persist order and trade or other relational data onto AWS RDS with Postgres engine. Setting up primary and standby AWS RDS with replication enabled along with CQRS pattern could help us achieve low latency and serve requests well under 5ms response times.
9. S3 VPC Gateway endpoint helps us to interact with S3 from within our VPC. Example, AWS RDS could archive data to S3 with appropriate storage classes.  
10. We can use DynamoDB for auditing, configuration or in use cases where schema is not necessary.
11. We could leverage Athena on top of S3 to query the S3 data catalogue. Glue helps us manage the data catalogue along with any ETL that could be necessary on the raw data. Lake formation could help manage the data lake as the application data grows. Lambda could help with petty tasks. Cloud watch to store logs and manage event bridge etc.
12. We can leverage event driven architecture with lambda and SQS along with Step Functions to manage any workflows. Example, depending on the state of the Order Book or Order, we could initiate specific functionalities such as placing trade and upon trade completion event trigger an update to the client report etc. 


### Possibilities are endless!