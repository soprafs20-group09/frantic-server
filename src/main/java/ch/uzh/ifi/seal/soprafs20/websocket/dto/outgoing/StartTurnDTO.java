package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class StartTurnDTO extends TurnDTO {

    private int timebombRounds;

    public int getTimebombRounds() {
        return timebombRounds;
    }

    public void setTimebombRounds(int timebombRounds) {
        this.timebombRounds = timebombRounds;
    }
}
