package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecessionEventTest {

    private Event recession = new RecessionEvent();

    @Test
    public void getNameTest() {
        assertEquals("recession", recession.getName());
    }

    @Test
    public void getMessageTest() {
        //TODO: make it dynamic
        assertEquals("One, two, three, ... Since you are the 3rd to discard, you can discard 3 cards!", recession.getMessage());
    }
}