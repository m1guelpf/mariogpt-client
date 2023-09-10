# MarioGPT Client

This repository contains the Java client for [my MarioGPT Cog](https://github.com/m1guelpf/cog-mariogpt). The client is designed to interact with [the MarioGPT model on Replicate](https://replicate.com/m1guelpf/mariogpt), providing a seamless interface between the game and the AI model.

## Getting Started

You can download a pre-built jar for the game on the [releases page](https://github.com/m1guelpf/mariogpt-client/releases/latest). You can then run it with `java -jar mariogpt-client.jar`. You will also need a Replicate API key, which you can get [here](https://replicate.com/account/api-tokens).

## Prerequisites

This project is built with Java, so you'll need to have a Java Development Kit (JDK) installed on your machine. You can download the latest version of the JDK [from Oracle's website](https://www.oracle.com/java/technologies/downloads/).

## Local Builds

After cloning the repository and installing the JDK, you can build the project using the provided Gradle wrapper. Run the following command in your terminal:

```
./gradlew build
```

This will compile the Java code and create a JAR file that you can run.

## Usage

To use the MarioGPT client, you'll need to run the JAR file that was created during the build process. You can do this by running the following command in your terminal:

```
java -jar build/libs/mariogpt-client.jar
```

This will start the MarioGPT client, and you can begin interacting with the MarioGPT model.

## License

This project is licensed under the terms of the MIT license. See the [LICENSE file](LICENSE) for more information.
