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

    @Mock
    private GameService gameService;

    @Test
    public void getNameTest() {
        Event recession = new RecessionEvent("abc", player1, listOfPlayers, gameService);
        assertEquals("recession", recession.getName());
    }

    @Test
    public void performEventTest() {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(gameService).sendRecession(Mockito.any(), Mockito.any(), Mockito.anyInt());

        Player player1 = new Player();
        listOfPlayers.add(player1);
        Player player2 = new Player();
        listOfPlayers.add(player2);
        Player player3 = new Player();
        listOfPlayers.add(player3);

        Event recession = new RecessionEvent("abc", player1, listOfPlayers, gameService);
        recession.performEvent();
        Mockito.verify(gameService, Mockito.times(1)).sendRecession(Mockito.matches("abc"), Mockito.any(), Mockito.eq(1));
        Mockito.verify(gameService, Mockito.times(1)).sendRecession(Mockito.matches("abc"), Mockito.any(), Mockito.eq(2));
        Mockito.verify(gameService, Mockito.times(1)).sendRecession(Mockito.matches("abc"), Mockito.any(), Mockito.eq(3));
    }

    @Test
    public void getMessageTest() {
        Event recession = new RecessionEvent("abc", player1, listOfPlayers, gameService);
        assertEquals("One, two, three, ... Since you are the 3rd to discard, you can discard 3 cards!", recession.getMessage());
    }
}