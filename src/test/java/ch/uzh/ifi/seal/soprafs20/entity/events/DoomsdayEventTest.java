package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoomsdayEventTest {

    private List<Player> listOfPlayers = new ArrayList<>();
    private Player player1 = new Player();
    private Player player2 = new Player();

    @Mock
    private Game game;

    @Test
    public void getNameTest() {
        Event doomsday = new DoomsdayEvent(game, listOfPlayers, player1);
        assertEquals("doomsday", doomsday.getName());
    }

    @Test
    public void performEventTest() {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(game).endGameRound(Mockito.any());

        player1.setPoints(20);
        listOfPlayers.add(player1);
        listOfPlayers.add(player2);

        Event doomsday = new DoomsdayEvent(game, listOfPlayers, player1);
        doomsday.performEvent();
        assertEquals(70, player1.getPoints());
        assertEquals(50, player2.getPoints());
        Mockito.verify(game).endGameRound(Mockito.any());
    }

    @Test
    public void getMessageTest() {
        Event doomsday = new DoomsdayEvent(game, listOfPlayers, player1);
        assertEquals("Rest in peace, everyone is dead. Well, not really, since it's just a game. The round is over and everyone's points increase by 50.", doomsday.getMessage());
    }
}