package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;

import java.util.ArrayList;
import java.util.List;

public class MarketEvent implements Event {
    public String getName() {
        return "market";
    }

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:market", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "Choose one of these incredibly awesome cards for the great price of only 0$!";
    }
}
