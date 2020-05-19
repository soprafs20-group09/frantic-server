package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player testPlayer;

    private Card someCard;

    @BeforeEach
    void setup() {
        testPlayer = new Player();
        someCard = new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 0);

        testPlayer.setId(1L);
        testPlayer.setUsername("testPlayer");
        testPlayer.setIdentity("FANCY_IDENTITY_FOR_TEST");
        testPlayer.setLobbyId("I like bread");
        testPlayer.setPoints(12);
        testPlayer.setBlocked(false);
        testPlayer.setAdmin(false);
        testPlayer.pushCardToHand(someCard);
    }

    @Test
    void getIdTest() {
        assertEquals(1L, testPlayer.getId());
    }

    @Test
    void setIdTest() {
        assertEquals(1L, testPlayer.getId());
        testPlayer.setId(2L);
        assertEquals(2L, testPlayer.getId());
    }

    @Test
    void getUsernameTest() {
        assertEquals("testPlayer", testPlayer.getUsername());
    }

    @Test
    void setUsernameTest() {
        assertEquals("testPlayer", testPlayer.getUsername());
        testPlayer.setUsername("SuperDuckling");
        assertEquals("SuperDuckling", testPlayer.getUsername());
    }

    @Test
    void getIdentityTest() {
        assertEquals("FANCY_IDENTITY_FOR_TEST", testPlayer.getIdentity());
    }

    @Test
    void setIdentityTest() {
        assertEquals("FANCY_IDENTITY_FOR_TEST", testPlayer.getIdentity());
        testPlayer.setIdentity("SUPER_SECRET_IDENTITY");
        assertEquals("SUPER_SECRET_IDENTITY", testPlayer.getIdentity());
    }

    @Test
    void getLobbyIdTest() {
        assertEquals("I like bread", testPlayer.getLobbyId());
    }

    @Test
    void setLobbyIdTest() {
        assertEquals("I like bread", testPlayer.getLobbyId());
        testPlayer.setLobbyId("Pros only");
        assertEquals("Pros only", testPlayer.getLobbyId());
    }

    @Test
    void getPointsTest() {
        assertEquals(12, testPlayer.getPoints());
    }

    @Test
    public void setPointsTest() {
        assertEquals(12, testPlayer.getPoints());
        testPlayer.setPoints(15);
        assertEquals(15, testPlayer.getPoints());
    }

    @Test
    public void popCardTest() {
        assertEquals(someCard, testPlayer.popCard(0));
        assertEquals(0, testPlayer.getHandSize());
    }

    @Test
    public void pushCardToHandTest() {
        assertEquals(1, testPlayer.getHandSize());
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 1));
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 2));
        assertEquals(3, testPlayer.getHandSize());
    }

    @Test
    public void isBlockedTest() {
        assertFalse(testPlayer.isBlocked());
    }

    @Test
    public void setBlockedTest() {
        assertFalse(testPlayer.isBlocked());
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
        assertFalse(testPlayer.isAdmin());
        testPlayer.setAdmin(true);
        assertTrue(testPlayer.isAdmin());
    }

    //TODO: hasNiceTryTest() & hasCounterAttackTest()

    @Test
    public void calculatePointsTest() {
        //TODO: test with special cards & FuckYouCard
        assertEquals(7, testPlayer.calculatePoints());
    }

    @Test
    public void clearHandTest() {
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 1));
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 2));
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 3));
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 4));
        assertEquals(5, testPlayer.getHandSize());
        testPlayer.clearHand();
        assertEquals(0, testPlayer.getHandSize());
    }
}
