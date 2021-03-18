package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;
import ch.uzh.ifi.seal.soprafs20.constant.TurnDuration;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Game {

    Logger log = LoggerFactory.getLogger(Game.class);

    private final String lobbyId;
    private int roundCount;
    private GameRound currentGameRound;
    private final GameLength gameDuration;
    private final TurnDuration turnDuration;
    private List<Player> listOfPlayers;
    private final int maxPoints;
    private Player firstPlayer;
    private Timer timer;

    private GameService gameService;

    public Game(String lobbyId, GameLength gameDuration, TurnDuration turnDuration) {
        this.gameService = GameService.getInstance();
        this.lobbyId = lobbyId;
        this.roundCount = 1;
        this.gameDuration = gameDuration;
        this.turnDuration = turnDuration;
        this.listOfPlayers = PlayerService.getInstance().getPlayersInLobby(lobbyId);
        this.firstPlayer = listOfPlayers.get(0);
        this.maxPoints = calculateMaxPoints();
    }

    public GameRound getCurrentGameRound() {
        return this.currentGameRound;
    }

    public void startGame() {
        this.currentGameRound = new GameRound(this, this.lobbyId, this.listOfPlayers, this.firstPlayer, this.turnDuration);
        this.currentGameRound.startGameRound();
    }

    private void startNewGameRound() {
        this.gameService.sendStartGameRound(this.lobbyId);
        this.currentGameRound = new GameRound(this, this.lobbyId, this.listOfPlayers, this.firstPlayer, this.turnDuration);
        this.roundCount++;
        this.currentGameRound.startGameRound();
    }

    public void triggerNewGameRound() {
        Chat chat = new EventChat(null, "A new round starts in 10 seconds");
        this.gameService.sendChatMessage(this.lobbyId, chat);
        this.gameService.sendTimer(this.lobbyId, 10);
        startTimer(10);
    }

    public void endGameRound(Player playerWithMaxPoints, Map<String, Integer> changes, String icon, String message) {
        setFirstPlayer(playerWithMaxPoints);
        removeCardsFromHands();
        if (!gameOver()) {
            log.info("Lobby " + this.lobbyId + ": Round over");

            message = message + " Watch everyone's standings and wait for the next round to start!";
            this.gameService.sendEndRound(this.lobbyId, this.listOfPlayers, changes, this.maxPoints, icon, message);
            Chat chat = new EventChat(null, "The round is over!");
            this.gameService.sendChatMessage(this.lobbyId, chat);
        }
        else {
            log.info("Lobby " + this.lobbyId + ": Game over");

            message = message + " The game is over. See who won below and challenge them to a rematch!";
            this.gameService.sendEndGame(this.lobbyId, this.listOfPlayers, changes, icon, message);
            Chat chat = new EventChat(null, "The game is over!");
            this.gameService.sendChatMessage(this.lobbyId, chat);
            onGameOver();
        }
        this.gameService.sendReconnect(this.lobbyId);
        startReconnectTimer(7);
    }

    public int getRoundCount() {
        return this.roundCount;
    }

    private void onGameOver() {
        for (Player player : this.listOfPlayers) {
            player.setPoints(0);
        }
        GameRepository.removeGame(this.lobbyId);
        this.gameService.endGame(this.lobbyId);
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
        if (this.timer != null) {
            this.timer.cancel();
        }
    }

    private void startReconnectTimer(int seconds) {
        int milliseconds = seconds * 1000;
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                updatePlayerIdentities();
            }
        };
        timer.schedule(timerTask, milliseconds);
    }

    private void updatePlayerIdentities() {
        List<Player> newListOfPlayers = this.gameService.getPlayersInGame(this.lobbyId);
        Map<String, Player> mapOfPlayers = new HashMap<>();
        for (Player p : this.listOfPlayers) {
            mapOfPlayers.put(p.getUsername(), p);
        }
        for (Player newPlayer : newListOfPlayers) {
            Player oldPlayer = mapOfPlayers.get(newPlayer.getUsername());
            if (oldPlayer != null) {
                newPlayer.setPoints(oldPlayer.getPoints());
            }
            if (newPlayer.getUsername().equals(this.firstPlayer.getUsername())) {
                this.firstPlayer = newPlayer;
            }
        }
        this.listOfPlayers = newListOfPlayers;
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

    public List<Player> getListOfPlayers() {
        return this.listOfPlayers;
    }

    //needed for testing
    public void setListOfPlayers(List<Player> playerList) {
        this.listOfPlayers = playerList;
    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }
}
