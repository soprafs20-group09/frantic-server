package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VandalismEventTest {

    private Event vandalism = new VandalismEvent();

    @Test
    public void getNameTest() {
        assertEquals("vandalism", vandalism.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("Let all your anger out! You can discard all cards of the last played color! Do it! Just smash them on the discard Pile!", vandalism.getMessage());
    }
}