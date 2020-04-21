package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;

import java.util.EnumSet;


public class DrawStack extends Pile<Card> {

    public DrawStack() {
        super();
        int orderNr = 0;
        for (Color c : EnumSet.complementOf(EnumSet.of(Color.MULTICOLOR, Color.NONE))) {
            // Create and add numbered Cards for all colors except MULTICOLOR
            for (int i = 1; i <= 9; i++) {
                this.push(new Card(c, i, orderNr++));
                // every number cards occurs twice, except for the black ones
                if (c != Color.BLACK) {
                    this.push(new Card(c, i, orderNr++));
                }
            }
            if (c != Color.BLACK) {
                this.push(new Card(c, Type.SPECIAL, Value.SECONDCHANCE, false, orderNr++));
                this.push(new Card(c, Type.SPECIAL, Value.GIFT, true, orderNr++));
                this.push(new Card(c, Type.SPECIAL, Value.GIFT, true, orderNr++));
                this.push(new Card(c, Type.SPECIAL, Value.EXCHANGE, true, orderNr++));
                this.push(new Card(c, Type.SPECIAL, Value.SKIP, true, orderNr++));
            }
        }
        for (int i = 0; i < 11; i++) {
            this.push(new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTIC, false, orderNr++));
        }
        for (int i = 0; i < 4; i++) {
            this.push(new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTICFOUR, true, orderNr++));
        }
        for (int i = 0; i < 2; i++) {
            this.push(new Card(Color.MULTICOLOR, Type.SPECIAL, Value.EQUALITY, true, orderNr++));
        }
        for (int i = 0; i < 4; i++) {
            this.push(new Card(Color.MULTICOLOR, Type.SPECIAL, Value.COUNTERATTACK, true, orderNr++));
        }
        for (int i = 0; i < 1; i++) {
            this.push(new Card(Color.MULTICOLOR, Type.SPECIAL, Value.NICETRY, false, orderNr++));
        }

        this.push(new Card(Color.BLACK, Type.SPECIAL, Value.FUCKYOU, false, orderNr));
        this.shuffle();
    }
}
