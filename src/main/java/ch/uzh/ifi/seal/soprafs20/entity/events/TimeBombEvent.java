package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.DiscardPile;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

import java.util.List;

public class TimeBombEvent implements Event {
    private GameRound gameRound;

    public TimeBombEvent(GameRound gameRound){
        this.gameRound = gameRound;
    }

    public String getName(){
        return "time-bomb";
    };

    public void performEvent() {
        gameRound.setTimeBomb(true);
    };

    public String getMessage() {return "Tick ... Tick ... Tick ... Boom! Everyone has three turns left! Defuse the Bomb and earn a reward by winning the round or let the Bomb explode and everyone's points in this round get doubled!";};
}
