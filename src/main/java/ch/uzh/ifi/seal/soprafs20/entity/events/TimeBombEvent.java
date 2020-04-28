package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;

import java.util.ArrayList;
import java.util.List;


public class TimeBombEvent implements Event {
    private final GameRound gameRound;

    public TimeBombEvent(GameRound gameRound) {
        this.gameRound = gameRound;
    }

    public String getName() {
        return "time-bomb";
    }

    public List<Chat> performEvent() {
        gameRound.setTimeBomb();
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:time-bomb", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "Tick ... Tick ... Tick ... Boom! Everyone has three turns left! Defuse the Bomb and earn a reward by winning the round or let the Bomb explode and everyone's points in this round get doubled!";
    }
}
