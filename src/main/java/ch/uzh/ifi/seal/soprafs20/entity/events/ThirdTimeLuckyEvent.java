package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.*;

import java.util.List;

public class ThirdTimeLuckyEvent implements Event {

    private List<Player> playerList;
    private Pile<Card> drawStack;

    public ThirdTimeLuckyEvent(GameRound round, Pile<Card> stack) {
        this.playerList = round.getListOfPlayers();
        this.drawStack = stack;
    }

    public String getName(){
        return "third-time-lucky";
    }

    public void performEvent() {
        for (Player player : this.playerList) {
           for (int i = 0; i < 3; i++) {
               if (!drawStack.empty()) {
                   player.pushCardToHand(drawStack.pop());
               }
           }
        }
    }

    public String getMessage() {return "Three cards for everyone!";}
}
