package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.DiscardPile;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NiceTryActionTest {

    @Test
    void wishColor() {
        DiscardPile discardPile = new DiscardPile();
        Player initiator = new Player();
        initiator.setUsername("Testplayer");

        NiceTryAction niceTry = new NiceTryAction(initiator, Color.BLUE, discardPile);
        List<Chat> chat = niceTry.perform();

        String chatMessage = chat.get(0).getMessage();
        String chatType = chat.get(0).getType();
        String chatIcon = chat.get(0).getIcon();
        Card wished = discardPile.peek();

        assertEquals(Color.BLUE, wished.getColor());
        assertEquals(Type.WISH, wished.getType());
        assertEquals("Testplayer wished blue.", chatMessage);
        assertEquals("event", chatType);
        assertEquals("special:nice-try", chatIcon);
    }

}
