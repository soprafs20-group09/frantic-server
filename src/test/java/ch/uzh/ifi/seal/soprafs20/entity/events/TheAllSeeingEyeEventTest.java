package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TheAllSeeingEyeEventTest {

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
    }

    @Test
    void getMessageTest() {
        TheAllSeeingEyeEvent theAllSeeingEye = new TheAllSeeingEyeEvent(this.gameRound);
        assertEquals("You can't run! You can't hide! The all-seeing eye is here! Take a good look at everyone's cards!", theAllSeeingEye.getMessage());
    }

    @Test
    void performEventTest() {
        Mockito.doNothing().when(gameService).sendGameState(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyBoolean());
        Mockito.doNothing().when(gameService).sendTimer(Mockito.any(), Mockito.anyInt());

        Player player1 = new Player();
        this.listOfPlayers.add(player1);

        TheAllSeeingEyeEvent theAllSeeingEye = new TheAllSeeingEyeEvent(this.gameRound);
        theAllSeeingEye.performEvent();

        Mockito.verify(gameService).sendTimer(Mockito.any(), Mockito.anyInt());
    }

    @Test
    void getNameTest() {
        TheAllSeeingEyeEvent theAllSeeingEye = new TheAllSeeingEyeEvent(this.gameRound);
        assertEquals("the-all-seeing-eye", theAllSeeingEye.getName());
    }
}