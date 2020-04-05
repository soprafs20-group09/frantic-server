package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LobbyTest {

    @Test
    public void addPlayer_increaseSize() {
        Lobby lobby = new Lobby();
        Player testPlayer1 = new Player();
        testPlayer1.setUsername("testPlayer1");
        Player testPlayer2 = new Player();
        testPlayer1.setUsername("testPlayer2");

        lobby.addPlayer(testPlayer1);
        lobby.addPlayer(testPlayer2);
        assertEquals(lobby.getPlayers(), 2);
    }

    @Test
    public void removePlayer_decreaseSize() {
        Lobby lobby = new Lobby();
        Player testPlayer1 = new Player();
        testPlayer1.setUsername("testPlayer1");
        Player testPlayer2 = new Player();
        testPlayer1.setUsername("testPlayer2");

        lobby.addPlayer(testPlayer1);
        lobby.addPlayer(testPlayer2);
        assertEquals(lobby.getPlayers(), 2);
        lobby.removePlayer(testPlayer2);
        assertEquals(lobby.getPlayers(), 1);
    }

    @Test
    public void createLobby_baseSettings() {
        Lobby lobby = new Lobby();
        assertNotNull(lobby.getLobbyId());
        assertEquals(lobby.getGameDuration(), GameLength.MEDIUM);
        assertTrue(lobby.isPublic());
        assertEquals(lobby.getListOfPlayers().size(), 0);
        assertFalse(lobby.isPlaying());
    }

    @Test
    public void addPlayers_getListOfPlayers() {
        Lobby lobby = new Lobby();
        Player testPlayer1 = new Player();
        testPlayer1.setUsername("testPlayer1");
        Player testPlayer2 = new Player();
        testPlayer1.setUsername("testPlayer2");

        lobby.addPlayer(testPlayer1);
        lobby.addPlayer(testPlayer2);

        assertEquals(lobby.getListOfPlayers().size(), 2);
        assertEquals(lobby.getListOfPlayers().get(0), testPlayer1.getUsername());
        assertEquals(lobby.getListOfPlayers().get(1), testPlayer2.getUsername());
    }
}
