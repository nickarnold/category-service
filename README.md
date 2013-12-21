# Asset Category Service

## Introduction

The LB Engineering team is starting to examine a possible architecture whereby individual services would run in their own container.  This container needs to support RESTful API calls.  We've looked at the [Dropwizard](http://dropwizard.codahale.com) in previous projects; this project attempts to show how as RESTful service with both HTML and JSON endpoints would be constructed.

## High Level Architecture
* [Dropwizard](http://dropwizard.codahale.com) -- Dropwizard provides glue code around several popular Java-based projects:  Jetty, Jersey, Hibernate, and others.  It has proven to be a straightforward, performant, and extensible project thus far.
* HTML UI -- Using [Mustache](https://github.com/spullara/mustache.java) templates and [Bootstrap](http://getbootstrap.com/) CSS
* JSON/HATEOAS API:  Using [Jersey](https://jersey.java.net/) for serving REST requests, [Jackson](http://jackson.codehaus.org/) for JSON marshalling, and [JAX-RS-HATEOAS](https://github.com/jayway/jax-rs-hateoas) for HATEOAS linking functionality.
* SQL Database -- Using [HSQL](http://hsqldb.org/) for development, but would move to something else in a production app.
* Messaging -- Plan is to integrate [RabbitMQ](http://www.rabbitmq.com/) into this project to publish and consume message with other services


## Running The Application

To run the application run the following commands.

* Download or fork this repository.

* Build the project:

        mvn package

* Run the server:

        java -jar target/category-service-0.1.0-SNAPSHOT.jar server config.yml

* Access the web page (in a browser):  [http://localhost:8899/categories](http://localhost:8899/categories)

* Access the JSON API (I use a Chrome app:  [Dev HTTP Client](https://chrome.google.com/webstore/detail/dev-http-client/aejoelaoggembcahagimdiliamlcdmfm?hl=en-US&utm_source=chrome-ntp-launcher))
        
        GET http://localhost:8899/categories (Accept:  application/json
        
  Follow the links from there.
        

