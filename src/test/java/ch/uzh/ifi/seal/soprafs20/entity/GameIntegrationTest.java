package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GameIntegrationTest {

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    private String testLobbyId;

    @BeforeEach
    public void setup() {
        lobbyRepository.deleteAll();
        playerRepository.deleteAll();

        Player player1 = playerService.createPlayer("id1", "player1");
        this.testLobbyId = lobbyService.createLobby(player1);

        Player player2 = playerService.createPlayer("id2", "player2");
        lobbyService.joinLobby(this.testLobbyId, player2);
    }

    @Test
    public void calculateMaxPoints_2Players() {
        Game testGame = new Game(this.testLobbyId, GameLength.SHORT);
        assertEquals(137, testGame.getMaxPoints());

        testGame = new Game(this.testLobbyId, GameLength.MEDIUM);
        assertEquals(154, testGame.getMaxPoints());

        testGame = new Game(this.testLobbyId, GameLength.LONG);
        assertEquals(179, testGame.getMaxPoints());
    }

    @Test
    public void calculateMaxPoints_4Players() {
        Player player3 = playerService.createPlayer("id3", "player3");
        lobbyService.joinLobby(this.testLobbyId, player3);
        Player player4 = playerService.createPlayer("id4", "player4");
        lobbyService.joinLobby(this.testLobbyId, player4);
        Game testGame = new Game(this.testLobbyId, GameLength.MEDIUM);

        assertEquals(154, testGame.getMaxPoints());
    }

    @Test
    public void calculateMaxPoints_6Players() {
        Player player3 = playerService.createPlayer("id3", "player3");
        lobbyService.joinLobby(this.testLobbyId, player3);
        Player player4 = playerService.createPlayer("id4", "player4");
        lobbyService.joinLobby(this.testLobbyId, player4);
        Player player5 = playerService.createPlayer("id5", "player5");
        lobbyService.joinLobby(this.testLobbyId, player5);
        Player player6 = playerService.createPlayer("id6", "player6");
        lobbyService.joinLobby(this.testLobbyId, player6);

        Game testGame = new Game(this.testLobbyId, GameLength.SHORT);
        assertEquals(113, testGame.getMaxPoints());

        testGame = new Game(this.testLobbyId, GameLength.MEDIUM);
        assertEquals(137, testGame.getMaxPoints());

        testGame = new Game(this.testLobbyId, GameLength.LONG);
        assertEquals(154, testGame.getMaxPoints());
    }
}
