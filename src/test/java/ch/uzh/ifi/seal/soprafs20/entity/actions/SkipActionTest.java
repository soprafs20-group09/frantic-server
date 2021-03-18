package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SkipActionTest {

    @Test
    void targetIsNotBlocked() {
        Player initiator = new Player();
        Player target = new Player();

        SkipAction skip = new SkipAction(initiator, target);
        skip.perform();
        assertTrue(target.isBlocked());
    }

    @Test
    void targetIsAlreadyBlocked() {
        Player initiator = new Player();
        Player target = new Player();
        target.setBlocked(true);

        SkipAction skip = new SkipAction(initiator, target);
        skip.perform();
        assertTrue(target.isBlocked());
    }

}