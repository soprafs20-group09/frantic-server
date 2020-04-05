package ch.uzh.ifi.seal.soprafs20.entity.cards;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.entity.Card;

public class NumberCard extends Card {

    private int number;

    public NumberCard(Color c, Type t, int n){
        super(c, t);
        this.number = n;
    }

    public int getNumber() {
        return number;
    }

    public void performAction(){}
}
