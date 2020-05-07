package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
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

public class DoomsdayEventTest {

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
        Event doomsday = new DoomsdayEvent(this.game, this.gameRound);
        assertEquals("doomsday", doomsday.getName());
    }

    @Test
    public void getMessageTest() {
        Event doomsday = new DoomsdayEvent(this.game, this.gameRound);
        assertEquals("Rest in peace, everyone is dead. Well, not really, since it's just a game. The round is over and everyone's points increase by 50.", doomsday.getMessage());
    }

    @Test
    public void performEventTest() {
        Mockito.doNothing().when(game).endGameRound(Mockito.any());

        Player player1 = new Player();
        player1.setPoints(20);
        this.listOfPlayers.add(player1);
        Player player2 = new Player();
        this.listOfPlayers.add(player2);

        Mockito.when(this.gameRound.getCurrentPlayer()).thenReturn(player1);

        Event doomsday = new DoomsdayEvent(this.game, this.gameRound);

        doomsday.performEvent();
        assertEquals(70, player1.getPoints());
        assertEquals(50, player2.getPoints());
        Mockito.verify(game).endGameRound(Mockito.any());
    }
}