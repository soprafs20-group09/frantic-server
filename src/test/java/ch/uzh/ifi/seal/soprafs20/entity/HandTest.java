package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HandTest {

    @Test
    void pushCardToHandAndPopCardFromHand() {
        Card blue1 = new Card(Color.BLUE, 1, 1);

        Hand hand = new Hand();
        hand.push(blue1);
        assertEquals(1, hand.size());
        assertEquals(blue1, hand.pop(0));
        assertEquals(0, hand.size());
    }

    @Test
    void pushCardToHandOrdering() {
        Card blue1 = new Card(Color.BLUE, 1, 1);
        Card blue2 = new Card(Color.BLUE, 2, 2);
        Card blue3 = new Card(Color.BLUE, 3, 3);
        Card blue4 = new Card(Color.BLUE, 4, 4);
        Card red1 = new Card(Color.RED, 1, 5);
        Card red2 = new Card(Color.RED, 2, 6);
        Card red3 = new Card(Color.RED, 3, 7);

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
    void sizeOfHand() {
        Card blue1 = new Card(Color.BLUE, 1, 1);
        Card red2 = new Card(Color.RED, 2, 2);

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
    void clearAllCardsFromHand() {
        Card blue1 = new Card(Color.BLUE, 1, 1);
        Card red2 = new Card(Color.RED, 2, 2);

        Hand hand = new Hand();
        hand.push(blue1);
        hand.push(red2);
        assertEquals(2, hand.size());

        hand.clearHand();
        assertEquals(0, hand.size());

    }

}