package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.GameRound;


public class TimeBombEvent implements Event {
    private final GameRound gameRound;

    public TimeBombEvent(GameRound gameRound) {
        this.gameRound = gameRound;
    }

    public String getName() {
        return "time-bomb";
    }

    public void performEvent() {
        gameRound.setTimeBomb();
    }

    public String getMessage() {
        return "Tick ... Tick ... Tick ... Boom! Everyone has three turns left! Defuse the Bomb and earn a reward by winning the round or let the Bomb explode and everyone's points in this round get doubled!";
    }
}
