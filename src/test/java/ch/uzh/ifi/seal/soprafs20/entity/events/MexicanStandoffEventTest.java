package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MexicanStandoffEventTest {

    private List<Player> listOfPlayers = new ArrayList<>();
    private Pile<Card> discardPile = new DiscardPile();
    private Pile<Card> drawStack = new DrawStack();
    private Player player1 = new Player();
    private Event mexicanStandoff;

    @BeforeEach
    public void setup() {
        player1.pushCardToHand(new Card(Color.RED, 7, 11));
        player1.pushCardToHand(new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTIC, false, 12));
        player1.pushCardToHand(new Card(Color.BLUE, Type.SPECIAL, Value.SKIP, true, 13));
        player1.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.SECONDCHANCE, true, 14));
        listOfPlayers.add(player1);

        mexicanStandoff = new MexicanStandoffEvent(listOfPlayers, discardPile, drawStack);
    }

    @Test
    public void getNameTest() {
        assertEquals("mexican-standoff", mexicanStandoff.getName());
    }

    @Test
    public void performEventTest() {
        Card c = new Card(Color.RED, 3, 1);

        assertEquals(4, this.player1.getHandSize());
        assertEquals(125, this.drawStack.size());

        this.mexicanStandoff.performEvent();
        assertEquals(3, this.player1.getHandSize());
        assertEquals(4, this.discardPile.size());
        assertEquals(122, this.drawStack.size());
    }

    @Test
    public void getMessageTest() {
        assertEquals("Show your skills off! Everyone gets rid of their cards and gets three new ones! Who can finish first?", mexicanStandoff.getMessage());
    }
}