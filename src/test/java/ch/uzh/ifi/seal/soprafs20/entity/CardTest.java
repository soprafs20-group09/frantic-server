package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.cards.NumberCard;
import org.assertj.core.util.diff.Delta;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void isPlayable() {
        /**
         tested using NumberCard as concrete implementation. -> update once more cards are implemented
         */
        NumberCard blue1 = new NumberCard(Color.BLUE, Type.NUMBER, Value.ONE);
        NumberCard red1 = new NumberCard(Color.RED, Type.NUMBER, Value.ONE);
        NumberCard blue4 = new NumberCard(Color.BLUE, Type.NUMBER, Value.FOUR);

        assertTrue(blue1.isPlayable(red1));
        assertTrue(blue1.isPlayable(blue4));
    }
}