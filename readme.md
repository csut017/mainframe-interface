# Mainframe Interface: Project 1 Starter

Welcome to your first project for SOFTENG 306. 

The project specification is available [on Canvas](https://canvas.auckland.ac.nz/courses/121698/pages/project-1-overview). The four deliverables for this project are:
* [Proposal](https://canvas.auckland.ac.nz/courses/121698/assignments/456190)
* [Presentation](https://canvas.auckland.ac.nz/courses/121698/assignments/456189)
* [Code](https://canvas.auckland.ac.nz/courses/121698/assignments/456187)
* [Documentation](https://canvas.auckland.ac.nz/courses/121698/assignments/456188)

## Important

Update this readme file to contain the names of each team member and your GitHub login.

* *Replace this text: Team Member Name 1 - GitHub Login*
* *Replace this text: Team Member Name 2 - GitHub Login*
* *Replace this text: Team Member Name 3 - GitHub Login*
* *Replace this text: Team Member Name 4 - GitHub Login*

You must also update this file to contain either the instructions for running your code or a link to the instructions.

## Initial Repository Code

This repository initially contains the code for the mainframe interface and the unit tests. The following code is supplied:
* [mainframe](src/main/java/mainframe/client/): the interfaces for sending requests to and receiving responses from the mainframe. You will need to use the [Connection](src/main/java/mainframe/client/Connection.java) interface in your code. We have provided an implementation that connects to a web server in [HttpConnection](src/main/java/mainframe/client/HttpConnection.java).
* [tests](src/test/java/mainframe/client/): the unit tests. Most of these connect to the web server, but there is an [example unit test](src/test/java/mainframe/client/SimulateListCustomers.java) for connecting to a stub.
