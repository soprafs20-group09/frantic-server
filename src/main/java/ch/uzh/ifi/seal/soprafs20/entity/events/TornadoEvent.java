package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.*;

import java.util.ArrayList;
import java.util.List;

public class TornadoEvent implements Event {

    private final List<Player> listOfPlayers;
    private final Pile<Card> tornadoStack;

    public TornadoEvent(GameRound round) {
        this.listOfPlayers = round.getListOfPlayers();
        this.tornadoStack = new DiscardPile();
    }

    public String getName() {
        return "tornado";
    }

    public List<Chat> performEvent() {
        // collect cards
        for (Player player : this.listOfPlayers) {
            while (player.getHandSize() > 0) {
                this.tornadoStack.push(player.popCard());
            }
        }
        this.tornadoStack.shuffle();

        // redistribute cards
        int i = 0;
        while (!this.tornadoStack.isEmpty()) {
            this.listOfPlayers.get(i).pushCardToHand(this.tornadoStack.pop());
            i = ++i % this.listOfPlayers.size();
        }
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:tornado", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "Oh no! A tornado whirled all the cards around! Looks like we have to redistribute them!";
    }
}
