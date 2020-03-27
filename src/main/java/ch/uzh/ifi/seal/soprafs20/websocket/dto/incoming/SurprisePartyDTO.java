package ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming;

public class SurprisePartyDTO {

    private int card;

    private String target;

    public int getCard() {
        return card;
    }

    public void setCard(int card) {
        this.card = card;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
