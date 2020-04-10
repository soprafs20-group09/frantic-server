package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.cards.NumberCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {

    @Test
    public void pushCardToHandAndPopCardFromHand() {
        NumberCard blue1 = new NumberCard(Color.BLUE, 1,1 );

        Hand hand = new Hand();
        hand.push(blue1);
        assertEquals(1, hand.size());
        assertEquals(blue1, hand.pop(0));
        assertEquals(0, hand.size());
    }

    @Test
    public void pushCardToHandOrdering() {
        NumberCard blue1 = new NumberCard(Color.BLUE, 1, 1);
        NumberCard blue2 = new NumberCard(Color.BLUE, 2, 2);
        NumberCard blue3 = new NumberCard(Color.BLUE, 3, 3);
        NumberCard blue4 = new NumberCard(Color.BLUE, 4, 4);
        NumberCard red1 = new NumberCard(Color.RED, 1, 5 );
        NumberCard red2 = new NumberCard(Color.RED, 2, 6 );
        NumberCard red3 = new NumberCard(Color.RED, 3, 7 );

        Hand hand = new Hand();

        //push in a random order
        hand.push(red3);
        hand.push(red2);
        hand.push(red1);
        hand.push(blue1);
        hand.push(blue4);
        hand.push(blue2);
        hand.push(blue3);

        //make sure the cards are ordered
        assertEquals(blue1, hand.pop(0));
        assertEquals(blue2, hand.pop(0));
        assertEquals(blue3, hand.pop(0));
        assertEquals(blue4, hand.pop(0));
        assertEquals(red1, hand.pop(0));
        assertEquals(red2, hand.pop(0));
        assertEquals(red3, hand.pop(0));
    }

    @Test
    public void sizeOfHand() {
        NumberCard blue1 = new NumberCard(Color.BLUE, 1, 1);
        NumberCard red2 = new NumberCard(Color.RED, 2, 2 );

        Hand hand = new Hand();
        assertEquals(0, hand.size());
        hand.push(blue1);
        assertEquals(1, hand.size());
        hand.push(red2);
        assertEquals(2, hand.size());
        hand.pop(0);
        assertEquals(1, hand.size());
        hand.pop(0);
        assertEquals(0, hand.size());
    }

    @Test
    public void clearAllCardsFromHand(){
        NumberCard blue1 = new NumberCard(Color.BLUE, 1, 1);
        NumberCard red2 = new NumberCard(Color.RED, 2, 2 );

        Hand hand = new Hand();
        hand.push(blue1);
        hand.push(red2);
        assertEquals(2, hand.size());

        hand.clearHand();
        assertEquals(0, hand.size());

    }

}