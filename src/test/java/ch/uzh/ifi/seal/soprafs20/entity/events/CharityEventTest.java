package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CharityEventTest {

    private Event charity = new CharityEvent();

    @Test
    public void getNameTest() {
        assertEquals("charity", charity.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("How noble of you! You pick a card from the player with the most cards!", charity.getMessage());
    }
}
