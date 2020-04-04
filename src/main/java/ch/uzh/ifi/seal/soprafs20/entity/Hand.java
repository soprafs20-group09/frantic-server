package ch.uzh.ifi.seal.soprafs20.entity;

import java.util.List;

public class Hand {
    //private attributes
    private List<Card> cards;

    public Card pop(int index) {
        return cards.remove(index);
    }

    public void push(Card card) {
        cards.add(card);
    }

    public int size(){
        return cards.size();
    }
}
