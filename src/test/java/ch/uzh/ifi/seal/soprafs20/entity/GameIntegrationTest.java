package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.*;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Mock
    private GameService gameService;

    private String testLobbyId;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        lobbyRepository.deleteAll();
        playerRepository.deleteAll();

        Player player1 = playerService.createPlayer("id1", "player1");
        this.testLobbyId = lobbyService.createLobby(player1);

        Player player2 = playerService.createPlayer("id2", "player2");
        lobbyService.joinLobby(this.testLobbyId, player2);
    }

    @Test
    void calculateMaxPoints_2Players() {
        Game testGame = new Game(this.testLobbyId, GameLength.SHORT, TurnDuration.NORMAL);
        assertEquals(137, testGame.getMaxPoints());

        testGame = new Game(this.testLobbyId, GameLength.MEDIUM, TurnDuration.NORMAL);
        assertEquals(154, testGame.getMaxPoints());

        testGame = new Game(this.testLobbyId, GameLength.LONG, TurnDuration.NORMAL);
        assertEquals(179, testGame.getMaxPoints());
    }

    @Test
    void calculateMaxPoints_4Players() {
        Player player3 = playerService.createPlayer("id3", "player3");
        lobbyService.joinLobby(this.testLobbyId, player3);
        Player player4 = playerService.createPlayer("id4", "player4");
        lobbyService.joinLobby(this.testLobbyId, player4);
        Game testGame = new Game(this.testLobbyId, GameLength.MEDIUM, TurnDuration.NORMAL);

        assertEquals(154, testGame.getMaxPoints());
    }

    @Test
    void calculateMaxPoints_6Players() {
        Player player3 = playerService.createPlayer("id3", "player3");
        lobbyService.joinLobby(this.testLobbyId, player3);
        Player player4 = playerService.createPlayer("id4", "player4");
        lobbyService.joinLobby(this.testLobbyId, player4);
        Player player5 = playerService.createPlayer("id5", "player5");
        lobbyService.joinLobby(this.testLobbyId, player5);
        Player player6 = playerService.createPlayer("id6", "player6");
        lobbyService.joinLobby(this.testLobbyId, player6);

        Game testGame = new Game(this.testLobbyId, GameLength.SHORT, TurnDuration.NORMAL);
        assertEquals(113, testGame.getMaxPoints());

        testGame = new Game(this.testLobbyId, GameLength.MEDIUM, TurnDuration.NORMAL);
        assertEquals(137, testGame.getMaxPoints());

        testGame = new Game(this.testLobbyId, GameLength.LONG, TurnDuration.NORMAL);
        assertEquals(154, testGame.getMaxPoints());
    }

    @Test
    void endGameRound_notGameOver() {
        List<Player> playerList = new ArrayList<>();
        Player player1 = new Player();
        player1.setUsername("player1");
        player1.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 0));
        player1.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 1));
        player1.setPoints(42);
        playerList.add(player1);
        Player player2 = new Player();
        player2.setUsername("player2");
        player2.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 2));
        player2.setPoints(136);
        playerList.add(player2);
        Player player3 = new Player();
        player3.setUsername("player3");
        player3.setPoints(44);
        playerList.add(player3);

        assertEquals(2, player1.getHandSize());
        assertEquals(1, player2.getHandSize());
        assertEquals(0, player3.getHandSize());

        Game testGame = new Game(this.testLobbyId, GameLength.SHORT, TurnDuration.NORMAL);
        testGame.setListOfPlayers(playerList);
        testGame.setGameService(this.gameService);
        Map<String, Integer> changes = new HashMap<>();
        testGame.endGameRound(player2, changes, "icon", "message:");

        assertEquals(player3, testGame.getFirstPlayer());
        assertEquals(0, player1.getHandSize());
        assertEquals(0, player2.getHandSize());
        assertEquals(0, player3.getHandSize());
        Mockito.verify(this.gameService).sendEndRound(this.testLobbyId, playerList, changes, 137, 20, "icon", "message: Watch everyone's standings and wait for the next round to start!");
    }

    @Test
    void endGameRound_gameOver() {
        List<Player> playerList = new ArrayList<>();
        Player player1 = new Player();
        player1.setUsername("player1");
        player1.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 0));
        player1.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 1));
        player1.setPoints(42);
        playerList.add(player1);
        Player player2 = new Player();
        player2.setUsername("player2");
        player2.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 2));
        player2.setPoints(138);
        playerList.add(player2);
        Player player3 = new Player();
        player3.setUsername("player3");
        player3.setPoints(44);
        playerList.add(player3);

        assertEquals(2, player1.getHandSize());
        assertEquals(1, player2.getHandSize());
        assertEquals(0, player3.getHandSize());

        Game testGame = new Game(this.testLobbyId, GameLength.SHORT, TurnDuration.NORMAL);
        testGame.setListOfPlayers(playerList);
        testGame.setGameService(this.gameService);
        Map<String, Integer> changes = new HashMap<>();
        testGame.endGameRound(player2, changes, "icon", "message:");

        assertEquals(player3, testGame.getFirstPlayer());
        assertEquals(0, player1.getHandSize());
        assertEquals(0, player2.getHandSize());
        assertEquals(0, player3.getHandSize());
        Mockito.verify(this.gameService).sendEndGame(this.testLobbyId, playerList, changes, "icon", "message: The game is over. See who won below and challenge them to a rematch!");
        assertEquals(0, player1.getPoints());
        assertEquals(0, player2.getPoints());
        assertEquals(0, player3.getPoints());
    }

    @Test
    void removeFromPlayerList() {
        List<Player> playerList = new ArrayList<>();
        Player player1 = new Player();
        player1.setIdentity("player1");
        playerList.add(player1);
        Player player2 = new Player();
        player2.setIdentity("player2");
        playerList.add(player2);
        Player player3 = new Player();
        player3.setIdentity("player3");
        playerList.add(player3);
        Game testGame = new Game(this.testLobbyId, GameLength.SHORT, TurnDuration.NORMAL);
        testGame.setListOfPlayers(playerList);

        testGame.playerLostConnection(player1);

        assertEquals(2, playerList.size());
        assertEquals(player2, playerList.get(0));
        assertEquals(player3, playerList.get(1));
    }
}
