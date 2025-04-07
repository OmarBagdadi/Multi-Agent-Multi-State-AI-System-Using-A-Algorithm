# User Guide

## Running the project

In order to run the project navigate to desktop --> build --> libs from there if you interested in seeing the logs open cmd at this location and enter the following command.
```
java -jar desktop-1.0.jar 
```
if you just want to run the project you can double click the desktop-1.0.jar file and it should run.

One thing to not in order to run the program you will be required to have a JRE installed on your system.

## Importing the project into eclipse

In order to import the project into eclipse. Once in eclipse navigate to File --> Import --> Gradle --> Existing gradle project then select the submission folder and select finish. This will import the project into eclipse. If you want to run the project from eclipse navigate to desktop --> src --> DesktopLauncher and run that.

## Level Creation 

If you want to create your own custom level overrite the text file under MazeGame --> assets --> levels --> Level1.txt with your own maze but ensure when creating your maze the following characters represent objects in the game:

- P - represents the Treasure Hunter Agent
- \# - represents a wall
- . - represents a path
- X - represents a Treasure
- G - represents the goal

Your level should only contain one Treasure Hunter Agent (P) and Goal (G) you may have multiple Walls(\#) , Treasures(X) and Paths (.). Not that you can only play your level if you run the program through eclipse.

## LibGDX Wiki

If you having any further issues refer to the [LibGdx Wiki](https://libgdx.com/wiki/) 