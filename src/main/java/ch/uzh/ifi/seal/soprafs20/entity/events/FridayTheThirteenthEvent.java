package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;

import java.util.ArrayList;
import java.util.List;

public class FridayTheThirteenthEvent implements Event {
    public String getName() {
        return "friday-the-13th";
    }

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:friday-the-13th", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "It’s Friday, the Thirteenth. A hook-handed murderer is among us! But just in the movies, it’s a totally boring, normal Friday, nothing weird happens. The game round continues without further ado.";
    }
}
