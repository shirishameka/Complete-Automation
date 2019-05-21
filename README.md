# Rest Client

Used tools and frameworks
Http client
Maven repository
TestNG
JsonNode,JsonObject for json processing
Main features


1. Hybrid framework to test Webservices. It is keyword-driven and data-driven framework.
So, you can separate testing data in Excel sheet with predefined schema and using some keywords
inside Excel sheet you can validate the returned response body, headers and cookies.


2. All classes and methods are implemented in Java with Maven repository to include all dependencies
needed.HTTP client is used to make an http api call


3. Utilizes the capabilities of TestNG such as Data provider annotation to separate test data in external
file and flexible test suites configuration and management. Also, TestNG generates 2 types of reports, HTML
and XML reports. The HTML reports are very descriptive with good
statistics and the JUnit XML reports that can be integrated with Jenkins after
test execution to have summary status of each deployment.


4. The framework validates the returned status code, response body, headers and cookies. It can validate
each field data type and value. If the returned response includes object of arraylist,
the framework can validate its size using the keyword ".size()"