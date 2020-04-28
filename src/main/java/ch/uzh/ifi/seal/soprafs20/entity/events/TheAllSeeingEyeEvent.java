package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;

import java.util.ArrayList;
import java.util.List;

public class TheAllSeeingEyeEvent implements Event {
    public String getName() {
        return "the-all-seeing-eye";
    }

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:the-all-seeing-eye", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "You can't run! You can't hide! The all-seeing eye is here! Take a good look at everyone cards!";
    }
}
