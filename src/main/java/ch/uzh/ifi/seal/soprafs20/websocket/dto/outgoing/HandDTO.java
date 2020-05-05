package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class HandDTO {

    private CardDTO[] cards;

    public HandDTO(CardDTO[] cards) {
        this.cards = cards;
    }

    public CardDTO[] getCards() {
        return cards;
    }

    public void setCards(CardDTO[] cards) {
        this.cards = cards;
    }
}

