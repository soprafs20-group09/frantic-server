package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

import ch.uzh.ifi.seal.soprafs20.entity.events.Event;

public class EventDTO {

    private String event;

    private String message;

    public EventDTO(Event event) {
        this.event = event.getName();
        this.message = event.getMessage();
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
