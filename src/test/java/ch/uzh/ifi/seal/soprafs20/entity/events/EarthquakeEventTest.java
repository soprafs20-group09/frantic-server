package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EarthquakeEventTest {

    private Event earthquake = new EarthquakeEvent();

    @Test
    public void getNameTest() {
        assertEquals("earthquake", earthquake.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("Oh no! Everything is shaken up! Good luck with your new cards!", earthquake.getMessage());
    }
}