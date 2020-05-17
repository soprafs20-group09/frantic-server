package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class CardDisabledDTO extends CardDTO {

    private boolean disabled;

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
