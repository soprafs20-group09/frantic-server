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

public class CommunismEventTest {

    @Mock
    private GameService gameService;
    @Mock
    private GameRound gameRound;

    private List<Player> listOfPlayers = new ArrayList<>();

    @BeforeEach
    void setup() {
        this.listOfPlayers = new ArrayList<>();

        MockitoAnnotations.initMocks(this);
        Mockito.when(this.gameRound.getGameService()).thenReturn(this.gameService);
        Mockito.when(this.gameRound.getListOfPlayers()).thenReturn(this.listOfPlayers);
    }

    @Test
    void getNameTest() {
        CommunismEvent communism = new CommunismEvent(this.gameRound);
        assertEquals("communism", communism.getName());
    }

    @Test
    void getMessageTest() {
        CommunismEvent communism = new CommunismEvent(this.gameRound);
        assertEquals("Everybody is equal, now isn't that great?", communism.getMessage());
    }

    @Test
    void performEventTest() {
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

        Mockito.when(this.gameRound.getCurrentPlayer()).thenReturn(player2);
        CommunismEvent c = new CommunismEvent(this.gameRound);
        c.performEvent();

        Mockito.verify(this.gameRound, Mockito.times(1)).drawCardFromStack(player1, 5);
        Mockito.verify(this.gameRound, Mockito.times(1)).drawCardFromStack(player2, 4);
        Mockito.verify(this.gameRound, Mockito.times(1)).drawCardFromStack(player3, 3);
        Mockito.verify(this.gameRound, Mockito.times(0)).drawCardFromStack(player4, 0);
    }
}