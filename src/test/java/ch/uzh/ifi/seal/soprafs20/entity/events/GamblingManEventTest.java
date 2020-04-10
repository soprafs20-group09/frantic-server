package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GamblingManEventTest {

    private Event gamblingMan = new GamblingManEvent();

    @Test
    public void getNameTest() {
        assertEquals("gambling-man", gamblingMan.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("It's time to gamble! Choose a number card of the last played color. The player with the highest digit has to take all of them. So choose wisely!", gamblingMan.getMessage());
    }
}