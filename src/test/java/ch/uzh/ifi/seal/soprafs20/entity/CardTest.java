package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.cards.NumberCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    public void createCards_throwsException(){
        Exception e = assertThrows(RuntimeException.class, () -> new NumberCard(Color.BLUE, 12));
        String msg = e.getMessage();
        assertTrue(msg.contains("Invalid number"));
    }

    @Test
    public void isPlayable() {
        /**
         tested using NumberCard as concrete implementation. -> update once more cards are implemented
         */
        NumberCard blue1 = new NumberCard(Color.BLUE, 1);
        NumberCard red1 = new NumberCard(Color.RED, 1);
        NumberCard blue4 = new NumberCard(Color.BLUE, 4);

        assertTrue(blue1.isPlayable(red1));
        assertTrue(blue1.isPlayable(blue4));
    }
}