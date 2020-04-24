package ch.uzh.ifi.seal.soprafs20.entity.events;

public class VandalismEvent implements Event {
    public String getName() {
        return "vandalism";
    }

    public void performEvent() {
    }

    public String getMessage() {
        return "Let all your anger out! You can discard all cards of the last played color! Do it! Just smash them on the discard Pile!";
    }
}
