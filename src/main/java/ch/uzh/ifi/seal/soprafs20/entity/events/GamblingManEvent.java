package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;

import java.util.ArrayList;
import java.util.List;

public class GamblingManEvent implements Event {
    public String getName() {
        return "gambling-man";
    }

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:gambling-man", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "It's time to gamble! Choose a number card of the last played color. The player with the highest digit has to take all of them. So choose wisely!";
    }
}
