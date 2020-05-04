package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TornadoEvent implements Event {

    private final List<Player> listOfPlayers;
    private final List<Card> tornadoList;

    public TornadoEvent(GameRound round) {
        this.listOfPlayers = round.getListOfPlayers();
        this.tornadoList = new ArrayList<>();
    }

    public String getName() {
        return "tornado";
    }

    public List<Chat> performEvent() {
        // collect cards
        for (Player player : this.listOfPlayers) {
            while (player.getHandSize() > 0) {
                this.tornadoList.add(player.popCard());
            }
        }
        Collections.shuffle(this.tornadoList);

        // redistribute cards
        int i = 0;
        while (!this.tornadoList.isEmpty()) {
            this.listOfPlayers.get(i).pushCardToHand(this.tornadoList.remove(0));
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
