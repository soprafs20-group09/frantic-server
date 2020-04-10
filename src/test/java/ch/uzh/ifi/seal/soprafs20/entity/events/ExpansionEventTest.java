package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpansionEventTest {

    private Event expansion = new ExpansionEvent();

    @Test
    public void getNameTest() {
        assertEquals("expansion", expansion.getName());
    }

    @Test
    public void getMessageTest() {
        //TODO: make it dynamic
        assertEquals("One, Two, Three, ... Since you are the 3rd to draw, you have to draw 3 cards!", expansion.getMessage());
    }
}