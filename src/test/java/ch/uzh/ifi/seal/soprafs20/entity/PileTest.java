package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.entity.cards.NumberCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



class PileTest {

    /**
     * The Pile methods are tested using the DrawStack as a concrete implementation.
     */

    @Test
    public void createPile_getEmptyStack() {
        DrawStack drawStack = new DrawStack();
        assertEquals(0, drawStack.size());
    }

    @Test
    public void pushCardsintoStack() {
        NumberCard blue1 = new NumberCard(Color.BLUE, 1, 1);

        DrawStack drawStack = new DrawStack();
        drawStack.push(blue1);
        Card peeked = drawStack.peek();
        assertEquals(Color.BLUE, peeked.getColor());
    }

    @Test
    public void peek1stCard() {
        DrawStack drawStack = new DrawStack();

        Card peeked = drawStack.peek();

        assertEquals(Type.NUMBER, peeked.getType());
    }

    // This works, has been tested in earlier version
    @Test
    public void peek2ndCard() {
        DrawStack drawStack = new DrawStack();

        Card peeked = drawStack.peekSecond();
        assertEquals(Type.NUMBER, peeked.getType());

    }

    @Test
    public void popCard() {
        DrawStack drawStack = new DrawStack();
        int oldSize = drawStack.size();
        Card popped = drawStack.pop();

        assertEquals(Type.NUMBER, popped.getType());
        assertEquals(oldSize-1, drawStack.size());

    }
}