package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SurprisePartyEventTest {

    private Event surpriseParty = new SurprisePartyEvent();

    @Test
    public void getNameTest() {
        assertEquals("surprise-party", surpriseParty.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("Surprise another player by gifting them one of your cards!", surpriseParty.getMessage());
    }
}