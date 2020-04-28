package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;

import java.util.ArrayList;
import java.util.List;

public class ExpansionEvent implements Event {
    public String getName() {
        return "expansion";
    }

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:expansion", this.getMessage()));
        return chat;
    }

    //TODO: make it dynamic
    public String getMessage() {
        return "One, Two, Three, ... Since you are the 3rd to draw, you have to draw 3 cards!";
    }
}
