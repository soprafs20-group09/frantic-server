package ch.uzh.ifi.seal.soprafs20.entity.events;

public class ExpansionEvent implements Event {
    public String getName() {
        return "expansion";
    }

    public void performEvent() {
    }

    //TODO: make it dynamic
    public String getMessage() {
        return "One, Two, Three, ... Since you are the 3rd to draw, you have to draw 3 cards!";
    }
}
