package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class CommunismEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;

    public CommunismEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
    }

    public String getName() {
        return "communism";
    }

    public void performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:communism", this.getMessage()));
        this.gameService.sendChatMessage(this.gameRound.getLobbyId(), chat);

        int maxCards = 0;
        for (Player player : this.listOfPlayers) {
            if (player.getHandSize() > maxCards) {
                maxCards = player.getHandSize();
            }
        }

        for (Player player : this.listOfPlayers) {
            if (player.getHandSize() < maxCards) {
                int toDraw = maxCards - player.getHandSize();
                this.gameRound.drawCardFromStack(player, toDraw);
            }
        }
        this.gameRound.sendCompleteGameState();
        this.gameRound.finishTurn();
    }

    public String getMessage() {
        return "Everybody is equal, now isn't that great?";
    }
}
