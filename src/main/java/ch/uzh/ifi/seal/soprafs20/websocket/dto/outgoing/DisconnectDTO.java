package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class DisconnectDTO {

    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
