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

public class ExpansionEventTest {

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
        ExpansionEvent expansion = new ExpansionEvent(this.gameRound);
        assertEquals("expansion", expansion.getName());
    }

    @Test
    public void getMessageTest() {
        ExpansionEvent expansion = new ExpansionEvent(this.gameRound);
        assertEquals("Cards are selling like hot cakes! Grab one or two or three ...", expansion.getMessage());
    }

    @Test
    public void performEventTest() {
        Player player1 = new Player();
        player1.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 0));
        player1.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 1));
        player1.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 2));
        player1.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 3));
        this.listOfPlayers.add(player1);

        Player player2 = new Player();
        player2.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 4));
        player2.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 5));
        this.listOfPlayers.add(player2);

        Player player3 = new Player();
        player3.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 6));
        player3.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 7));
        player3.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 8));
        this.listOfPlayers.add(player3);

        Player player4 = new Player();
        player4.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 9));
        player4.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 10));
        player4.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 11));
        player4.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 12));
        player4.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 13));
        this.listOfPlayers.add(player4);

        Mockito.when(this.gameRound.getCurrentPlayer()).thenReturn(player2);

        ExpansionEvent e = new ExpansionEvent(this.gameRound);
        e.performEvent();

        Mockito.verify(this.gameRound, Mockito.times(1)).drawCardFromStack(player1, 3);
        Mockito.verify(this.gameRound, Mockito.times(1)).drawCardFromStack(player2, 4);
        Mockito.verify(this.gameRound, Mockito.times(1)).drawCardFromStack(player3, 1);
        Mockito.verify(this.gameRound, Mockito.times(1)).drawCardFromStack(player4, 2);
    }
}