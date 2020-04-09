package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThirdTimeLuckyEventTest {

    private Event thirdTimeLucky = new ThirdTimeLuckyEvent();

    @Test
    public void getNameTest() {
        assertEquals("third-time-lucky", thirdTimeLucky.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("Three cards for everyone!", thirdTimeLucky.getMessage());
    }
}