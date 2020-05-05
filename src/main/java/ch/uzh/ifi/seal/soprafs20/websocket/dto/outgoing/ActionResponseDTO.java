package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class ActionResponseDTO {

    private String action;

    public ActionResponseDTO(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
