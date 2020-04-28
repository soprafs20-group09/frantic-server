package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.DiscardPile;
import ch.uzh.ifi.seal.soprafs20.entity.Pile;
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
import static org.mockito.Mockito.mock;

public class RecessionEventTest {

    private List<Player> listOfPlayers = new ArrayList<>();
    private Pile<Card> discardPile = new DiscardPile();
    private Player player1 = new Player();
    private Player player2 = new Player();
    private Event recession;

    @Mock
    private GameService gameService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        listOfPlayers.add(player1);
        listOfPlayers.add(player2);

        recession = new RecessionEvent("abc", player1, listOfPlayers, discardPile, gameService);
    }

    @Test
    public void getNameTest() {
        assertEquals("recession", recession.getName());
    }

    @Test
    public void performEventTest() {
        Mockito.doNothing().when(gameService).sendRecession(Mockito.any(), Mockito.any(), Mockito.anyInt());

        recession.performEvent();
        Mockito.verify(gameService, Mockito.times(1)).sendRecession(Mockito.matches("abc"), Mockito.any(), Mockito.eq(1));
        Mockito.verify(gameService, Mockito.times(1)).sendRecession(Mockito.matches("abc"), Mockito.any(), Mockito.eq(2));
    }

    @Test
    public void getMessageTest() {
        //TODO: make it dynamic
        assertEquals("One, two, three, ... Since you are the 3rd to discard, you can discard 3 cards!", recession.getMessage());
    }
}