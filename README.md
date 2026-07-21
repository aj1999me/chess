# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

Sequence Diagram: https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmyyKp8izfL8-yAmMSxXKBgpAf6IzKUR8xqSCGk7HsOmYAZ8K+s6XYwAgQnihignCQSRJgKSb6GLuNL7gyTJTipXI3gFd5LsKMBihKboynKZbvEqmAqsGGoAHIQDAqg+CA+pvOFvKReJVBIjAPZ9tufmlf6ACSaBUCaSAcG6nJRjGcaFHpSaVKJaZ4UpYw5qoeamYWxbQPUejriihJqGA1n0UVC4Cr1HZlS6G7uluvmCsO-L1E1yAcE0+iqNO5ZzGsFkAnOEUOlF9RwPEKAgBqt0MWl6owAA6t4DgwJ9K37iBDn1P9BSOMDe3raBEMA-YZ1bDsHUoLGCnofCybIKmMDpgAjENI1jQWYxFiW9Q+NMl7QEgABeKC7DAdFNgdi6leVcDQCi4BA+dmnVftt6HTAh5yCgz7xOel7Xuza2VMuAZrgGsu7Q57a2fU7nihkuUIIBjzAXD1SGcZV2mdpsGXlR9ZqSzWOYbj2EwLh+GjObyWqVb5E24hdvaQ7y2eN4fj+F4KDoDEcSJBHUfub4WCiYK8ONNIEb8RG7QRt0PRyaoCnDBRtvdabdkPLC-rF-7hS2Sn4NOUJiduU3p6eQtPka-5xWixwKDcMeftITL8E1-dPeLor0UvW9GrV0hqWqhqzI03qdOM+uoAjqYy3y-e0XSP3TKGPP6BC+tWsCa3YB6wBNlGxhJsSaWKL61gPU4yUYA4XhjYMSHzF-AonXP4bA4oNT8TRDAAA4kqDQydaqlgaNArOud7BKiLkPM+PVKiX1PrXB+5dfL1GQDkduOQR6UTHufKkIt6ROTRLAnMlCS7j1WvvZ6r13rGiwYUb6y9V7xHXkzY0+4-4g0ilPIKYAmFqBYdQ2GH5CEMjgbfA299K7Y3ss-FRcxIE5EdutfqrtBoezGOgnMBYGjjAsSgOq0gCwE3CMEQIKxhjLE2PEXUKA2qQTMjAYYYxkigDVL4723xbEZSVGpC4MBOis3-kxMOHAADsbgnAoCcDECMwQ4BcQAGzwAnIYWRMAijOzEtomo9QpIdDQRgwRJchqRL8YHXSZd9LKJgC0uYGivz107PUcW6JZEYh6WFWGtCHr0OGSgUZ4y2GgyemLLhaopbyKQosyRQppHzKVFEuYpJjDtnlt2Xs-Y97LJDBwPZcwDlhUuZzLa58lGaPqNctRhtNFgx0TAIy5ilT2Mcc4wIDsP59QqT-d2-zbFAvqE4lxQcmwALDpYfuzlNjRyQAkMAaK+wQExQAKQgOKGBFZ-DBPeuUr+AzqmNCaMyGSPRbGYNHkhLM2AEDADRVAbmzkoBrFhdIdpz9OlvL+esLlPK+XQD2L9FgdVs49AAEL8QUHAAA0qsdxAK5hwpgAi0F0JCG0vKgAKxJWgUZIAID8sFYC6QndOzC2maOFZs91n4K2Y9KR7r3oCnwTQmQdC3X5VnrcuxjqJE+p2X6jUSAABm3SKxWhtEG05caZVQAxDa-l3rJ6xrDdwxNYtbXQBgNkGACaURiEeVU8qLzcFdItbrf86i65PzpSMEViZP54zdlmBJjFQ4BC8NyrFOKx3ykQMGWAwBsCcsIHkAoZSEFVMkunTO2dc7GEMU28VIBuB4GkMqjEpIO31q2oe2dJ6z3ppDUMo97pT35oVrGw+A9DCBn7Io-dX5H2zs+X042iDkRtvfh0oxkKTFOHEUAA

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
