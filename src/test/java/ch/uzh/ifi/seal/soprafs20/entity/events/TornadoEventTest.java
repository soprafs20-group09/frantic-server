package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TornadoEventTest {

    private List<Player> listOfPlayers = new ArrayList<>();

    @Test
    public void getNameTest() {
        Event tornado = new TornadoEvent(listOfPlayers);
        assertEquals("tornado", tornado.getName());
    }

    @Test
    public void performEventTest() {
        Player player1 = new Player();
        player1.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 0));
        player1.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.NINE, false, 1));
        player1.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.TWO, false, 2));
        player1.pushCardToHand(new Card(Color.RED, Type.NUMBER, Value.EIGHT, false, 3));
        this.listOfPlayers.add(player1);

        Player player2 = new Player();
        player2.pushCardToHand(new Card(Color.YELLOW, Type.NUMBER, Value.ONE, false, 5));
        player2.pushCardToHand(new Card(Color.YELLOW, Type.NUMBER, Value.ONE, false, 6));
        this.listOfPlayers.add(player2);

        Event tornado = new TornadoEvent(listOfPlayers);
        tornado.performEvent();
        assertEquals(3, player1.getHandSize());
        assertEquals(3, player2.getHandSize());
    }

    @Test
    public void getMessageTest() {
        Event tornado = new TornadoEvent(listOfPlayers);
        assertEquals("Oh no! A tornado whirled all the cards around! Looks like we have to redistribute them!", tornado.getMessage());
    }
}