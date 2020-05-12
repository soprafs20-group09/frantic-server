package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinishLineEvent implements Event {

    private final Game game;
    private final GameRound gameRound;
    private final GameService gameService;

    private final List<Player> listOfPlayers;

    public FinishLineEvent(Game game, GameRound gameRound) {
        this.game = game;
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
    }

    public String getName() {
        return "finish-line";
    }

    public void performEvent() {
        int maxPoints = 0;
        Map<String, Integer> changes = new HashMap<>();
        Player playerWithMaxPoints = this.listOfPlayers.get(0); //to make sure playerWithMaxPoints is initialized in all cases
        for (Player player : listOfPlayers) {
            int playersPoints = player.calculatePoints();
            changes.put(player.getUsername(), playersPoints);
            player.setPoints(player.getPoints() + playersPoints);
            if (playersPoints >= maxPoints) {
                maxPoints = playersPoints;
                playerWithMaxPoints = player;
            }
        }
        String message = "Welcome at the finish line! Watch everyone's standings and wait for the next round to start!";
        this.game.endGameRound(playerWithMaxPoints, changes, "event:finish-line", message);
    }

    public String getMessage() {
        return "Looks like the round is over! Count your points!";
    }

}
