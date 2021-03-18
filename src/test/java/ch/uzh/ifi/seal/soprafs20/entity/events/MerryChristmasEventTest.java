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

public class MerryChristmasEventTest {

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
    void getNameTest() {
        MerryChristmasEvent merryChristmas = new MerryChristmasEvent(this.gameRound);
        assertEquals("merry-christmas", merryChristmas.getName());
    }

    @Test
    void performEventTest() {
        Mockito.doNothing().when(gameService).sendEventActionResponse(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(gameService).sendTimer(Mockito.any(), Mockito.anyInt());

        MerryChristmasEvent merryChristmas = new MerryChristmasEvent(this.gameRound);
        merryChristmas.performEvent();

        Mockito.verify(gameService).sendEventActionResponse(Mockito.any(), Mockito.eq("merry-christmas"));
    }

    @Test
    void getMessageTest() {
        MerryChristmasEvent merryChristmas = new MerryChristmasEvent(this.gameRound);
        assertEquals("Merry christmas everyone! It's that time of the year again. Give presents to your loved ones!", merryChristmas.getMessage());
    }
}