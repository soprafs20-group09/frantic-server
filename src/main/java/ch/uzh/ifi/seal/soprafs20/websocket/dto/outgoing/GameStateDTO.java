package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class GameStateDTO {

    private DiscardPileDTO discardPile;

    private PlayerStateDTO[] players;

    public DiscardPileDTO getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(DiscardPileDTO discardPile) {
        this.discardPile = discardPile;
    }

    public PlayerStateDTO[] getPlayers() {
        return players;
    }

    public void setPlayers(PlayerStateDTO[] players) {
        this.players = players;
    }
}

