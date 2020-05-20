package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.GameLength;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ThirdTimeLuckyEventTest {

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
        Event thirdTimeLucky = new ThirdTimeLuckyEvent(this.gameRound);
        assertEquals("third-time-lucky", thirdTimeLucky.getName());
    }

    @Test
    void getMessageTest() {
        Event thirdTimeLucky = new ThirdTimeLuckyEvent(this.gameRound);
        assertEquals("Three cards for everyone!", thirdTimeLucky.getMessage());
    }

    @Test
    void performEventTest() {
        Player player1 = new Player();
        Player player2 = new Player();
        player1.pushCardToHand(new Card(Color.RED, 7, 1));
        this.listOfPlayers.add(player1);
        this.listOfPlayers.add(player2);

        assertEquals(1, player1.getHandSize());
        assertEquals(0, player2.getHandSize());


        Mockito.when(this.gameRound.getCurrentPlayer()).thenReturn(player1);
        Event thirdTimeLucky = new ThirdTimeLuckyEvent(this.gameRound);
        thirdTimeLucky.performEvent();

        Mockito.verify(this.gameRound, Mockito.times(1)).drawCardFromStack(player1, 3);
        Mockito.verify(this.gameRound, Mockito.times(1)).drawCardFromStack(player2, 3);
    }
}