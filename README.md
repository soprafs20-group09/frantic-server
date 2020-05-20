# Frantic-Server
[![Deployment Status](https://github.com/soprafs20-group09/frantic-server/workflows/Deploy%20Project/badge.svg)](https://github.com/soprafs20-group09/frantic-server/actions)
![Heroku](https://heroku-badge.herokuapp.com/?app=sopra-fs20-group-09-server)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=soprafs20-group09_frantic-server&metric=coverage)](https://sonarcloud.io/dashboard?id=soprafs20-group09_frantic-server)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=soprafs20-group09_frantic-server&metric=alert_status)](https://sonarcloud.io/dashboard?id=soprafs20-group09_frantic-server)

## Introduction

Frantic is a chaotic card game created by [Rulefactory](https://rulefactory.ch). This repository is the back-end part of an online implementation for this card game.

Please find the current build under http://frantic.online/.

## Technologies

To establish a connection between the front- and backend REST is used. When further proceeding in the game (joining a lobby), a websocket connection gets established.

## High-level Components

The [GameConroller](src/main/java/ch/uzh/ifi/seal/soprafs20/controller/GameController.java) recieves all game-related packages and passes them to the [GameService](src/main/java/ch/uzh/ifi/seal/soprafs20/service/GameService.java). The GameService then reads the packages and assigns the tasks to the Game and GameRound.

The [Game class](src/main/java/ch/uzh/ifi/seal/soprafs20/entity/Game.java) handles the overall state of the game (game starts, round starts, round or game ends etc). </br> The [GameRound](src/main/java/ch/uzh/ifi/seal/soprafs20/entity/GameRound.java) class manages the individual rounds. Which means it decides what happens when a (special) card is played, event gets triggerd etc. The GameRound class is by far the largest class of the backend.


When starting the server the [Application class](src/main/java/ch/uzh/ifi/seal/soprafs20/Application.java) gets executed

## Launch & Deployment

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

### Test

```bash
./gradlew test
```

### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`


## Roadmap

1. Implement the expansion pack
2. Minor Bug fixes
3. Moblie Version?

## Authors and Aknowledgements

### Members of the SoPra-group 09 2020:

Kyrill Hux,
Jan Willi,
Davide Fontanella,
Remy Egloff,
Sina Krumhard

### Aknowledgements

First of all we want to thank Rulefactory for creating such an amazing game. We all had a fun time implementing the game, and also learned a lot.
Further we would like to thank our tutor Moritz Eck, who always provided useful advice to get cleaner code and more user friendlyness. We are also very grateful for our friends and families for testing our game extensively and also providing improvements in terms of user friendlyness 
