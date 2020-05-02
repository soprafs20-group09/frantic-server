package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Pile;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatingSeasonEvent implements Event {

    private final List<Player> listOfPlayers;
    private final Player initiator;
    private final Pile<Card> discardPile;

    public MatingSeasonEvent(List<Player> listOfPlayers, Player currentPlayer, Pile<Card> discardPile) {
        this.listOfPlayers = listOfPlayers;
        this.initiator = currentPlayer;
        this.discardPile = discardPile;
    }

    public String getName() {
        return "mating-season";
    }

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:mating-season", this.getMessage()));

        int numOfPlayers = this.listOfPlayers.size();
        int initiatorIndex = this.listOfPlayers.indexOf(this.initiator);

        for (int p = 1; p <= numOfPlayers; p++) {
            Map<Value, Integer> mappedValues = new HashMap<>();
            Player playerOfInterest = this.listOfPlayers.get((initiatorIndex + p) % numOfPlayers);

            for (int i = 0; i < playerOfInterest.getHandSize(); i++) {
                Card card = playerOfInterest.peekCard(i);
                Value cardValue = card.getValue();
                if (cardValue.ordinal() < 9) {
                    int count = mappedValues.getOrDefault(cardValue, 0);
                    mappedValues.put(cardValue, count + 1);
                }
            }

            int discardedCards = 0;
            for (int i = playerOfInterest.getHandSize() - 1; i >= 0; i--) {
                Card card = playerOfInterest.peekCard(i);
                Value cardValue = card.getValue();
                if (mappedValues.containsKey(cardValue) && mappedValues.get(cardValue) > 1) {
                    discardPile.push(playerOfInterest.popCard(i));
                    discardedCards++;
                }
            }

            if (discardedCards > 0) {
                chat.add(new Chat("event", "avatar:" + playerOfInterest.getUsername(),
                        playerOfInterest.getUsername() + " discarded " + discardedCards + " cards"));
            }
        }
        return chat;
    }

    public String getMessage() {
        return "It's valentines day! Well, at least for your cards! Discard numeral pairs, triples and so on.";
    }
}
