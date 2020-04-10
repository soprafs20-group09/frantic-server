package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MerryChristmasEventTest {

    private Event merryChristmas = new MerryChristmasEvent();

    @Test
    public void getNameTest() {
        assertEquals("merry-christmas", merryChristmas.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("Merry christmas everyone! It's that time of the year again. Give presents to your loved ones!", merryChristmas.getMessage());
    }
}