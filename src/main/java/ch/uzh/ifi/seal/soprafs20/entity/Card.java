package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;

public abstract class Card {

    private Color color;
    private Type type;

    public Card(Color c, Type t){
        this.type = t;
        this.color = c;
    }

    public Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    // resets the value of a card (ex. when shuffling) -> really needed??
    //public void reset(){}

    public abstract void performAction();
}
