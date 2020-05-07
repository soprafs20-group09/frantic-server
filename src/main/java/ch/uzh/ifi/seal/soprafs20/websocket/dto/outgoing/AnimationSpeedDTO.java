package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class AnimationSpeedDTO {

    private int hand;

    public AnimationSpeedDTO(int hand) {
        this.hand = hand;
    }

    public int getHand() {
        return hand;
    }

    public void setHand(int hand) {
        this.hand = hand;
    }
}
