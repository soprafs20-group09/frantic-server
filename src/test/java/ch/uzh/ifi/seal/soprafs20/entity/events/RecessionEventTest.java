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
import static org.mockito.Mockito.mock;

public class RecessionEventTest {

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
        Mockito.when(this.gameRound.getLobbyId()).thenReturn("abc");
    }

    @Test
    void getNameTest() {
        Event recession = new RecessionEvent(this.gameRound);
        assertEquals("recession", recession.getName());
    }

    @Test
    void getMessageTest() {
        Event recession = new RecessionEvent(this.gameRound);
        assertEquals("Card Stocks are going down! Dispose one or two or three ...", recession.getMessage());
    }

    @Test
    void performEventTest() {
        Mockito.doNothing().when(gameService).sendRecession(Mockito.any(), Mockito.any(), Mockito.anyInt());

        Player player1 = new Player();
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 1));
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 2));
        this.listOfPlayers.add(player1);

        Player player2 = new Player();
        player2.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 3));
        player2.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 4));
        this.listOfPlayers.add(player2);
        Player player3 = new Player();
        player3.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 5));
        this.listOfPlayers.add(player3);

        Mockito.when(this.gameRound.getCurrentPlayer()).thenReturn(player1);
        Event recession = new RecessionEvent(this.gameRound);
        recession.performEvent();
        Mockito.verify(gameService, Mockito.times(1)).sendRecession("abc", player1, 2);
        Mockito.verify(gameService, Mockito.times(1)).sendRecession("abc", player2, 1);
        Mockito.verify(gameService, Mockito.times(1)).sendRecession("abc", player3, 1);
    }

}