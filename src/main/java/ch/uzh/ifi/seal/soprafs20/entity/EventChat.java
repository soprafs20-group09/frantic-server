package ch.uzh.ifi.seal.soprafs20.entity;

public class EventChat extends Chat {
    public EventChat(String username, String icon, String message) {
        super("event", username, icon, message);
    }

    public EventChat(String icon, String message) {
        super("event", icon, message);
    }
}
