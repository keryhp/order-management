### Coding Challenge - Order Management

The task is to build an Order Management system able to calculate instrument prices and trade with clients.

Please see the attached PDF under /resources/CodingChallenge-OrderManagement.pdf for the requirement

### Assumptions made on top of the given requirements
1. "Orders with the same symbol can come from many different sources" - an additional field source would be necessary
2. "the application should maintain some state"
        - the application should return to the same or previous state upon restart and should be recoverable - example if a price is calculated for a client, upon restart ofg the application the trade processing should continue from that step (nice to have)
        - individual unique requests should be served only once: example 1 new order should only create 1 order (even after retrials/restarts) 
3. 

### Getting Started


###Helpful commands
java -cp <location>\h2-2.2.224.jar org.h2.tools.Shell -url "jdbc:h2:file:./src/test/resources/db/testOrderManagementDb" -user "developer" -password "changeIt"

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.5/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.5/gradle-plugin/reference/html/#build-image)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

