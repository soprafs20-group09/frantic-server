package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatingSeasonEventTest {

    private Event matingSeason = new MatingSeasonEvent();

    @Test
    public void getNameTest() {
        assertEquals("mating-season", matingSeason.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("It's valentines day! Well, at least for your cards! Discard numeral pairs, triples and so on.", matingSeason.getMessage());
    }
}