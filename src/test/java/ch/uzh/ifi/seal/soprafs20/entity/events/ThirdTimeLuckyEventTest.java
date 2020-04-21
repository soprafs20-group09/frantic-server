package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;
import ch.uzh.ifi.seal.soprafs20.entity.DrawStack;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ThirdTimeLuckyEventTest {

    private Event thirdTimeLucky;
    private Player p1;
    private Player p2;
    private Game game;


    /*@Mock
    PlayerService playerService;


    public ThirdTimeLuckyEventTest() {
        ArrayList<Player> playerList  = new ArrayList<>();
        p1 = new Player();
        p2 = new Player();
        playerList.add(p1);
        playerList.add(p2);

        when(PlayerService.getInstance()).thenReturn(playerService);
        when(playerService.getPlayersInLobby("a")).thenReturn(playerList);

        game = new Game("a", GameLength.SHORT);
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(game, "listOfPlayers", playerList);

        GameRound gameRound = new GameRound(game, "a", playerList, p1);
        DrawStack drawStack = new DrawStack();
        Event thirdTimeLucky = new ThirdTimeLuckyEvent(gameRound, drawStack);
    }

    @Test
    public void getNameTest() {
        assertEquals("third-time-lucky", thirdTimeLucky.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("Three cards for everyone!", thirdTimeLucky.getMessage());
    }*/

    /*@Test
    public void performTest() {
         this.thirdTimeLucky.performEvent();

         assertEquals(3, p1.getHandSize());
         assertEquals(3, p2.getHandSize());
    }*/


}