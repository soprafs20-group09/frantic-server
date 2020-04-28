package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DoomsdayEvent implements Event {

    private final Game game;
    private final List<Player> listOfPlayers;
    private final Player currentPlayer;

    public DoomsdayEvent(Game game, List<Player> listOfPlayers, Player currentPlayer) {
        this.game = game;
        this.listOfPlayers = listOfPlayers;
        this.currentPlayer = currentPlayer;
    }

    public String getName() {
        return "doomsday";
    }

    public List<Chat> performEvent() {
        for (Player player : listOfPlayers) {
            player.setPoints(player.getPoints() + 50);
        }
        this.game.endGameRound(currentPlayer);
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:doomsday", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "Rest in peace, everyone is dead. Well, not really, since it's just a game. The round is over and everyone's points increase by 50.";
    }

}
