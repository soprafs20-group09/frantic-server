package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class GamblingManEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;

    public GamblingManEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
    }

    public String getName() {
        return "gambling-man";
    }

    public void performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:gambling-man", this.getMessage()));
        this.gameService.sendChatMessage(this.gameRound.getLobbyId(), chat);
    }

    public String getMessage() {
        return "It's time to gamble! Choose a number card of the last played color. The player with the highest digit has to take all of them. So choose wisely!";
    }
}
