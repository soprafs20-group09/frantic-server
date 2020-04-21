package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.*;

public class ThirdTimeLuckyEvent implements Event {

    private GameRound gameRound;
    private Pile<Card> drawStack;

    public ThirdTimeLuckyEvent(GameRound round, Pile<Card> stack) {
        this.gameRound = round;
        this.drawStack = stack;
    }

    public String getName(){
        return "third-time-lucky";
    }

    public void performEvent() {
        for (Player player : this.gameRound.getListOfPlayers()) {
           for (int i = 0; i < 3; i++) {
               if (!drawStack.empty()) {
                   player.pushCardToHand(drawStack.pop());
               }
           }
        }
    }

    public String getMessage() {return "Three cards for everyone!";}
}
