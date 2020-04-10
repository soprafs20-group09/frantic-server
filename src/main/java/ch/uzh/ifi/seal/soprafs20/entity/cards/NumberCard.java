package ch.uzh.ifi.seal.soprafs20.entity.cards;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;

public class NumberCard extends Card {


    public NumberCard(Color c, int value, int orderKey) {
        super(c, Type.NUMBER, Value.values()[value-1], orderKey);
        if (value > 10) {
            throw new RuntimeException("Invalid number");
        }
    }

    public void performAction() {
    }
}
