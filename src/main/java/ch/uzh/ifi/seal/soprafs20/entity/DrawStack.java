package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.cards.NumberCard;
import java.util.EnumSet;


public class DrawStack extends Pile<Card> {

    public DrawStack() {
        super();
        int orderNr = 0;
        for (Color c : EnumSet.complementOf(EnumSet.of(Color.MULTICOLOR))) {
            // Create and add numbered Cards for all colors except MULTICOLOR
            for (int i = 1; i <= 9; i++) {
                this.push(new NumberCard(c, i, orderNr++));
                // every number cards occurs twice, except for the black ones
                if (c != Color.BLACK) {
                    this.push(new NumberCard(c, i, orderNr++));
                }
            }
        }
        this.shuffle();
    }
}
