package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.*;

import java.util.ArrayList;
import java.util.List;

public class ThirdTimeLuckyEvent implements Event {

    private final List<Player> playerList;
    private final Pile<Card> drawStack;

    public ThirdTimeLuckyEvent(GameRound round, Pile<Card> stack) {
        this.playerList = round.getListOfPlayers();
        this.drawStack = stack;
    }

    public String getName() {
        return "third-time-lucky";
    }

    public List<Chat> performEvent() {
        for (Player player : this.playerList) {
            for (int i = 0; i < 3; i++) {
                if (!drawStack.empty()) {
                    player.pushCardToHand(drawStack.pop());
                }
            }
        }
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:third-time-lucky", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "Three cards for everyone!";
    }
}
