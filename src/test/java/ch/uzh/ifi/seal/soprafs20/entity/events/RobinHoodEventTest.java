package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RobinHoodEventTest {

    @Mock
    private GameService gameService;
    @Mock
    private GameRound gameRound;

    private List<Player> listOfPlayers = new ArrayList<>();

    @BeforeEach
    public void setup() {
        this.listOfPlayers = new ArrayList<>();

        MockitoAnnotations.initMocks(this);
        Mockito.when(this.gameRound.getGameService()).thenReturn(this.gameService);
        Mockito.when(this.gameRound.getListOfPlayers()).thenReturn(this.listOfPlayers);
    }

    @Test
    public void getNameTest() {
        RobinHoodEvent robinHood = new RobinHoodEvent(this.gameRound);
        assertEquals("robin-hood", robinHood.getName());
    }

    @Test
    public void getMessageTest() {
        RobinHoodEvent robinHood = new RobinHoodEvent(this.gameRound);
        assertEquals("Some call him a hero, some call him a thief! The player with the least cards has to swap cards with the player holding the most!", robinHood.getMessage());
    }

    @Test
    public void performEventTest_differentNumberOfCards() {
        Player player1 = new Player();
        player1.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 0));
        player1.pushCardToHand(new Card(Color.BLUE, Type.SPECIAL, Value.GIFT, true, 1));
        this.listOfPlayers.add(player1);

        Player player2 = new Player();
        List<Card> player2Cards = new ArrayList<>();
        player2Cards.add(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 0));
        player2.pushCardToHand(player2Cards.get(0));
        this.listOfPlayers.add(player2);

        Player player3 = new Player();
        List<Card> player3Cards = new ArrayList<>();
        player3Cards.add(new Card(Color.GREEN, Type.NUMBER, Value.ONE, false, 0));
        player3Cards.add(new Card(Color.BLUE, Type.NUMBER, Value.FIVE, false, 1));
        player3Cards.add(new Card(Color.YELLOW, Type.NUMBER, Value.THREE, false, 2));
        for (Card card : player3Cards) {
            player3.pushCardToHand(card);
        }
        this.listOfPlayers.add(player3);

        Mockito.when(this.gameRound.getCurrentPlayer()).thenReturn(player1);
        RobinHoodEvent robinHood = new RobinHoodEvent(this.gameRound);
        robinHood.performEvent();

        assertEquals(2, player1.getHandSize());
        assertEquals(3, player2.getHandSize());
        assertEquals(1, player3.getHandSize());

        for (int index = 0; index < player3Cards.size(); index++) {
            assertEquals(player3Cards.get(index).getColor(), player2.peekCard(index).getColor());
            assertEquals(player3Cards.get(index).getType(), player2.peekCard(index).getType());
            assertEquals(player3Cards.get(index).getValue(), player2.peekCard(index).getValue());
        }
        for (int index = 0; index < player2Cards.size(); index++) {
            assertEquals(player2Cards.get(index).getColor(), player3.peekCard(index).getColor());
            assertEquals(player2Cards.get(index).getType(), player3.peekCard(index).getType());
            assertEquals(player2Cards.get(index).getValue(), player3.peekCard(index).getValue());
        }
    }

    @Test
    public void performEventTest_sameNumberOfCards() {
        Player player1 = new Player();
        List<Card> player1Cards = new ArrayList<>();
        player1Cards.add(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 0));
        player1Cards.add(new Card(Color.BLUE, Type.SPECIAL, Value.GIFT, true, 1));
        for (Card card : player1Cards) {
            player1.pushCardToHand(card);
        }
        this.listOfPlayers.add(player1);

        Player player2 = new Player();
        List<Card> player2Cards = new ArrayList<>();
        player2Cards.add(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 2));
        player2.pushCardToHand(player2Cards.get(0));
        this.listOfPlayers.add(player2);

        Player player3 = new Player();
        List<Card> player3Cards = new ArrayList<>();
        player3Cards.add(new Card(Color.GREEN, Type.NUMBER, Value.ONE, false, 3));
        player3Cards.add(new Card(Color.BLUE, Type.NUMBER, Value.FIVE, false, 4));
        for (Card card : player3Cards) {
            player3.pushCardToHand(card);
        }
        this.listOfPlayers.add(player3);

        Mockito.when(this.gameRound.getCurrentPlayer()).thenReturn(player1);
        RobinHoodEvent robinHood = new RobinHoodEvent(this.gameRound);
        robinHood.performEvent();

        assertEquals(2, player1.getHandSize());
        assertEquals(2, player2.getHandSize());
        assertEquals(1, player3.getHandSize());

        for (int index = 0; index < player1Cards.size(); index++) {
            assertEquals(player1Cards.get(index).getColor(), player1.peekCard(index).getColor());
            assertEquals(player1Cards.get(index).getType(), player1.peekCard(index).getType());
            assertEquals(player1Cards.get(index).getValue(), player1.peekCard(index).getValue());
        }
        for (int index = 0; index < player3Cards.size(); index++) {
            assertEquals(player3Cards.get(index).getColor(), player2.peekCard(index).getColor());
            assertEquals(player3Cards.get(index).getType(), player2.peekCard(index).getType());
            assertEquals(player3Cards.get(index).getValue(), player2.peekCard(index).getValue());
        }
        for (int index = 0; index < player2Cards.size(); index++) {
            assertEquals(player2Cards.get(index).getColor(), player3.peekCard(index).getColor());
            assertEquals(player2Cards.get(index).getType(), player3.peekCard(index).getType());
            assertEquals(player2Cards.get(index).getValue(), player3.peekCard(index).getValue());
        }
    }

    @Test
    public void performEventTest_nothingShouldHappen() {
        Player player1 = new Player();
        List<Card> player1Cards = new ArrayList<>();
        player1Cards.add(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 0));
        player1Cards.add(new Card(Color.BLUE, Type.SPECIAL, Value.GIFT, true, 1));
        for (Card card : player1Cards) {
            player1.pushCardToHand(card);
        }
        this.listOfPlayers.add(player1);

        Player player2 = new Player();
        List<Card> player2Cards = new ArrayList<>();
        player2Cards.add(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 2));
        player2Cards.add(new Card(Color.BLACK, Type.NUMBER, Value.FOUR, false, 3));
        for (Card card : player2Cards) {
            player2.pushCardToHand(card);
        }
        this.listOfPlayers.add(player2);

        Player player3 = new Player();
        List<Card> player3Cards = new ArrayList<>();
        player3Cards.add(new Card(Color.GREEN, Type.NUMBER, Value.ONE, false, 4));
        player3Cards.add(new Card(Color.BLUE, Type.NUMBER, Value.FIVE, false, 5));
        for (Card card : player3Cards) {
            player3.pushCardToHand(card);
        }
        this.listOfPlayers.add(player3);

        Mockito.when(this.gameRound.getCurrentPlayer()).thenReturn(player1);
        RobinHoodEvent robinHood = new RobinHoodEvent(this.gameRound);
        robinHood.performEvent();

        assertEquals(2, player1.getHandSize());
        assertEquals(2, player2.getHandSize());
        assertEquals(2, player3.getHandSize());

        for (int index = 0; index < player1Cards.size(); index++) {
            assertEquals(player1Cards.get(index).getColor(), player1.peekCard(index).getColor());
            assertEquals(player1Cards.get(index).getType(), player1.peekCard(index).getType());
            assertEquals(player1Cards.get(index).getValue(), player1.peekCard(index).getValue());
        }
        for (int index = 0; index < player2Cards.size(); index++) {
            assertEquals(player2Cards.get(index).getColor(), player2.peekCard(index).getColor());
            assertEquals(player2Cards.get(index).getType(), player2.peekCard(index).getType());
            assertEquals(player2Cards.get(index).getValue(), player2.peekCard(index).getValue());
        }
        for (int index = 0; index < player3Cards.size(); index++) {
            assertEquals(player3Cards.get(index).getColor(), player3.peekCard(index).getColor());
            assertEquals(player3Cards.get(index).getType(), player3.peekCard(index).getType());
            assertEquals(player3Cards.get(index).getValue(), player3.peekCard(index).getValue());
        }
    }
}