package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class MerryChristmasEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;

    public MerryChristmasEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
    }

    public String getName() {
        return "merry-christmas";
    }

    public void performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:merry-christmas", this.getMessage()));

        this.gameService.sendChatMessage(this.gameRound.getLobbyId(), chat);
    }

    public String getMessage() {
        return "Merry christmas everyone! It's that time of the year again. Give presents to your loved ones!";
    }
}
