package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TheAllSeeingEyeEventTest {

    private Event theAllSeeingEye = new TheAllSeeingEyeEvent();

    @Test
    public void getNameTest() {
        assertEquals("the-all-seeing-eye", theAllSeeingEye.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("You can't run! You can't hide! The all-seeing eye is here! Take a good look at everyone cards!", theAllSeeingEye.getMessage());
    }
}