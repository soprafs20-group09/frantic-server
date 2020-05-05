package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class StartTurnDTO extends TurnDTO {

    private int timebombRounds;

    public StartTurnDTO(String currentPlayer, int time, int turn, int timebombRounds) {
        super(currentPlayer, time, turn);
        this.timebombRounds = timebombRounds;
    }

    public int getTimebombRounds() {
        return timebombRounds;
    }

    public void setTimebombRounds(int timebombRounds) {
        this.timebombRounds = timebombRounds;
    }
}
