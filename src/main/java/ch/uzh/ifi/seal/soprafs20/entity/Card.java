package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;

public abstract class Card {

    protected Color color;
    protected Type type;
    protected Value value;

    public Card(Color c, Type t, Value v){
        this.type = t;
        this.color = c;
        this.value = v;
    }

    public Color getColor() {
        return color;
    }

    public Value getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    // resets the value of a card (ex. when shuffling) -> really needed??
    //public void reset(){}

    public abstract void performAction();

    public boolean isPlayable(Card other){
        return this.getColor() == other.getColor() || this.getValue() == other.getValue();
    }

    public String toString(){
        return "Card: " + this.color.toString() + ", " + this.value.toString() + ", " + this.type.toString();
    }

}
