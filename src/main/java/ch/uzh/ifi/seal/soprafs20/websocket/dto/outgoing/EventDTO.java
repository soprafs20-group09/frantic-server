package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class EventDTO {

    private String event;

    private String message;

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
