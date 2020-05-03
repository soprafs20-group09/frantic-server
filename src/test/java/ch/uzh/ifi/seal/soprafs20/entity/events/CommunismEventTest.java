package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.DrawStack;
import ch.uzh.ifi.seal.soprafs20.entity.Pile;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommunismEventTest {

    private List<Player> listOfPlayers = new ArrayList<>();
    private final Pile<Card> drawStack = new DrawStack();

    @Test
    public void getNameTest() {
        CommunismEvent communism = new CommunismEvent(this.listOfPlayers, this.drawStack);
        assertEquals("communism", communism.getName());
    }

    @Test
    public void getMessageTest() {
        CommunismEvent communism = new CommunismEvent(this.listOfPlayers, this.drawStack);
        assertEquals("Everybody is equal, now isn't that great?", communism.getMessage());
    }

    @Test
    public void performEventTest() {
        Player player1 = new Player();
        player1.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.ONE, false, 0));
        this.listOfPlayers.add(player1);

        Player player2 = new Player();
        player2.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.TWO, false, 1));
        player2.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.THREE, false, 2));
        this.listOfPlayers.add(player2);

        Player player3 = new Player();
        player3.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.FOUR, false, 3));
        player3.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.FIVE, false, 4));
        player3.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.SIX, false, 5));
        this.listOfPlayers.add(player3);

        Player player4 = new Player();
        player4.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.SEVEN, false, 6));
        player4.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 7));
        player4.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.NINE, false, 8));
        player4.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.ONE, false, 9));
        player4.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.TWO, false, 10));
        player4.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.THREE, false, 11));
        this.listOfPlayers.add(player4);

        assertEquals(125, this.drawStack.size());

        CommunismEvent c = new CommunismEvent(this.listOfPlayers, this.drawStack);
        c.performEvent();

        assertEquals(6, player1.getHandSize());
        assertEquals(6, player2.getHandSize());
        assertEquals(6, player3.getHandSize());
        assertEquals(6, player4.getHandSize());

        assertEquals(113, this.drawStack.size());
    }
}