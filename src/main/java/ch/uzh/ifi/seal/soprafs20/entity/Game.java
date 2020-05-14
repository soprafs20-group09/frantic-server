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
    private List<Player> listOfPlayers;
    private final int maxPoints;
    private Player firstPlayer;
    private Timer timer;

    private GameService gameService;

    public Game(String lobbyId, GameLength gameDuration) {
        this.gameService = GameService.getInstance();
        this.lobbyId = lobbyId;
        this.gameDuration = gameDuration;
        this.listOfPlayers = PlayerService.getInstance().getPlayersInLobby(lobbyId);
        this.firstPlayer = listOfPlayers.get(0);
        this.maxPoints = calculateMaxPoints();
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
            message = message + " Watch everyone's standings and wait for the next round to start!";
            this.gameService.sendEndRound(this.lobbyId, this.listOfPlayers, changes, this.maxPoints, 20, icon, message);
            startTimer(20);
        }
        else {
            message = message + " The game is over. See who won below and challenge them to a rematch!";
            this.gameService.sendEndGame(this.lobbyId, this.listOfPlayers, changes, icon, message);
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
        return Collections.max(getScores().values()) >= this.maxPoints;
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

    private void startTimer(int seconds) {
        int milliseconds = seconds * 1000;
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startNewGameRound();
            }
        };
        timer.schedule(timerTask, milliseconds);
    }

    public void stopTimer() {
        this.timer.cancel();
        this.timer.purge();
    }

    private void removeFromPlayerList(Player player) {
        this.listOfPlayers.removeIf(p -> player.getIdentity().equals(p.getIdentity()));
    }

    public int getMaxPoints() {
        return this.maxPoints;
    }

    public Player getFirstPlayer() {
        return this.firstPlayer;
    }

    //needed for testing
    public void setListOfPlayers(List<Player> playerList) {
        this.listOfPlayers = playerList;
    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }
}
