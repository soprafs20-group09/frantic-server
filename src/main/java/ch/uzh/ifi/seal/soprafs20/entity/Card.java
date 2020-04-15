package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;

public class Card {

    protected Color color;
    protected Type type;
    protected Value value;
    protected final int orderKey;
    protected final String key;
    protected final boolean counterable;

    public Card(Color c, int value, int orderKey) {
        this(c, Type.NUMBER, Value.values()[value-1], false, orderKey);
        if (value > 10) {
            throw new RuntimeException("Invalid number");
        }
    }
    public Card(Color c, Type t, Value v, boolean counterable, int orderKey) {
        this.type = t;
        this.color = c;
        this.value = v;
        this.counterable = counterable;
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
        return counterable;
    }

    public boolean isPlayable(Card other) {
        switch (this.getColor()) {
            case BLACK:
                return this.getValue() == other.getValue();

            case MULTICOLOR:
                return true;

            default:
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
