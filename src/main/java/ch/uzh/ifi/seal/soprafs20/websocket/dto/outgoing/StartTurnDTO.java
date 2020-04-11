package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class StartTurnDTO {

    private String currentPlayer;

    private int time;

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
