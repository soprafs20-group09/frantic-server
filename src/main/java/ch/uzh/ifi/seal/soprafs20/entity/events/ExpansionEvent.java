package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.DrawStack;
import ch.uzh.ifi.seal.soprafs20.entity.Pile;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

import java.util.List;

public class ExpansionEvent implements Event {

    private final List<Player> listOfPlayers;
    private final Player currentPlayer;
    private final Pile<Card> drawStack;

    public ExpansionEvent(List<Player> listOfPlayers, Player currentPlayer, Pile<Card> drawStack) {
        this.listOfPlayers = listOfPlayers;
        this.currentPlayer = currentPlayer;
        this.drawStack = drawStack;
    }

    public String getName() {
        return "expansion";
    }

    public void performEvent() {
        int numOfPlayers = this.listOfPlayers.size();
        int currentPlayerIndex = this.listOfPlayers.indexOf(currentPlayer);

        for (int i = 1; i <= numOfPlayers; i++) {
            Player playerOfInterest = this.listOfPlayers.get((currentPlayerIndex + i) % numOfPlayers);
            for (int j = 1; j <= i; j++) {
                playerOfInterest.pushCardToHand(this.drawStack.pop());
            }
        }
    }

    //TODO: make it dynamic
    public String getMessage() {
        return "One, Two, Three, ... Since you are the 3rd to draw, you have to draw 3 cards!";
    }
}
