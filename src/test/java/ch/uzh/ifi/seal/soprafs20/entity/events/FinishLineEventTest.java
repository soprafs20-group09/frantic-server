package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
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

public class FinishLineEventTest {

    @Mock
    private Game game;
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
        Event finishLine = new FinishLineEvent(this.game, this.gameRound);
        assertEquals("finish-line", finishLine.getName());
    }

    @Test
    public void getMessageTest() {
        Event finishLine = new FinishLineEvent(this.game, this.gameRound);
        assertEquals("Looks like the round is over! Count your points!", finishLine.getMessage());
    }

    @Test
    public void performEventTest() {
        Mockito.doNothing().when(game).endGameRound(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());

        Player player1 = new Player();
        player1.pushCardToHand(new Card(Color.RED, 3, 1));
        Player player2 = new Player();
        player2.pushCardToHand(new Card(Color.RED, 7, 2));
        this.listOfPlayers.add(player1);
        this.listOfPlayers.add(player2);

        Mockito.when(this.gameRound.getCurrentPlayer()).thenReturn(player2);

        Event finishLine = new FinishLineEvent(this.game, this.gameRound);
        finishLine.performEvent();

        assertEquals(3, player1.getPoints());
        assertEquals(7, player2.getPoints());
        Mockito.verify(this.game).endGameRound(Mockito.any(), Mockito.anyMap(), Mockito.any(), Mockito.any());
    }
}