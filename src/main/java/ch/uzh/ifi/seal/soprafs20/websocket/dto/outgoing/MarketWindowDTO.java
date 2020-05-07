package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class MarketWindowDTO {

    private CardDTO[] cards;

    public MarketWindowDTO(CardDTO[] cards) {
        this.cards = cards;
    }

    public CardDTO[] getCards() {
        return cards;
    }

    public void setCards(CardDTO[] cards) {
        this.cards = cards;
    }
}
