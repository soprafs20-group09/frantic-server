package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RobinHoodEventTest {

    private Event robinHood = new RobinHoodEvent();

    @Test
    public void getNameTest() {
        assertEquals("robin-hood", robinHood.getName());
    }

    @Test
    public void getMessageTest() {
        //TODO: make it dynamic
        assertEquals("Some call him a hero, some call him a thief! The player with the least cards has to swap cards with the player holding the most!", robinHood.getMessage());
    }
}