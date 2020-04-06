package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class GameStateDTO {

    private CardDTO discardPile;

    private PlayerStateDTO[] players;

    public CardDTO getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(CardDTO discardPile) {
        this.discardPile = discardPile;
    }

    public PlayerStateDTO[] getPlayers() {
        return players;
    }

    public void setPlayers(PlayerStateDTO[] players) {
        this.players = players;
    }
}

