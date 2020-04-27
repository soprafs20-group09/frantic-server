package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class StartTurnDTO {

    private String currentPlayer;

    private int time;

    private int turn;

    private int timebombRounds;

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

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTimebombRounds() {
        return timebombRounds;
    }

    public void setTimebombRounds(int timebombRounds) {
        this.timebombRounds = timebombRounds;
    }
}
