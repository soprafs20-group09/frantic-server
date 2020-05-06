package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;


public class TimeBombEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;

    public TimeBombEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
    }

    public String getName() {
        return "time-bomb";
    }

    public void performEvent() {
        gameRound.setTimeBomb();
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:time-bomb", this.getMessage()));

        this.gameService.sendChatMessage(this.gameRound.getLobbyId(), chat);
        this.gameRound.sendCompleteGameState();
        this.gameRound.finishTurn();
    }

    public String getMessage() {
        return "Tick ... Tick ... Tick ... Boom! Everyone has three turns left! Defuse the Bomb and earn a reward by winning the round or let the Bomb explode and everyone's points in this round get doubled!";
    }
}
