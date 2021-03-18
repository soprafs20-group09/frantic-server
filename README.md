<h1 align="center">
  <br>
  <a href="https://github.com/soprafs20-group09"><img src="src/main/assets/logo-hollow.svg" alt="Frantic" width="200"></a>
  <br>
  Frantic-Server
  <br>
</h1>

<p align="center">
  <a href="https://github.com/soprafs20-group09/frantic-server/actions">
    <img src="https://github.com/soprafs20-group09/frantic-server/workflows/Deploy%20Project/badge.svg">
  </a>
  <a href="https://heroku-badge.herokuapp.com/?app=sopra-fs20-group-09-server"><img src="https://heroku-badge.herokuapp.com/?app=sopra-fs20-group-09-server"></a>
  <a href="https://sonarcloud.io/dashboard?id=soprafs20-group09_frantic-server">
      <img src="https://sonarcloud.io/api/project_badges/measure?project=soprafs20-group09_frantic-server&metric=coverage">
  </a>
  <a href="https://sonarcloud.io/dashboard?id=soprafs20-group09_frantic-server">
        <img src="https://sonarcloud.io/api/project_badges/measure?project=soprafs20-group09_frantic-server&metric=alert_status">
    </a>
</p>

## Introduction

Frantic is a chaotic card game created by [Rulefactory](https://rulefactory.ch). This repository is the back-end part of an online implementation for this card game.

Please find the current build under [frantic.online](http://frantic.online/).

## Technologies

The server is written in Java using the Spring Boot framework. JPA is used for persistence and deployment is handled by Heroku.

To establish a connection between the front- and backend REST is used. When further proceeding in the game (joining a lobby), a websocket connection gets established.

## High-level Components

The [GameController](src/main/java/ch/uzh/ifi/seal/soprafs20/controller/GameController.java) receives all game-related packages and passes them to the [GameService](src/main/java/ch/uzh/ifi/seal/soprafs20/service/GameService.java). The GameService then reads the packages and assigns the tasks to the Game and GameRound.

The [Game](src/main/java/ch/uzh/ifi/seal/soprafs20/entity/Game.java) class handles the overall state of the game (game starts, round starts, round or game ends etc). </br> The [GameRound](src/main/java/ch/uzh/ifi/seal/soprafs20/entity/GameRound.java) class manages the individual rounds. Which means it decides what happens when a (special) card is played, event gets triggered etc. The GameRound class is by far the largest class of the backend.


When starting the server the [Application](src/main/java/ch/uzh/ifi/seal/soprafs20/Application.java) class gets executed.

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

## Roadmap

1. Implement the expansion pack
2. Minor Bug fixes
3. Mobile Version?

## Authors and Acknowledgements

### Members of the SoPra-group 09 2020:

[Kyrill Hux](https://github.com/realChesta), [Jan Willi](https://github.com/JaanWilli), [Davide Fontanella](https://github.com/Davfon), [Remy Egloff](https://github.com/regloff), [Sina Krumhard](https://github.com/sei-nah)

### Acknowledgements

First of all we want to thank Rulefactory for creating such an amazing game. We all had a fun time implementing the game, and also learned a lot.
Further we would like to thank our tutor Moritz Eck, who always provided useful advice to get cleaner code and more user-friendliness. We are also very grateful for our friends and families for testing our game extensively and also providing improvements in terms of user-friendlyness.

## License
Apache License 2.0
