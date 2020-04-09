package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.cards.NumberCard;

import java.util.Collections;
import java.util.EnumSet;


public class DrawStack extends Pile<Card> {

    public DrawStack() {
        super();
        for (Color c : EnumSet.complementOf(EnumSet.of(Color.MULTICOLOR))) {
            // Create and add numbered Cards for all colors except MULTICOLOR
            for (int i = 1; i <= 9; i++) {
                this.push(new NumberCard(c, i));
            }
        }
        this.shuffle();
    }

}
