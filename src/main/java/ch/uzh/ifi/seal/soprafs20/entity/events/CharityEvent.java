package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;

import java.util.ArrayList;
import java.util.List;

public class CharityEvent implements Event {
    public String getName() {
        return "charity";
    }

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:charity", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "How noble of you! You pick a card from the player with the most cards!";
    }
}
