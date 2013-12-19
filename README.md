# Exec Server 2.0 -- Webified!

## Introduction

The LB exec-server 1.0 application is an extremely simple socket-based remote-process exector.  The basic architecture is as follows:

* Simple main method runs and accepts incoming socket connections over a custom port.
* The incoming request schema is defined in an XSD and hydrated via XMLBeans.
* Once a valid request is accepted via the inbound socket connection, a thread is spun up to handle it.
* The thread reads the request data and finds a "command key", which is a simple name mapped to an executable path
	* Example:  command key: "say", executable path: "/usr/bin/say"
* The thread executes the path as a Java Process and waits for a response on stdout and stderr.
* Optionally, the request can receive a stream of stdout/stderr back through the socket connection
* Once the process completes, a full response is sent back over the socket, containing the process return code and stdout/stderr body.

Data structure conformance comes from an XSD, using XMLBeans to construct the data transfer objects.

The 1.x release has served LB well over several years now-- it is in use at several high profile production customers for a variety of remote command line needs.

However, more recent customer engagements have found the current architecture to be lacking for their requirements.  Examples include:

* Desire to cluster command executions intelligently across multiple exec servers.  Right now clustering is very dumb from RE's point of view-- it's a simple randomization of available servers per request.
* Desire to leverage the exec server as more of a "user agent"-- ie, the ability to configure an exec-server as being on a user's machine, and have it execute commands on the user's behalf on their workstation.
* Desire to have a user interface on the exec-server, complete with a command status view, administration ability, etc
* Desire to configure maximum concurrent executions by command.  For instance, the admin might want to be able to run 2 ffmpeg commands and 5 mediainfo commands concurrently.

Given short- to medium-term needs of customers in these areas, a 2.0 version of exec-server has been started.  This document will detail the goals of this version and its basic architecture.


## High Level Architecture
Exec-Server 2.0 intends to be a web application.  Our direction is to use [Dropwizard](http://dropwizard.codahale.com) as the underlying web framework.  We like Dropwizard because it is a very lightweight glue framework-- it is not trying to reinvent the wheel, it just straps together several best-of-breed wheels together into an easy-to-use package.

Along with dropwizard, we are experimenting with using WebSockets to emulate the as-it-happens socket streaming of 1.0.  We are currently looking to use [Atmosphere](https://github.com/Atmosphere/atmosphere) as the Java WebSocket implementation.  The idea is that Reach Engine, or a web client, could "subscribe" to a command execution URL and obtain updates as they happen.

Also important is the decoupling of a "command" from a command-line process call.  While a process command is still very important (and initially the vast majority of calls made), it is our intent to allow any code that implements a Command interface to be run within the exec server.  This provides a more scalable execution environment for all sorts of commands that Reach Engine often runs inside its VM today, such as FTP transfers.





## Running The Application

To test the example application run the following commands.

*   To package the example run.

        mvn package

*   To run the server run.

        java -jar target/dropwizard-example-0.6.2.jar server example.yml

To test chat, which purposely resides in its own directory to excercise CORS

*   Browse the file

        app/chat.html

* To test the bad word filter, enter any message with ".NET" and it will be replaced with "***" :)


