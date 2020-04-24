package ch.uzh.ifi.seal.soprafs20.utils;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FranticUtilsTest {

    @Test
    public void check_getValueRepresentation() {
        assertEquals("4", FranticUtils.getStringRepresentation(Value.FOUR));
        assertEquals("blue", FranticUtils.getStringRepresentation(Color.BLUE));
        assertEquals("special", FranticUtils.getStringRepresentation(Type.SPECIAL));
    }

    @Test
    public void getStringRepresentationOfNumberCardTest() {
        Card card = new Card(Color.RED, 3, 1);
        assertEquals("red 3", FranticUtils.getStringRepresentationOfNumberCard(card));
    }
}