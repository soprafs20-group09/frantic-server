package ch.uzh.ifi.seal.soprafs20.entity;

import java.util.List;

public class Hand {
    //private attributes
    private List<Card> cards;

    public Card pop(int index) {
        return this.cards.remove(index);
    }

    public void push(Card card) {
        //insert the new card at the right position BLUE, GREEN, YELLOW, RED, BLACK, MULTICOLOR
        //re-indexing of cards that come after the newly inserted card
        this.cards.add(card);
    }

    public int size(){
        return this.cards.size();
    }
}
