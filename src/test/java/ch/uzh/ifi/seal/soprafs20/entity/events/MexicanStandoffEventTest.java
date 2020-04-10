package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MexicanStandoffEventTest {

    private Event mexicanStandoff = new MexicanStandoffEvent();

    @Test
    public void getNameTest() {
        assertEquals("mexican-standoff", mexicanStandoff.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("Show your skills off! Everyone gets rid of their cards and gets three new ones! Who can finish first?", mexicanStandoff.getMessage());
    }
}