package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Pile;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

import java.util.ArrayList;
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

    public List<Chat> performEvent() {
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
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:mexican-standoff", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "Show your skills off! Everyone gets rid of their cards and gets three new ones! Who can finish first?";
    }
}
