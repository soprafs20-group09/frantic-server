package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EqualityActionTest {

    DiscardPile discardPile = new DiscardPile();
    private Player initiator;
    private Player target;
    private Action equalityAction;
    private final Card blue1 = new Card(Color.BLUE, Type.NUMBER, Value.ONE, false, 0);
    private final Card blue2 = new Card(Color.BLUE, Type.NUMBER, Value.TWO, false, 1);
    private final Card blue3 = new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 2);
    private final Card red1 = new Card(Color.RED, Type.NUMBER, Value.ONE, false, 3);
    private final Card red2 = new Card(Color.RED, Type.NUMBER, Value.TWO, false, 4);
    private final Card red3 = new Card(Color.RED, Type.NUMBER, Value.THREE, false, 5);
    private final Card red4 = new Card(Color.RED, Type.NUMBER, Value.FOUR, false, 6);
    private final Card colorWishBlue = new Card(Color.BLUE, Type.WISH, Value.COLORWISH, false, 7);

    @BeforeEach
    public void setup() {
        this.initiator = new Player();
        this.initiator.pushCardToHand(blue1);
        this.initiator.pushCardToHand(blue2);
        this.initiator.pushCardToHand(blue3);

        this.target = new Player();
        this.target.setUsername("TripleOwner7");

        discardPile.push(red1);

        DrawStack drawStack = new DrawStack();
        drawStack.push(red2);
        drawStack.push(red3);
        drawStack.push(red4);

        this.equalityAction = new EqualityAction(initiator, target, Color.BLUE, discardPile, drawStack);
    }

    @Test
    public void performTest() {
        List<Chat> resultChat = equalityAction.perform();
        assertEquals(3, this.initiator.getHandSize());
        assertEquals(3, this.target.getHandSize());
        assertEquals(blue1, this.initiator.popCard(0));
        assertEquals(blue2, this.initiator.popCard(0));
        assertEquals(blue3, this.initiator.popCard(0));
        assertEquals(red2, this.target.popCard(0));
        assertEquals(red3, this.target.popCard(0));
        assertEquals(red4, this.target.popCard(0));
        Card popped = discardPile.pop();
        assertEquals(colorWishBlue.getColor(), popped.getColor());
        assertEquals(colorWishBlue.getType(), popped.getType());
        assertEquals(colorWishBlue.getValue(), popped.getValue());

        assertEquals("event", resultChat.get(0).getType());
        assertEquals("special:equality", resultChat.get(0).getIcon());
        assertEquals("TripleOwner7 drew 3 cards.", resultChat.get(0).getMessage());
    }

    @Test
    public void getTargetsTest() {
        assertEquals(this.target, equalityAction.getTargets()[0]);
    }

    @Test
    public void getInitiatorTest() {
        assertEquals(this.initiator, equalityAction.getInitiator());
    }

    @Test
    public void isCounterableTest() {
        assertTrue(equalityAction.isCounterable());
    }
}