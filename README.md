# Springboot-GraphQL
This example contains the basic integration of GraphQL in spring boot application using H2 database.
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Minimal [Spring Boot](http://projects.spring.io/spring-boot/) sample app.


## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Clone the application
```git clone https://github.com/deepak-kumbhar/Springboot-GraphQL.git```

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.example.graphql.SpringbootGraphQlApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

1. Get all blog list with author and title

Request: 
```{
getAllBlog{
author
title

}
}
```

Response:
```
{
    "data": {
        "getAllBlog": [
            {
                "author": "Deepak",
                "title": "Demo of springboot"
            },
            {
                "author": "Deepak",
                "title": "Demo of springboot graphql"
            },
            {
                "author": "Deepak",
                "title": "Demo of springboot kafka"
            }
        ]
    },
    "errors": [],
    "dataPresent": true,
    "extensions": null
}
```

2. Get blog by title only author and title

Request:
```
{
findBlogByTitle(title:"Demo of springboot"){
author
title
}
}
```

Response:
```
{
    "data": {
        "findBlogByTitle": {
            "author": "Deepak",
            "title": "Demo of springboot"
        }
    },
    "errors": [],
    "dataPresent": true,
    "extensions": null
}
```

