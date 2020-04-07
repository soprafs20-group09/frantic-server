package ch.uzh.ifi.seal.soprafs20.entity.events;

public interface Event {
    public int getId();
    public void performEvent();
    public void getMessage();
}
