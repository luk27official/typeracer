## TypeRacer
This repository contains a very simple singleplayer remake of the game TypeRacer (available at https://typeracer.com) in Java.

### Game description
The main purpose of the game is to type the shown text without any mistakes the fastest the player can. The texts are available in the specified JSON file and the player may replace them with their own. The game measures the player's average typing speed (in WPM - words per minute), typing accuracy (in %) and time (in seconds) as well. The game provides the player a leaderboard, which is stored in a JSON file.

### Requirements
- Java 19
- Maven v3.8 or higher

### How to run
The recommended way to run the program is to compile it via Maven and then run.
To compile the program, run the following command in the project's root directory: `mvn clean compile`
To run the program, run the following command in the project's root directory: `mvn compile exec:java`

To create a JAR package, run the following command in the project's root directory: `mvn compile package`
You then may run the JAR package by running the following command in the project's root directory: `java -jar target/typeracer-1.0-SNAPSHOT-jar-with-dependencies.jar`

### Credits
- original game authors (https://typeracer.com)