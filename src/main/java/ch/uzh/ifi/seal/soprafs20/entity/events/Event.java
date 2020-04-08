package ch.uzh.ifi.seal.soprafs20.entity.events;

public interface Event {
    String getName();
    void performEvent();
    String getMessage();
}
