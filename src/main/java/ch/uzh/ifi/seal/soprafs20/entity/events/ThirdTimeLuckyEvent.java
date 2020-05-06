package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.*;

import java.util.ArrayList;
import java.util.List;

public class ThirdTimeLuckyEvent implements Event {

    private final List<Player> listOfPlayers;
    private final Pile<Card> drawStack;

    public ThirdTimeLuckyEvent(List<Player> listOfPlayers, Pile<Card> stack) {
        this.listOfPlayers = listOfPlayers;
        this.drawStack = stack;
    }

    public String getName() {
        return "third-time-lucky";
    }

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        for (Player player : this.listOfPlayers) {
            int drawnCards = 0;
            for (int i = 0; i < 3; i++) {
                if (!drawStack.empty()) {
                    player.pushCardToHand(drawStack.pop());
                    drawnCards++;
                }
            }
            if (drawnCards > 0) {
                chat.add(new Chat("event", "avatar:" + player.getUsername(),
                        player.getUsername() + " drew " + drawnCards + " cards"));
            }
        }

        chat.add(new Chat("event", "event:third-time-lucky", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "Three cards for everyone!";
    }
}
