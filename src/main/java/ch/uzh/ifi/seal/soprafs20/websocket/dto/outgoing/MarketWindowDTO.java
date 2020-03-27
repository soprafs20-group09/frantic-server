package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class MarketWindowDTO {

    private int time;

    private CardDTO[] cards;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public CardDTO[] getCards() {
        return cards;
    }

    public void setCards(CardDTO[] cards) {
        this.cards = cards;
    }
}
