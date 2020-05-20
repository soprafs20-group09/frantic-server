package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MexicanStandoffEventTest {

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
        Event mexicanStandoff = new MexicanStandoffEvent(this.gameRound);
        assertEquals("mexican-standoff", mexicanStandoff.getName());
    }

    @Test
    void getMessageTest() {
        Event mexicanStandoff = new MexicanStandoffEvent(this.gameRound);
        assertEquals("Show your skills off! Everyone gets rid of their cards and gets three new ones! Who can finish first?", mexicanStandoff.getMessage());
    }

    @Test
    void performEventTest() {
        Player player1 = new Player();
        player1.pushCardToHand(new Card(Color.RED, 7, 11));
        player1.pushCardToHand(new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTIC, false, 12));
        player1.pushCardToHand(new Card(Color.BLUE, Type.SPECIAL, Value.SKIP, true, 13));
        player1.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.SECONDCHANCE, true, 14));
        this.listOfPlayers.add(player1);

        assertEquals(4, player1.getHandSize());

        Mockito.when(this.gameRound.getCurrentPlayer()).thenReturn(player1);
        Event mexicanStandoff = new MexicanStandoffEvent(this.gameRound);
        mexicanStandoff.performEvent();

        assertEquals(0, player1.getHandSize());
        Mockito.verify(this.gameRound, Mockito.times(1)).drawCardFromStack(player1, 3);
    }
}