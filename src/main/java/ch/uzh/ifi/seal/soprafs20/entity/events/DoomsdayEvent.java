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

public class DoomsdayEvent implements Event {

    private final Game game;
    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;
    private final Player currentPlayer;

    public DoomsdayEvent(Game game, GameRound gameRound) {
        this.game = game;
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
        this.currentPlayer = gameRound.getCurrentPlayer();
    }

    public String getName() {
        return "doomsday";
    }

    public void performEvent() {
        Map<String, Integer> changes = new HashMap<>();
        for (Player player : listOfPlayers) {
            changes.put(player.getUsername(), 50);
            player.setPoints(player.getPoints() + 50);
        }

        String message = "Doomsday killed everyone!";
        this.game.endGameRound(currentPlayer, changes, "event:doomsday", message);
    }

    public String getMessage() {
        return "Rest in peace, everyone is dead. Well, not really, since it's just a game. The round is over and everyone's points increase by 50.";
    }

}
