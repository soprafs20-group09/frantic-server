package ch.uzh.ifi.seal.soprafs20.entity.events;

public class RobinHoodEvent implements Event {
    public String getName() {
        return "robin-hood";
    }

    public void performEvent() {
    }

    public String getMessage() {
        return "Some call him a hero, some call him a thief! The player with the least cards has to swap cards with the player holding the most!";
    }
}
