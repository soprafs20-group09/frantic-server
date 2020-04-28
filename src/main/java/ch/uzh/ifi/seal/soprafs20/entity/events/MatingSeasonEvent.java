package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;

import java.util.ArrayList;
import java.util.List;

public class MatingSeasonEvent implements Event {
    public String getName() {
        return "mating-season";
    }

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:mating-season", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "It's valentines day! Well, at least for your cards! Discard numeral pairs, triples and so on.";
    }
}
