package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class TurnDTO {

    private String currentPlayer;

    public TurnDTO(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
