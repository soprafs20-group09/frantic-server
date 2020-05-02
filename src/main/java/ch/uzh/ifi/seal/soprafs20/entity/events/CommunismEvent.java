package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Pile;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommunismEvent implements Event {

    private final List<Player> listOfPlayers;
    private final Pile<Card> drawStack;

    public CommunismEvent(List<Player> listOfPlayers, Pile<Card> drawStack) {
        this.listOfPlayers = listOfPlayers;
        this.drawStack = drawStack;
    }

    public String getName() {
        return "communism";
    }

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:communism", this.getMessage()));

        int maxCards = 0;
        for (Player player : this.listOfPlayers) {
            if (player.getHandSize() > maxCards) {
                maxCards = player.getHandSize();
            }
        }

        for (Player player : this.listOfPlayers) {
            int drawnCards = 0;
            if (player.getHandSize() < maxCards) {
                int toDraw = maxCards - player.getHandSize();
                for (int i = 1; i <= toDraw; i++) {
                    if (this.drawStack.size() > 0) {
                        player.pushCardToHand(this.drawStack.pop());
                        drawnCards++;
                    }
                }
                chat.add(new Chat("event", "avatar:" + player.getUsername(),
                        player.getUsername() + " drew " + drawnCards + " cards"));
            }
        }
        return chat;
    }

    public String getMessage() {
        return "Everybody is equal, now isn't that great?";
    }
}
