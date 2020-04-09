package ch.uzh.ifi.seal.soprafs20.entity;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    //private attributes
    private List<Card> cards;

    public Hand(){
        this.cards = new ArrayList<>();
    }

    public Card pop(int index) {
        return this.cards.remove(index);
    }

    /*
    public void push(Card card) {
        int counter = 0;
        for (Card handCard : cards){
            if (card.getOrderKey() < handCard.getOrderKey()){
                this.cards.add(counter, card);
                break;
            }
            counter++;
        }
    }
     */

    public void push(Card c) {
        this.cards.add(c);
    }

    public int size() {
        return this.cards.size();
    }

    public void clearHand() {
        this.cards.removeAll(cards);
    }
}
