package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class PlayableCardsDTO {

    private int[] playable;

    public PlayableCardsDTO(int[] playable) {
        this.playable = playable;
    }

    public int[] getPlayable() {
        return playable;
    }

    public void setPlayable(int[] playable) {
        this.playable = playable;
    }
}
