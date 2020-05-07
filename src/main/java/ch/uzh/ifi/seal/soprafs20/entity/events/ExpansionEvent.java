package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.*;

import java.util.ArrayList;
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

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:expansion", this.getMessage()));

        int numOfPlayers = this.listOfPlayers.size();
        int initiatorIndex = this.listOfPlayers.indexOf(currentPlayer);

        for (int i = 1; i <= numOfPlayers; i++) {
            Player playerOfInterest = this.listOfPlayers.get((initiatorIndex + i) % numOfPlayers);
            int drawnCards = 0;
            for (int j = 1; j <= i; j++) {
                if (this.drawStack.size() > 0) {
                    playerOfInterest.pushCardToHand(this.drawStack.pop());
                    drawnCards++;
                }
            }
            if (drawnCards > 0) {
                chat.add(new Chat("event", "avatar:" + playerOfInterest.getUsername(),
                        playerOfInterest.getUsername() + " drew " + drawnCards + " cards"));
            }
        }
        return chat;
    }

    public String getMessage() {
        return "Cards are selling like hot cakes! Grab one or two or three ...";
    }
}
