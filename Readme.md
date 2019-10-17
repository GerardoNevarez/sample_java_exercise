# Java Exercise

### Description
The project implements a small REST API, to manage Contact resources

### Toolset
The project was developed using [Spring Boot](https://spring.io/projects/spring-boot) as the application framework, with initial scaffolding aided by [Spring Initializr](https://start.spring.io/). It uses an in-memory [H2](http://www.h2database.com/) database for data persistence with the support of [Hibernate](https://hibernate.org/) and [Spring Data JPA](https://spring.io/projects/spring-data-jpa).

### Build and Launch
We use [Gradle](https://docs.gradle.org) as the build tool, you can launch the project using the following command:

```
gradlew clean bootRun
```

A self-contained executable JAR file can be built for distribution as well:

```
gradlew bootJar
```

### Unit Testing
Unit and integration tests can be executed, inclusding creating test coverage reports, with the following command:

```
gradlew  clean test jacocoTestReport
```

Finally, there is a Postman collection file that can be imported for development testing, available in the `Postman` folder.
