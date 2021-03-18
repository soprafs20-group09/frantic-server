package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.TurnDuration;
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

public class SurprisePartyEventTest {

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
        Mockito.when(this.gameRound.getTurnDuration()).thenReturn(TurnDuration.NORMAL);
    }

    @Test
    void getNameTest() {
        SurprisePartyEvent surpriseParty = new SurprisePartyEvent(this.gameRound);
        assertEquals("surprise-party", surpriseParty.getName());
    }

    @Test
    void performEventTest() {
        Mockito.doNothing().when(gameService).sendEventActionResponse(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(gameService).sendTimer(Mockito.any(), Mockito.anyInt());

        SurprisePartyEvent surpriseParty = new SurprisePartyEvent(this.gameRound);
        surpriseParty.performEvent();

        Mockito.verify(gameService).sendEventActionResponse(Mockito.any(), Mockito.eq("surprise-party"));
    }

    @Test
    void getMessageTest() {
        SurprisePartyEvent surpriseParty = new SurprisePartyEvent(this.gameRound);
        assertEquals("Surprise another player by gifting them one of your cards!", surpriseParty.getMessage());
    }
}