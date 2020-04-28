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

    private List<Player> listOfPlayers = new ArrayList<>();
    private Player player1 = new Player();
    private Player player2 = new Player();
    private Event finishLine;

    @Mock
    private Game game;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        player1.pushCardToHand(new Card(Color.RED, 3, 1));
        player2.pushCardToHand(new Card(Color.RED, 7, 2));
        listOfPlayers.add(player1);
        listOfPlayers.add(player2);

        finishLine = new FinishLineEvent(game, listOfPlayers);
    }

    @Test
    public void getNameTest() {
        assertEquals("finish-line", finishLine.getName());
    }

    @Test
    public void performEventTest() {
        Mockito.doNothing().when(game).endGameRound(Mockito.any());

        finishLine.performEvent();
        assertEquals(3, player1.getPoints());
        assertEquals(7, player2.getPoints());
        Mockito.verify(game).endGameRound(Mockito.any());
    }

    @Test
    public void getMessageTest() {
        assertEquals("Looks like the round is over! Count your points!", finishLine.getMessage());
    }
}