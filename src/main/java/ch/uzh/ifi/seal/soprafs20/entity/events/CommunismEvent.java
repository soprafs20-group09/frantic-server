package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;

import java.util.ArrayList;
import java.util.List;

public class CommunismEvent implements Event {
    public String getName() {
        return "communism";
    }

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:communism", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "Everybody is equal, now isn't that great?";
    }
}
