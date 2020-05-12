package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;

import java.util.*;

public class Game {

    private final String lobbyId;
    private GameRound currentGameRound;
    private final GameLength gameDuration;
    private final List<Player> listOfPlayers;
    private final int maxPoints;
    private Player firstPlayer;
    private final List<Player> winners;
    private Timer timer;

    private final GameService gameService;

    public Game(String lobbyId, GameLength gameDuration) {
        this.gameService = GameService.getInstance();
        this.lobbyId = lobbyId;
        this.gameDuration = gameDuration;
        this.listOfPlayers = PlayerService.getInstance().getPlayersInLobby(lobbyId);
        this.firstPlayer = listOfPlayers.get(0);
        this.maxPoints = calculateMaxPoints();
        this.winners = new ArrayList<>();
    }

    public GameRound getCurrentGameRound() {
        return this.currentGameRound;
    }

    public void startGame() {
        this.currentGameRound = new GameRound(this, this.lobbyId, this.listOfPlayers, this.firstPlayer);
        this.currentGameRound.startGameRound();
    }

    private void startNewGameRound() {
        this.gameService.sendStartGameRound(this.lobbyId);
        this.currentGameRound = new GameRound(this, this.lobbyId, this.listOfPlayers, this.firstPlayer);
        this.currentGameRound.startGameRound();
    }

    public void endGameRound(Player playerWithMaxPoints, Map<String, Integer> changes, String icon, String message) {
        setFirstPlayer(playerWithMaxPoints);
        removeCardsFromHands();
        if (!gameOver()) {
            this.gameService.sendEndRound(this.lobbyId, this.listOfPlayers, changes, calculateMaxPoints(), 20, icon, message);
            startTimer(20);
        }
        else {
            this.gameService.sendEndGame(this.lobbyId, this.listOfPlayers);
            onGameOver();
        }
    }

    private void onGameOver() {
        for (Player player : this.listOfPlayers) {
            player.setPoints(0);
        }
        GameRepository.removeGame(this.lobbyId);
    }

    //Removes all cards from the players hands
    private void removeCardsFromHands() {
        for (Player player : this.listOfPlayers) {
            player.clearHand();
        }
    }

    private Map<String, Integer> getScores() {
        Map<String, Integer> mappedPlayers = new HashMap<>();
        for (Player player : this.listOfPlayers) {
            int points = player.getPoints();
            mappedPlayers.put(player.getUsername(), points);
        }
        return mappedPlayers;
    }

    private boolean gameOver() {
        Map<String, Integer> scores = getScores();
        if (Collections.max(scores.values()) >= this.maxPoints) {
            calculateWinners(scores);
            return true;
        }
        return false;
    }

    private void calculateWinners(Map<String, Integer> scores) {
        //Calculate smallest number of points some player has
        int minPoints = Collections.min(scores.values());

        //Add all players with minPoints to winners-list
        for (Player player : this.listOfPlayers) {
            if (player.getPoints() == minPoints) {
                this.winners.add(player);
            }
        }
    }

    //The first player is the player to the right of the player who shuffles the cards
    private void setFirstPlayer(Player playerWhoShuffleCards) {
        int playersIndex = this.listOfPlayers.indexOf(playerWhoShuffleCards);
        playersIndex = (playersIndex + 1) % this.listOfPlayers.size();
        this.firstPlayer = this.listOfPlayers.get(playersIndex);
    }

    private int calculateMaxPoints() {
        int numOfPlayers = this.listOfPlayers.size();
        if (numOfPlayers <= 4) {
            if (this.gameDuration == GameLength.SHORT) {
                return 137;
            }
            else if (this.gameDuration == GameLength.MEDIUM) {
                return 154;
            }
            else {
                return 179;
            }
        }
        else {
            if (this.gameDuration == GameLength.SHORT) {
                return 113;
            }
            else if (this.gameDuration == GameLength.MEDIUM) {
                return 137;
            }
            else {
                return 154;
            }
        }
    }

    //If a player loses connection he/she is removed from the listOfPlayers
    public void playerLostConnection(Player player) {
        //in case the connection is lost during initialization
        if (this.currentGameRound != null) {
            this.currentGameRound.playerLostConnection(player);
        }
        this.removeFromPlayerList(player);
    }

    public void startTimer(int seconds) {
        int milliseconds = seconds * 1000;
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startNewGameRound();
            }
        };
        this.timer.schedule(timerTask, milliseconds);
    }

    private void removeFromPlayerList(Player player) {
        this.listOfPlayers.removeIf(p -> player.getIdentity().equals(p.getIdentity()));
    }
}
