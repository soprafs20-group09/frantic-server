package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TornadoEventTest {

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
        Event tornado = new TornadoEvent(this.gameRound);
        assertEquals("tornado", tornado.getName());
    }

    @Test
    public void getMessageTest() {
        Event tornado = new TornadoEvent(this.gameRound);
        assertEquals("Oh no! A tornado whirled all the cards around! Looks like we have to redistribute them!", tornado.getMessage());
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

        Mockito.when(this.gameRound.getCurrentPlayer()).thenReturn(player2);
        Event tornado = new TornadoEvent(this.gameRound);
        tornado.performEvent();

        assertEquals(3, player1.getHandSize());
        assertEquals(3, player2.getHandSize());
    }
}