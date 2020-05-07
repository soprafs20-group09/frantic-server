package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class PlayableDTO {

    private int[] cards;

    private boolean canDraw;

    private boolean canEnd;

    public PlayableDTO(int[] cards, boolean canDraw, boolean canEnd) {
        this.cards = cards;
        this.canDraw = canDraw;
        this.canEnd = canEnd;
    }

    public int[] getCards() {
        return cards;
    }

    public void setCards(int[] cards) {
        this.cards = cards;
    }

    public boolean isCanDraw() {
        return canDraw;
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    public boolean isCanEnd() {
        return canEnd;
    }

    public void setCanEnd(boolean canEnd) {
        this.canEnd = canEnd;
    }
}
