package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TheAllSeeingEyeEventTest {

    @Mock
    private GameRound gameRound;

    private Event theAllSeeingEye = new TheAllSeeingEyeEvent(gameRound);

    @Test
    public void getNameTest() {
        assertEquals("the-all-seeing-eye", theAllSeeingEye.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("You can't run! You can't hide! The all-seeing eye is here! Take a good look at everyone cards!", theAllSeeingEye.getMessage());
    }
}