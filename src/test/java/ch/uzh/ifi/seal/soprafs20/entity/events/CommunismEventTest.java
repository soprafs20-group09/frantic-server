package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommunismEventTest {

    private Event communism = new CommunismEvent();

    @Test
    public void getNameTest() {
        assertEquals("communism", communism.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("Everybody is equal, now isn't that great?", communism.getMessage());
    }
}