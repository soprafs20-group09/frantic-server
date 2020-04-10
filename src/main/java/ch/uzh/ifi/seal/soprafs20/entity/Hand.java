package ch.uzh.ifi.seal.soprafs20.entity;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    //private attributes
    private List<Card> cards;

    public Hand(){
        this.cards = new ArrayList<>();
    }

    public List<Card> getCards() {
        return cards;
    }

    public Card pop(int index) {
        return this.cards.remove(index);
    }

    public void push(Card card) {
        int counter = 0;
        if (cards.isEmpty()) {
            this.cards.add(card);
        } else {
            for (Card handCard : cards) {
                if (card.getOrderKey() < handCard.getOrderKey()) {
                    this.cards.add(counter, card);
                    break;
                } else if (card.getOrderKey() > handCard.getOrderKey() && counter == cards.size() - 1) {
                    this.cards.add(card);
                    break;
                }
                counter++;
            }
        }
    }

    public int size() {
        return this.cards.size();
    }

    public void clearHand() {
        List<Card> toBeRemoved = this.cards;
        this.cards.removeAll(toBeRemoved);
    }


}
