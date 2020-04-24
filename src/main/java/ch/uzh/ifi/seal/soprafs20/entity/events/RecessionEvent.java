package ch.uzh.ifi.seal.soprafs20.entity.events;

public class RecessionEvent implements Event {
    public String getName() {
        return "recession";
    }

    public void performEvent() {
    }

    //TODO: make it dynamic
    public String getMessage() {
        return "One, two, three, ... Since you are the 3rd to discard, you can discard 3 cards!";
    }
}
