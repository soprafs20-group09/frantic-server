package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



class PileTest {

    /**
     * The Pile methods are tested using the DrawStack as a concrete implementation.
     */

    @Test
    public void pushCardsintoStack() {
        Card blue1 = new Card(Color.BLUE, 1, 1);

        DrawStack drawStack = new DrawStack();
        drawStack.push(blue1);
        Card peeked = drawStack.peek();
        assertEquals(Color.BLUE, peeked.getColor());
    }

    @Test
    public void peek1stCard() {
        Card testCard = new Card(Color.GREEN, Type.SPECIAL, Value.SECONDCHANCE,false, 1);
        DrawStack drawStack = new DrawStack();
        drawStack.push(testCard);

        Card peeked = drawStack.peek();

        assertEquals(Color.GREEN, peeked.getColor());
        assertEquals(Type.SPECIAL, peeked.getType());
        assertEquals(Value.SECONDCHANCE, peeked.getValue());
    }

    // This works, has been tested in earlier version
    @Test
    public void peek2ndCard() {
        Card blue1 = new Card(Color.BLUE, 1, 1);
        Card testCard = new Card(Color.GREEN, Type.SPECIAL, Value.SECONDCHANCE,false, 1);
        DrawStack drawStack = new DrawStack();
        drawStack.push(blue1);
        drawStack.push(testCard);

        Card peeked = drawStack.peekN(2);
        assertEquals(Type.NUMBER, peeked.getType());
        assertEquals(Color.BLUE, peeked.getColor());

    }

    @Test
    public void popCard() {
        DrawStack drawStack = new DrawStack();
        int oldSize = drawStack.size();
        Card popped = drawStack.pop();
        
        assertEquals(oldSize-1, drawStack.size());

    }
}