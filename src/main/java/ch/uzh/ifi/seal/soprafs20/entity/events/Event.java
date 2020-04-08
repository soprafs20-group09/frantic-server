package ch.uzh.ifi.seal.soprafs20.entity.events;

public interface Event {
    public String getId();
    public void performEvent();
    public String getMessage();
}
