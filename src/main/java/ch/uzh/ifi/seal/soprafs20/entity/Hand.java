package ch.uzh.ifi.seal.soprafs20.entity;

import java.util.List;

public class Hand {
    //private attributes
    private List<Card> cards;

    public Card pop(int index) {
        return this.cards.remove(index);
    }

    /*
    public void push(Card card) {
        int counter = 0;
        for (Card handCard : cards){
            if (card.getOrderKey() < handCard.getOrderKey()){
                this.cards.add(counter, card);
            }
            counter++;
        }
    }
     */

    public int size(){
        return this.cards.size();
    }
}
