package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Pile;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

import java.util.List;

public class MexicanStandoffEvent implements Event {

    private final List<Player> listOfPlayers;
    private Pile<Card> discardPile;
    private Pile<Card> drawStack;

    public MexicanStandoffEvent(List<Player> listOfPlayers, Pile<Card> discardPile, Pile<Card> drawStack) {
        this.listOfPlayers = listOfPlayers;
        this.discardPile = discardPile;
        this.drawStack = drawStack;
    }

    public String getName() {
        return "mexican-standoff";
    }

    public void performEvent() {
        for (Player player : this.listOfPlayers) {
            for (int i = player.getHandSize() - 1; i >= 0; i--) {
                this.discardPile.push(player.popCard(i));
            }
            for (int i = 0; i < 3; i++) {
                if (!drawStack.empty()) {
                    player.pushCardToHand(drawStack.pop());
                }
            }
        }
    }

    public String getMessage() {
        return "Show your skills off! Everyone gets rid of their cards and gets three new ones! Who can finish first?";
    }
}
