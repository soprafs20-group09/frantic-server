package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class SurprisePartyEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;

    public SurprisePartyEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
    }

    public String getName() {
        return "surprise-party";
    }

    public void performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:surprise-party", this.getMessage()));
        this.gameService.sendChatMessage(this.gameRound.getLobbyId(), chat);
    }

    public String getMessage() {
        return "Surprise another player by gifting them one of your cards!";
    }
}
