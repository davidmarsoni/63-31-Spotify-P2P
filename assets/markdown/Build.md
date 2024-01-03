# How to Build The Project

## Requirements

Before we start to build our project you need to have Java 17+ and Maven installed. Here are two links:

- [Java official download page](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven official download page](https://maven.apache.org/download.cgi)

For help here is a tutorial for installing Maven in Windows

- [How to Install Maven on Windows](https://phoenixnap.com/kb/install-maven-windows)


## Build

Before build our project re-open your IDE if you have installed Maven or java after opening your IDE.

To build our project you need to run the following command:

```bash
mvn clean package #In the root folder of your project
```

n.b. You need to be in the root directory of the project to run this command. This command will first clean the build directory and after that build the project.

The generated jar file will be located in the target folder of your project. This project will have the following name: `62-31-Spotify-P2P-jar-with-dependencies.jar`
