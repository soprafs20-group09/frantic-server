package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.cards.NumberCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Mock
    private Player testPlayer;

    private Card someCard;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Mockito.when(testPlayer.getId()).thenReturn(1L);
        Mockito.when(testPlayer.getUsername()).thenReturn("testPlayer");
        Mockito.when(testPlayer.getIdentity()).thenReturn("FANCY_IDENTITY_FOR_TEST");
        Mockito.when(testPlayer.getLobbyId()).thenReturn("I like bread");
        Mockito.when(testPlayer.getPoints()).thenReturn(12);
        Mockito.when(testPlayer.popCard(0)).thenReturn(someCard);
        Mockito.when(testPlayer.isBlocked()).thenReturn(false);
        Mockito.when(testPlayer.isAdmin()).thenReturn(false);
        Mockito.when(testPlayer.getHandSize()).thenReturn(1);
        Mockito.when(testPlayer.calculatePoints()).thenReturn(1);
    }

    @Test
    public void getIdTest() {
        assertEquals(1L, testPlayer.getId());
    }

    @Test
    public void setIdTest() {
        Mockito.when(testPlayer.getId()).thenReturn(2L);
        testPlayer.setId(2L);
        assertEquals(2L, testPlayer.getId());
    }

    @Test
    public void getUsernameTest() {
        assertEquals("testPlayer", testPlayer.getUsername());
    }

    @Test
    public void setUsernameTest() {
        Mockito.when(testPlayer.getUsername()).thenReturn("SuperDuckling");
        testPlayer.setUsername("SuperDuckling");
        assertEquals("SuperDuckling", testPlayer.getUsername());
    }

    @Test
    public void getIdentityTest() {
        assertEquals("FANCY_IDENTITY_FOR_TEST", testPlayer.getIdentity());
    }

    @Test
    public void setIdentityTest() {
        Mockito.when(testPlayer.getIdentity()).thenReturn("SUPER_SECRET_IDENTITY");
        testPlayer.setIdentity("SUPER_SECRET_IDENTITY");
        assertEquals("SUPER_SECRET_IDENTITY", testPlayer.getIdentity());
    }

    @Test
    public void getLobbyIdTest() {
        assertEquals("I like bread", testPlayer.getLobbyId());
    }

    @Test
    public void setLobbyIdTest() {
        Mockito.when(testPlayer.getLobbyId()).thenReturn("Pros only");
        testPlayer.setLobbyId("Pros only");
        assertEquals("Pros only", testPlayer.getLobbyId());
    }

    @Test
    public void getPointsTest() {
        assertEquals(12, testPlayer.getPoints());
    }

    @Test
    public void setPointsTest() {
        Mockito.when(testPlayer.getPoints()).thenReturn(15);
        testPlayer.setPoints(15);
        assertEquals(15, testPlayer.getPoints());
    }

    @Test
    public void popCardTest() {
        Mockito.when(testPlayer.getHandSize()).thenReturn(0);
        assertEquals(someCard, testPlayer.popCard(0));
        assertEquals(0, testPlayer.getHandSize());
    }

    @Test
    public void pushCardToHandTest() {
        testPlayer.pushCardToHand(someCard);
        testPlayer.pushCardToHand(someCard);
        assertEquals(someCard, testPlayer.popCard(0));
        assertEquals(1, testPlayer.getHandSize());
    }

    @Test
    public void isBlockedTest() {
        assertFalse(testPlayer.isBlocked());
    }

    @Test
    public void setBlockedTest() {
        Mockito.when(testPlayer.isBlocked()).thenReturn(true);
        testPlayer.setBlocked(true);
        assertTrue(testPlayer.isBlocked());
    }

    @Test
    public void getHandSizeTest() {
        assertEquals(1, testPlayer.getHandSize());
    }

    @Test
    public void isAdminTest() {
        assertFalse(testPlayer.isAdmin());
    }

    @Test
    public void setAdminTest() {
        Mockito.when(testPlayer.isAdmin()).thenReturn(true);
        testPlayer.setAdmin(true);
        assertTrue(testPlayer.isAdmin());
    }

    //TODO: hasNiceTryTest() & hasCounterAttackTest()

    @Test
    public void calculatePointsTest() {
        //TODO: test with special cards & FuckYouCard
        assertEquals(1, testPlayer.calculatePoints());
    }

    @Test
    public void clearHandTest() {
        Mockito.when(testPlayer.getHandSize()).thenReturn(5);
        testPlayer.pushCardToHand(someCard);
        testPlayer.pushCardToHand(someCard);
        testPlayer.pushCardToHand(someCard);
        testPlayer.pushCardToHand(someCard);
        assertEquals(5, testPlayer.getHandSize());
        testPlayer.clearHand();
        Mockito.when(testPlayer.getHandSize()).thenReturn(0);
        assertEquals(0, testPlayer.getHandSize());
    }
}
