package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.GameLength;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ThirdTimeLuckyEventTest {

    private List<Player> listOfPlayers = new ArrayList<>();
    private Pile<Card> drawStack = new DrawStack();

    @Test
    public void getNameTest() {
        Event thirdTimeLucky = new ThirdTimeLuckyEvent(listOfPlayers, drawStack);
        assertEquals("third-time-lucky", thirdTimeLucky.getName());
    }

    @Test
    public void performEventTest() {
        Player player1 = new Player();
        Player player2 = new Player();
        player1.pushCardToHand(new Card(Color.RED, 7, 1));
        listOfPlayers.add(player1);
        listOfPlayers.add(player2);

        assertEquals(1, player1.getHandSize());
        assertEquals(0, player2.getHandSize());

        Event thirdTimeLucky = new ThirdTimeLuckyEvent(listOfPlayers, drawStack);
        thirdTimeLucky.performEvent();

        assertEquals(4, player1.getHandSize());
        assertEquals(3, player2.getHandSize());
    }

    @Test
    public void getMessageTest() {
        Event thirdTimeLucky = new ThirdTimeLuckyEvent(listOfPlayers, drawStack);
        assertEquals("Three cards for everyone!", thirdTimeLucky.getMessage());
    }
}