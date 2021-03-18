package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.TurnDuration;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
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

public class MarketEventTest {

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
        MarketEvent market = new MarketEvent(this.gameRound);
        assertEquals("market", market.getName());
    }

    @Test
    void performEventTest() {
        Mockito.doNothing().when(gameService).sendMarketWindow(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(gameService).sendTimer(Mockito.any(), Mockito.anyInt());

        Player player1 = new Player();
        player1.pushCardToHand(new Card(Color.RED, 7, 11));
        player1.pushCardToHand(new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTIC, false, 12));
        player1.pushCardToHand(new Card(Color.RED, Type.SPECIAL, Value.SKIP, true, 13));
        this.listOfPlayers.add(player1);
        Mockito.when(this.gameRound.getCurrentPlayer()).thenReturn(player1);

        Player player2 = new Player();
        player1.pushCardToHand(new Card(Color.RED, 7, 11));
        player1.pushCardToHand(new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTIC, false, 12));
        player1.pushCardToHand(new Card(Color.RED, Type.SPECIAL, Value.SKIP, true, 13));
        this.listOfPlayers.add(player2);

        DrawStack drawStack = new DrawStack();
        Mockito.when(this.gameRound.getDrawStack()).thenReturn(drawStack);

        Event market = new MarketEvent(this.gameRound);
        market.performEvent();

        Mockito.verify(gameService).sendMarketWindow(Mockito.any(), Mockito.eq(player2), Mockito.any(), Mockito.any());
    }

    @Test
    void getMessageTest() {
        MarketEvent market = new MarketEvent(this.gameRound);
        assertEquals("Choose one of these incredibly awesome cards for the great price of only 0$!", market.getMessage());
    }
}