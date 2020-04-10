package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FridayTheThirteenthEventTest {

    private Event fridayTheThirteenth = new FridayTheThirteenthEvent();

    @Test
    public void getNameTest() {
        assertEquals("friday-the-13th", fridayTheThirteenth.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("It’s Friday, the Thirteenth. A hook-handed murderer is among us! But just in the movies, it’s a totally boring, normal Friday, nothing weird happens. The game round continues without further ado.", fridayTheThirteenth.getMessage());
    }
}