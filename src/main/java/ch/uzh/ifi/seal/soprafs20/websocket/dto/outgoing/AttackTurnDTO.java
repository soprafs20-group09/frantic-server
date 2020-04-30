package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class AttackTurnDTO {

    private String currentPlayer;

    private int time;

    private int turn;

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
}
