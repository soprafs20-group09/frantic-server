package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;

import java.util.ArrayList;
import java.util.List;

public class MerryChristmasEvent implements Event {
    public String getName() {
        return "merry-christmas";
    }

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:merry-christmas", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "Merry christmas everyone! It's that time of the year again. Give presents to your loved ones!";
    }
}
