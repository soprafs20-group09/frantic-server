package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeBombEventTest {

    private Event timeBomb = new TimeBombEvent();

    @Test
    public void getNameTest() {
        assertEquals("time-bomb", timeBomb.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("Tick ... Tick ... Tick ... Boom! Everyone has three turns left! Defuse the Bomb and earn a reward by winning the round or let the Bomb explode and everyone's points in this round get doubled!", timeBomb.getMessage());
    }
}