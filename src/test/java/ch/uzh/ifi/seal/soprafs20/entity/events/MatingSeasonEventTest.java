package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
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

public class MatingSeasonEventTest {

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
        MatingSeasonEvent matingSeason = new MatingSeasonEvent(this.gameRound);
        assertEquals("mating-season", matingSeason.getName());
    }

    @Test
    void getMessageTest() {
        MatingSeasonEvent matingSeason = new MatingSeasonEvent(this.gameRound);
        assertEquals("It's valentines day! Well, at least for your cards! Discard numeral pairs, triples and so on.", matingSeason.getMessage());
    }

    @Test
    void performEventTest() {
        Player player1 = new Player();
        player1.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 0));
        player1.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.NINE, false, 1));
        player1.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.TWO, false, 2));
        player1.pushCardToHand(new Card(Color.RED, Type.NUMBER, Value.EIGHT, false, 3));
        this.listOfPlayers.add(player1);

        Player player2 = new Player();
        player2.pushCardToHand(new Card(Color.YELLOW, Type.NUMBER, Value.ONE, false, 4));
        player2.pushCardToHand(new Card(Color.YELLOW, Type.NUMBER, Value.ONE, false, 5));
        this.listOfPlayers.add(player2);

        Player player3 = new Player();
        player3.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, false, 6));
        player3.pushCardToHand(new Card(Color.BLACK, Type.NUMBER, Value.SEVEN, false, 7));
        player3.pushCardToHand(new Card(Color.BLUE, Type.SPECIAL, Value.GIFT, false, 8));
        this.listOfPlayers.add(player3);

        Player player4 = new Player();
        player4.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.FIVE, false, 9));
        player4.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.FIVE, false, 10));
        player4.pushCardToHand(new Card(Color.YELLOW, Type.NUMBER, Value.FIVE, false, 11));
        player4.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.EIGHT, false, 12));
        player4.pushCardToHand(new Card(Color.RED, Type.NUMBER, Value.EIGHT, false, 13));
        player4.pushCardToHand(new Card(Color.BLACK, Type.SPECIAL, Value.FUCKYOU, false, 14));
        this.listOfPlayers.add(player4);

        Mockito.when(this.gameRound.getCurrentPlayer()).thenReturn(player2);

        MatingSeasonEvent m = new MatingSeasonEvent(this.gameRound);
        m.performEvent();

        assertEquals(2, player1.getHandSize());
        assertEquals(0, player2.getHandSize());
        assertEquals(3, player3.getHandSize());
        assertEquals(1, player4.getHandSize());
    }
}