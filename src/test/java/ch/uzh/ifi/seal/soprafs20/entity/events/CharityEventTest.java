package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CharityEventTest {

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
        CharityEvent charity = new CharityEvent(this.gameRound);
        assertEquals("charity", charity.getName());
    }

    @Test
    public void getMessageTest() {
        CharityEvent charity = new CharityEvent(this.gameRound);
        assertEquals("How noble of you! You pick a card from the player with the most cards!", charity.getMessage());
    }

    @Test
    public void performEventTest() {
        Player player1 = new Player();
        player1.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 0));
        player1.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.NINE, false, 1));
        player1.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.TWO, false, 2));
        player1.pushCardToHand(new Card(Color.RED, Type.NUMBER, Value.EIGHT, false, 3));
        player1.pushCardToHand(new Card(Color.BLACK, Type.SPECIAL, Value.FUCKYOU, false, 4));
        this.listOfPlayers.add(player1);

        Player player2 = new Player();
        player2.pushCardToHand(new Card(Color.YELLOW, Type.NUMBER, Value.ONE, false, 5));
        player2.pushCardToHand(new Card(Color.YELLOW, Type.NUMBER, Value.ONE, false, 6));
        this.listOfPlayers.add(player2);

        Player player3 = new Player();
        player3.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, false, 7));
        player3.pushCardToHand(new Card(Color.BLACK, Type.NUMBER, Value.SEVEN, false, 8));
        player3.pushCardToHand(new Card(Color.BLUE, Type.SPECIAL, Value.GIFT, false, 9));
        this.listOfPlayers.add(player3);

        Player player4 = new Player();
        player4.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.FIVE, false, 10));
        player4.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.FIVE, false, 11));
        player4.pushCardToHand(new Card(Color.YELLOW, Type.NUMBER, Value.FIVE, false, 12));
        player4.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 13));
        player4.pushCardToHand(new Card(Color.RED, Type.NUMBER, Value.EIGHT, false, 14));
        this.listOfPlayers.add(player4);

        Mockito.when(this.gameRound.getCurrentPlayer()).thenReturn(player2);

        CharityEvent c = new CharityEvent(this.gameRound);
        c.performEvent();

        assertEquals(3, player1.getHandSize());
        assertEquals(4, player2.getHandSize());
        assertEquals(5, player3.getHandSize());
        assertEquals(3, player4.getHandSize());
    }

    @Test
    public void performEventTest_playerHasNoMoreCards() {
        Player player1 = new Player();
        player1.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 0));
        this.listOfPlayers.add(player1);

        Player player2 = new Player();
        player2.pushCardToHand(new Card(Color.YELLOW, Type.NUMBER, Value.ONE, false, 1));
        this.listOfPlayers.add(player2);

        Player player3 = new Player();
        player3.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, false, 2));
        this.listOfPlayers.add(player3);

        Player player4 = new Player();
        player4.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.FIVE, false, 3));
        player4.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.FIVE, false, 4));
        this.listOfPlayers.add(player4);

        Mockito.when(this.gameRound.getCurrentPlayer()).thenReturn(player2);

        CharityEvent c = new CharityEvent(this.gameRound);
        c.performEvent();

        assertEquals(2, player1.getHandSize());
        assertEquals(1, player2.getHandSize());
        assertEquals(2, player3.getHandSize());
        assertEquals(0, player4.getHandSize());
    }
}
