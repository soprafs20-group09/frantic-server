package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GiftActionTest {

    private Player initiator;
    private Player target;
    private Action giftAction;
    private Card blue1 = new Card(Color.BLUE, Type.NUMBER, Value.ONE, false, 0);
    private Card blue2 = new Card(Color.BLUE, Type.NUMBER, Value.TWO, false, 1);
    private Card blue3 = new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 2);
    private Card fuckYou = new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FUCKYOU, false, 3);

    @BeforeEach
    public void setup() {
        this.initiator = new Player();
        this.initiator.pushCardToHand(blue1);
        this.initiator.pushCardToHand(blue2);
        this.initiator.pushCardToHand(blue3);

        this.target = new Player();

        int[] gifts = new int[]{1, 2};

        this.giftAction = new GiftAction(initiator, target, gifts);
    }

    @Test
    public void performTest(){
        giftAction.perform();
        assertEquals(1, this.initiator.getHandSize());
        assertEquals(2, this.target.getHandSize());
        assertEquals(blue1, this.initiator.popCard(0));
        assertEquals(blue2, this.target.popCard(0));
        assertEquals(blue3, this.target.popCard(0));
    }

    @Test
    public void getTargetsTest(){
        assertEquals(this.target, giftAction.getTargets()[0]);
    }

    @Test
    public void getInitiatorTest(){
        assertEquals(this.initiator, giftAction.getInitiator());
    }

    @Test
    public void isCounterableTest(){
        assertTrue(giftAction.isCounterable());
    }

    @Test
    public void fuckYouNotGiftableTest() {
        this.initiator.popCard(0);
        this.initiator.pushCardToHand(fuckYou);
        giftAction.perform();
        assertEquals(2, this.initiator.getHandSize());
        assertEquals(1, this.target.getHandSize());
        assertEquals(blue2, this.initiator.popCard(0));
        assertEquals(fuckYou, this.initiator.popCard(0));
        assertEquals(blue3, this.target.popCard(0));
    }
}