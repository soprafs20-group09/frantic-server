package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.EndRoundDTO;

import java.util.*;

public class Game implements Runnable {

    private GameLength gameDuration;

    private List<Player> listOfPlayers;

    private int maxPoints;

    private Player firstPlayer;

    private List<Player> winners;

    public Game(GameLength gameDuration, List<Player> listOfPlayers) {
        this.gameDuration = gameDuration;
        this.listOfPlayers = listOfPlayers;
        this.firstPlayer = listOfPlayers.get(0);
        this.maxPoints = calculateMaxPoints();
    }

    @Override
    public void run() {
        while (!gameOver()) {
            StartNewGameRound();
            changeFirstPlayer();
        }
    }

    private void StartNewGameRound() {
        GameRound currentGameRound = new GameRound(listOfPlayers, firstPlayer);
        currentGameRound.initializeGameRound();
        currentGameRound.startGameRound();
    }

    private Map<String, Integer> getScores() {
        Map<String, Integer> mappedPlayers = new HashMap<>();
        for (Player player : listOfPlayers) {
            int points = player.getPoints();
            mappedPlayers.put(player.getUsername(), points);
        }
        return mappedPlayers;
    }

    private boolean gameOver() {
        Map<String, Integer> scores = getScores();
        if (Collections.max(scores.values()) >= maxPoints) {
            calculateWinners(scores);
            return true;
        }
        return false;
    }

    private void calculateWinners(Map<String, Integer> scores) {
        //Calculate smallest number of points some player has
        int minPoints = Collections.min(scores.values());

        //Add all players with minPoints to winners-list
        for (Player player : listOfPlayers) {
            if (player.getPoints() == minPoints) {
                this.winners.add(player);
            }
        }
    }

    private void changeFirstPlayer() {
        int playersIndex = this.listOfPlayers.indexOf(this.firstPlayer);
        playersIndex = (playersIndex + 1)%listOfPlayers.size();
        this.firstPlayer = listOfPlayers.get(playersIndex);
    }

    private int calculateMaxPoints() {
        int numOfPlayers = this.listOfPlayers.size();
        if (numOfPlayers <= 4) {
            if (gameDuration == GameLength.SHORT) { return 137;
            } else if (gameDuration == GameLength.MEDIUM) { return 154;
            } else { return 179; }
        } else {
            if (gameDuration == GameLength.SHORT) { return 113;
            } else if (gameDuration == GameLength.MEDIUM) { return 137;
            } else { return 154; }
        }
    }
}
