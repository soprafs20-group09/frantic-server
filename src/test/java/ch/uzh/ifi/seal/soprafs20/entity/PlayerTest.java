package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
    void setPointsTest() {
        assertEquals(12, testPlayer.getPoints());
        testPlayer.setPoints(15);
        assertEquals(15, testPlayer.getPoints());
    }

    @Test
    void popCardTest() {
        assertEquals(someCard, testPlayer.popCard(0));
        assertEquals(0, testPlayer.getHandSize());
    }

    @Test
    void pushCardToHandTest() {
        assertEquals(1, testPlayer.getHandSize());
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 1));
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 2));
        assertEquals(3, testPlayer.getHandSize());
    }

    @Test
    void isBlockedTest() {
        assertFalse(testPlayer.isBlocked());
    }

    @Test
    void setBlockedTest() {
        assertFalse(testPlayer.isBlocked());
        testPlayer.setBlocked(true);
        assertTrue(testPlayer.isBlocked());
    }

    @Test
    void getHandSizeTest() {
        assertEquals(1, testPlayer.getHandSize());
    }

    @Test
    void isAdminTest() {
        assertFalse(testPlayer.isAdmin());
    }

    @Test
    void setAdminTest() {
        assertFalse(testPlayer.isAdmin());
        testPlayer.setAdmin(true);
        assertTrue(testPlayer.isAdmin());
    }

    @Test
    public void hasNiceTryTest() {
        Player testPlayer2 = new Player();
        Card niceTry = new Card(Color.MULTICOLOR, Type.SPECIAL, Value.NICETRY, false, 10);
        Card someCard2 = new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTIC, false, 12);
        testPlayer2.pushCardToHand(someCard);
        testPlayer2.pushCardToHand(niceTry);
        testPlayer2.pushCardToHand(someCard2);

        int[] result = {};
        int[] result2 = new int[]{1};

        assertArrayEquals(result, testPlayer.hasNiceTry());
        assertArrayEquals(result2, testPlayer2.hasNiceTry());
    }

    @Test
    public void hasCounterAttackTest() {
        Player testPlayer2 = new Player();
        Card counterAttack = new Card(Color.MULTICOLOR, Type.SPECIAL, Value.COUNTERATTACK, true, 11);
        Card someCard2 = new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTIC, false, 12);
        testPlayer2.pushCardToHand(someCard);
        testPlayer2.pushCardToHand(counterAttack);
        testPlayer2.pushCardToHand(someCard2);

        int[] result = {};
        int[] result2 = new int[]{1};

        assertArrayEquals(result, testPlayer.hasCounterAttack());
        assertArrayEquals(result2, testPlayer2.hasCounterAttack());
    }

    @Test
    void calculatePointsTest() {
        assertEquals(7, testPlayer.calculatePoints());

        testPlayer.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.ONE, false, 1));
        assertEquals(8, testPlayer.calculatePoints());

        testPlayer.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.FIVE, false, 2));
        assertEquals(13, testPlayer.calculatePoints());

        testPlayer.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.NINE, false, 3));
        assertEquals(22, testPlayer.calculatePoints());

        testPlayer.pushCardToHand(new Card(Color.BLACK, Type.NUMBER, Value.FOUR, false, 4));
        assertEquals(26, testPlayer.calculatePoints());

        testPlayer.pushCardToHand(new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTICFOUR, true, 5));
        assertEquals(33, testPlayer.calculatePoints());

        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.EXCHANGE, true, 6));
        assertEquals(40, testPlayer.calculatePoints());

        testPlayer.pushCardToHand(new Card(Color.BLACK, Type.SPECIAL, Value.FUCKYOU, false, 7));
        assertEquals(82, testPlayer.calculatePoints());
    }

    @Test
    void clearHandTest() {
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 1));
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 2));
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 3));
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 4));
        assertEquals(5, testPlayer.getHandSize());
        testPlayer.clearHand();
        assertEquals(0, testPlayer.getHandSize());
    }

    @Test
    void getPlayableCards_FuckYou() {
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.ONE, false, 1));
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.TWO, false, 2));
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.THREE, false, 3));
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.FOUR, false, 4));
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.ONE, false, 6));
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.TWO, false, 7));
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.THREE, false, 8));
        testPlayer.pushCardToHand(new Card(Color.BLACK, Type.SPECIAL, Value.FUCKYOU, false, 9));

        //player does not have 10 cards
        Card reference = new Card(Color.YELLOW, Type.NUMBER, Value.NINE, false, 11);
        assertEquals(0, testPlayer.getPlayableCards(reference).length);

        //player has 10 cards
        testPlayer.pushCardToHand(new Card(Color.GREEN, Type.NUMBER, Value.FOUR, false, 10));
        assertEquals(1, testPlayer.getPlayableCards(reference).length);
    }
}
