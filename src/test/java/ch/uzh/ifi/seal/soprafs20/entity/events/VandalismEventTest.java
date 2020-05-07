package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VandalismEventTest {

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
        Event vandalism = new VandalismEvent(this.gameRound);
        assertEquals("vandalism", vandalism.getName());
    }

    @Test
    public void getMessageTest() {
        Event vandalism = new VandalismEvent(this.gameRound);
        assertEquals("Let all your anger out! You can discard all cards of the last played color! Do it! Just smash them on the discard Pile!", vandalism.getMessage());
    }

    @Test
    public void performEventTest() {
        Player player1 = new Player();
        player1.pushCardToHand(new Card(Color.RED, 7, 11));
        player1.pushCardToHand(new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTIC, false, 12));
        player1.pushCardToHand(new Card(Color.RED, Type.SPECIAL, Value.SKIP, true, 13));
        this.listOfPlayers.add(player1);

        DiscardPile pile = new DiscardPile();
        pile.push(new Card(Color.RED, 3, 1));
        pile.push(new Card(Color.BLACK, 3, 2));
        Mockito.when(this.gameRound.getDiscardPile()).thenReturn(pile);

        assertEquals(3, player1.getHandSize());

        Mockito.when(this.gameRound.getCurrentPlayer()).thenReturn(player1);
        Event vandalism = new VandalismEvent(this.gameRound);
        vandalism.performEvent();

        assertEquals(1, player1.getHandSize());
    }
}