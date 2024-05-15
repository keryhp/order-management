### Coding Challenge - Order Management

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

