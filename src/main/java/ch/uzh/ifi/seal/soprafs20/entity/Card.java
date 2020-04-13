package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;

import java.util.UUID;

public abstract class Card {

    protected Color color;
    protected Type type;
    protected Value value;
    protected final int orderKey;
    protected final String key;
    protected boolean isCounterable;

    public Card(Color c, Type t, Value v, int orderKey) {
        this.type = t;
        this.color = c;
        this.value = v;
        this.key = FranticUtils.generateId(8);
        this.orderKey = orderKey;
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

    public String getKey() {
        return key;
    }

    public int getOrderKey() {
        return orderKey;
    }

    public boolean isCounterable() {
        return isCounterable;
    }


    public abstract void performAction();

    public boolean isPlayable(Card other) {
        if (this.getColor() == Color.BLACK) {
            return this.getValue() == other.getValue();
        }
        else {
            return (this.getColor() == other.getColor() || this.getValue() == other.getValue());
        }
    }

    public String toString() {
        return "Card: " + this.color.toString() + ", " + this.value.toString() + ", " + this.type.toString();
    }

    public String keysToString() {
        return "Card key of " + this.color + this.value + ": " + this.key + ", " + String.valueOf(this.orderKey);
    }

}
