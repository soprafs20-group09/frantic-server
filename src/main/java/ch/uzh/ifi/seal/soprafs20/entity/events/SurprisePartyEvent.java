package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;

import java.util.ArrayList;
import java.util.List;

public class SurprisePartyEvent implements Event {
    public String getName() {
        return "surprise-party";
    }

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:surprise-party", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "Surprise another player by gifting them one of your cards!";
    }
}
