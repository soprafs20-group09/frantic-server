package ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming;

public class GiveCardsToPlayerDTO {

    private int[] cards;

    private String target;

    public int[] getCards() {
        return cards;
    }

    public void setCards(int[] cards) {
        this.cards = cards;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
