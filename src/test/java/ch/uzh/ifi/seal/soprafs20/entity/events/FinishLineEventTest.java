package ch.uzh.ifi.seal.soprafs20.entity.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FinishLineEventTest {

    private Event finishLine = new FinishLineEvent();

    @Test
    public void getNameTest() {
        assertEquals("finish-line", finishLine.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("Looks like the round is over! Count your points!", finishLine.getMessage());
    }
}