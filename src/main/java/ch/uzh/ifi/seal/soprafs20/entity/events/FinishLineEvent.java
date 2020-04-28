package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FinishLineEvent implements Event {

    private final Game game;
    private final List<Player> listOfPlayers;

    public FinishLineEvent(Game game, List<Player> listOfPlayers) {
        this.game = game;
        this.listOfPlayers = listOfPlayers;
    }

    public String getName() {
        return "finish-line";
    }

    public List<Chat> performEvent() {
        int maxPoints = 0;
        Player playerWithMaxPoints = this.listOfPlayers.get(0); //to make sure playerWithMaxPoints is initialized in all cases
        for (Player player : listOfPlayers) {
            int playersPoints = player.calculatePoints();
            player.setPoints(player.getPoints() + playersPoints);
            if (playersPoints >= maxPoints) {
                maxPoints = playersPoints;
                playerWithMaxPoints = player;
            }
        }
        this.game.endGameRound(playerWithMaxPoints);
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:finish-line", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "Looks like the round is over! Count your points!";
    }

}
