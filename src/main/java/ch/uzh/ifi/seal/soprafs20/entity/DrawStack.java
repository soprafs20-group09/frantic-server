package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;

import java.util.EnumSet;


public class DrawStack extends Pile<Card> {

    public DrawStack() {
        super();
        int orderNr = 0;
        for (Color c : EnumSet.complementOf(EnumSet.of(Color.MULTICOLOR))) {
            // Create and add numbered Cards for all colors except MULTICOLOR
            for (int i = 1; i <= 9; i++) {
                this.push(new Card(c, i, orderNr++));
                // every number cards occurs twice, except for the black ones
                if (c != Color.BLACK) {
                    this.push(new Card(c, i, orderNr++));
                }
            }
            this.push(new Card(c, Type.SPECIAL, Value.SECONDCHANCE, false, orderNr++));
        }
        this.push(new Card(Color.BLACK, Type.SPECIAL, Value.FUCKYOU, false, orderNr));
        this.shuffle();
    }
}
