package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EarthquakeEventTest {

    private List<Player> listOfPlayers = new ArrayList<>();

    @Test
    public void getNameTest() {
        EarthquakeEvent earthquake = new EarthquakeEvent(this.listOfPlayers);
        assertEquals("earthquake", earthquake.getName());
    }

    @Test
    public void getMessageTest() {
        EarthquakeEvent earthquake = new EarthquakeEvent(this.listOfPlayers);
        assertEquals("Oh no! Everything is shaken up! Good luck with your new cards!", earthquake.getMessage());
    }

    @Test
    public void performEventTest() {
        Player player1 = new Player();
        List<Card> player1Cards = new ArrayList<>();
        player1Cards.add(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 0));
        for (Card card : player1Cards) {
            player1.pushCardToHand(card);
        }
        this.listOfPlayers.add(player1);

        Player player2 = new Player();
        List<Card> player2Cards = new ArrayList<>();
        player2Cards.add(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 0));
        player2Cards.add(new Card(Color.YELLOW, Type.NUMBER, Value.EIGHT, false, 1));
        for (Card card : player2Cards) {
            player2.pushCardToHand(card);
        }
        this.listOfPlayers.add(player2);

        Player player3 = new Player();
        List<Card> player3Cards = new ArrayList<>();
        player3Cards.add(new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTICFOUR, true, 0));
        player3Cards.add(new Card(Color.GREEN, Type.NUMBER, Value.THREE, false, 1));
        player3Cards.add(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 2));
        for (Card card : player3Cards) {
            player3.pushCardToHand(card);
        }
        this.listOfPlayers.add(player3);

        EarthquakeEvent e = new EarthquakeEvent(this.listOfPlayers);
        e.performEvent();

        assertEquals(player1Cards.size(), player2.getHandSize());
        assertEquals(player2Cards.size(), player3.getHandSize());
        assertEquals(player3Cards.size(), player1.getHandSize());

        for (int index = 0; index < player1Cards.size(); index++) {
            assertEquals(player1Cards.get(index).getColor(), player2.peekCard(index).getColor());
            assertEquals(player1Cards.get(index).getType(), player2.peekCard(index).getType());
            assertEquals(player1Cards.get(index).getValue(), player2.peekCard(index).getValue());
        }
        for (int index = 0; index < player2Cards.size(); index++) {
            assertEquals(player2Cards.get(index).getColor(), player3.peekCard(index).getColor());
            assertEquals(player2Cards.get(index).getType(), player3.peekCard(index).getType());
            assertEquals(player2Cards.get(index).getValue(), player3.peekCard(index).getValue());
        }
        for (int index = 0; index < player3Cards.size(); index++) {
            assertEquals(player3Cards.get(index).getColor(), player1.peekCard(index).getColor());
            assertEquals(player3Cards.get(index).getType(), player1.peekCard(index).getType());
            assertEquals(player3Cards.get(index).getValue(), player1.peekCard(index).getValue());
        }
    }
}