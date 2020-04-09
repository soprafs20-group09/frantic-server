package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarketEventTest {

    private Event market = new MarketEvent();

    @Test
    public void getNameTest() {
        assertEquals("market", market.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("Choose one of these incredibly awesome cards for the great price of only 0$!", market.getMessage());
    }
}