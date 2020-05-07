package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class ActionWindowDTO {

    private int[] playable;

    public ActionWindowDTO(int[] playable) {
        this.playable = playable;
    }

    public int[] getPlayable() {
        return playable;
    }

    public void setPlayable(int[] playable) {
        this.playable = playable;
    }
}
