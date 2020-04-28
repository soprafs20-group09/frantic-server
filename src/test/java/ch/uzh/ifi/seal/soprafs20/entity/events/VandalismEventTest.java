package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.DiscardPile;
import ch.uzh.ifi.seal.soprafs20.entity.Pile;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VandalismEventTest {

    private List<Player> listOfPlayers = new ArrayList<>();
    private Pile<Card> discardPile = new DiscardPile();
    private Player player1 = new Player();
    private Event vandalism;

    @BeforeEach
    public void setup() {
        player1.pushCardToHand(new Card(Color.RED, 7, 11));
        player1.pushCardToHand(new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTIC, false, 12));
        player1.pushCardToHand(new Card(Color.RED, Type.SPECIAL, Value.SKIP, true, 13));
        listOfPlayers.add(player1);

        this.discardPile.push(new Card(Color.RED, 3, 1));
        this.discardPile.push(new Card(Color.BLACK, 3, 2));

        vandalism = new VandalismEvent(listOfPlayers, discardPile);
    }

    @Test
    public void getNameTest() {
        assertEquals("vandalism", vandalism.getName());
    }

    @Test
    public void performEventTest() {
        assertEquals(3, this.player1.getHandSize());
        this.vandalism.performEvent();
        assertEquals(1, this.player1.getHandSize());
    }

    @Test
    public void getMessageTest() {
        assertEquals("Let all your anger out! You can discard all cards of the last played color! Do it! Just smash them on the discard Pile!", vandalism.getMessage());
    }
}