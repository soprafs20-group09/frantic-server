package ch.uzh.ifi.seal.soprafs20.entity.events;

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

public class FridayTheThirteenthEventTest {

    @Mock
    private Game game;
    @Mock
    private GameRound gameRound;
    @Mock
    private GameService gameService;

    private List<Player> listOfPlayers = new ArrayList<>();

    @BeforeEach
    void setup() {
        this.listOfPlayers = new ArrayList<>();

        MockitoAnnotations.initMocks(this);
        Mockito.when(this.gameRound.getGameService()).thenReturn(this.gameService);
        Mockito.when(this.gameRound.getListOfPlayers()).thenReturn(this.listOfPlayers);
    }

    @Test
    void getNameTest() {
        FridayTheThirteenthEvent fridayTheThirteenth = new FridayTheThirteenthEvent(this.gameRound);
        assertEquals("friday-the-13th", fridayTheThirteenth.getName());
    }

    @Test
    void getMessageTest() {
        FridayTheThirteenthEvent fridayTheThirteenth = new FridayTheThirteenthEvent(this.gameRound);
        assertEquals("It’s Friday, the Thirteenth. A hook-handed murderer is among us! But just in the movies, it’s a totally boring, normal Friday, nothing weird happens. The game round continues without further ado.", fridayTheThirteenth.getMessage());
    }
}