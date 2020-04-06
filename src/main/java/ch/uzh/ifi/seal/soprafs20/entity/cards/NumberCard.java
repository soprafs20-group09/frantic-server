package ch.uzh.ifi.seal.soprafs20.entity.cards;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;

public class NumberCard extends Card {


    public NumberCard(Color c, Type t, Value v) {
        super(c, t, v);
    }

    public void performAction() {
    }

    public boolean isPlayable(Card other) {
        return this.getColor() == other.getColor() || this.getValue() == other.getValue();
    }
}
