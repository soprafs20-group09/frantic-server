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

    @Test
    public void getNameTest() {
        Event vandalism = new VandalismEvent(listOfPlayers, discardPile);
        assertEquals("vandalism", vandalism.getName());
    }

    @Test
    public void performEventTest() {
        Player player1 = new Player();
        player1.pushCardToHand(new Card(Color.RED, 7, 11));
        player1.pushCardToHand(new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTIC, false, 12));
        player1.pushCardToHand(new Card(Color.RED, Type.SPECIAL, Value.SKIP, true, 13));
        listOfPlayers.add(player1);

        this.discardPile.push(new Card(Color.RED, 3, 1));
        this.discardPile.push(new Card(Color.BLACK, 3, 2));

        assertEquals(3, player1.getHandSize());

        Event vandalism = new VandalismEvent(listOfPlayers, discardPile);
        vandalism.performEvent();
        assertEquals(1, player1.getHandSize());
    }

    @Test
    public void getMessageTest() {
        Event vandalism = new VandalismEvent(listOfPlayers, discardPile);
        assertEquals("Let all your anger out! You can discard all cards of the last played color! Do it! Just smash them on the discard Pile!", vandalism.getMessage());
    }
}