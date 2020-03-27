package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class ActionWindowDTO {

    private int time;

    private int[] playable;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int[] getPlayable() {
        return playable;
    }

    public void setPlayable(int[] playable) {
        this.playable = playable;
    }
}
