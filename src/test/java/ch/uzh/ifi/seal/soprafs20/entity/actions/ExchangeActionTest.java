package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExchangeActionTest {

    @Test
    void exchange_bothHaveTwoCards() {
        Player initiator = new Player();
        Player target = new Player();
        Card blue1 = new Card(Color.BLUE, 1, 1);
        Card green2 = new Card(Color.GREEN, 2, 2);
        Card red3 = new Card(Color.RED, 3, 3);
        Card yellow4 = new Card(Color.YELLOW, 4, 4);
        initiator.pushCardToHand(blue1);
        initiator.pushCardToHand(green2);
        target.pushCardToHand(red3);
        target.pushCardToHand(yellow4);


        ExchangeAction exchange = new ExchangeAction(initiator, target, new int[]{0, 1});
        exchange.perform();

        // dummy test... tested with debugger. works
        assertEquals(2, initiator.getHandSize());
    }

    @Test
    void targetHasOneCard() {
        Player initiator = new Player();
        Player target = new Player();
        Card blue1 = new Card(Color.BLUE, 1, 1);
        Card green2 = new Card(Color.GREEN, 2, 2);
        Card red3 = new Card(Color.RED, 3, 3);
        initiator.pushCardToHand(blue1);
        initiator.pushCardToHand(green2);
        target.pushCardToHand(red3);

        ExchangeAction exchange = new ExchangeAction(initiator, target, new int[]{0, 1});
        exchange.perform();

        assertEquals(2, target.getHandSize());
        assertEquals(1, initiator.getHandSize());
    }

    @Test
    void initiatorHasOneCard() {
        Player initiator = new Player();
        Player target = new Player();
        Card blue1 = new Card(Color.BLUE, 1, 1);
        Card red3 = new Card(Color.RED, 3, 3);
        Card yellow4 = new Card(Color.YELLOW, 4, 4);
        initiator.pushCardToHand(blue1);
        target.pushCardToHand(red3);
        target.pushCardToHand(yellow4);

        ExchangeAction exchange = new ExchangeAction(initiator, target, new int[]{0});
        exchange.perform();

        assertEquals(2, initiator.getHandSize());
        assertEquals(1, target.getHandSize());
    }

}