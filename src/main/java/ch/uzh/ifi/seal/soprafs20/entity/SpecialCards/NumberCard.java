package ch.uzh.ifi.seal.soprafs20.entity.SpecialCards;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.entity.Card;

public class NumberCard extends Card {

    private int number;
    private Color color;
    private Type type;

    public NumberCard(Color c, Type t, int n){
        super(c, t);
        this.number = n;
    }

    public int getNumber() {
        return number;
    }

    public void performAction(){}
}
