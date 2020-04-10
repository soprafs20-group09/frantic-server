package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.cards.NumberCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {

    @Test
    public void pushCardToHandAndPopCardFromHand() {
        NumberCard blue1 = new NumberCard(Color.BLUE, 1);

        Hand hand = new Hand();
        hand.push(blue1);
        assertEquals(1, hand.size());
        assertEquals(blue1, hand.pop(0));
        assertEquals(0, hand.size());
    }

    @Test
    public void pushCardToHandOrdering() {
        //Test if cards get inserted at the correct position
    }

    @Test
    public void sizeOfHand() {
        NumberCard blue1 = new NumberCard(Color.BLUE, 1);

        Hand hand = new Hand();
        assertEquals(0, hand.size());
        hand.push(blue1);
        assertEquals(1, hand.size());
        hand.push(blue1);
        assertEquals(2, hand.size());
        hand.pop(0);
        assertEquals(1, hand.size());
        hand.pop(0);
        assertEquals(0, hand.size());
    }

    @Test
    public void clearAllCardsFromHand(){
        NumberCard blue1 = new NumberCard(Color.BLUE, 1);
        NumberCard red2 = new NumberCard(Color.RED, 2);

        Hand hand = new Hand();
        hand.push(blue1);
        hand.push(red2);
        assertEquals(2, hand.size());

        hand.clearHand();
        assertEquals(0, hand.size());

    }

}