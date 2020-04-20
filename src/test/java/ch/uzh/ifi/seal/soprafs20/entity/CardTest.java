package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    public void createCards_throwsException(){
        Exception e = assertThrows(RuntimeException.class, () -> new Card(Color.BLUE, 12, 1));
        String msg = e.getMessage();
        assertTrue(msg.contains("Invalid number"));
    }

    @Test
    public void isPlayable() {
        /**
         tested using Card as concrete implementation. -> update once more cards are implemented
         */
        Card blue1 = new Card(Color.BLUE, 1, 1);
        Card red1 = new Card(Color.RED, 1, 2);
        Card blue4 = new Card(Color.BLUE, 4, 3);
        Card black5 = new Card(Color.BLACK, 5, 4);
        Card blue8 = new Card(Color.BLUE, 8, 5);
        Card blue9 = new Card(Color.BLUE, 9, 6);

        assertTrue(blue1.isPlayableOn(red1));
        assertTrue(blue1.isPlayableOn(blue4));
        assertFalse(black5.isPlayableOn(blue1));
        assertTrue(blue9.isPlayableOn(blue8));
    }
}