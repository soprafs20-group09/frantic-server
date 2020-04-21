package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeBombEventTest {
    private String lobbyId = "bla";
    private Player player1 = new Player();
    private List<Player> listOfPlayers = new ArrayList<>();
    private Game game;
    private GameRound gameRound;
    private Event timeBomb;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        listOfPlayers.add(player1);

        Mockito.when(playerService.getPlayersInLobby(lobbyId)).thenReturn(listOfPlayers);
        Mockito.when(playerRepository.findByLobbyId(lobbyId)).thenReturn(listOfPlayers);

        this.game = new Game(lobbyId, GameLength.MEDIUM);
        this.gameRound = new GameRound(game, lobbyId, listOfPlayers, player1);
        this.timeBomb = new TimeBombEvent(gameRound);
    }

    @Test
    public void getNameTest() {
        assertEquals("time-bomb", timeBomb.getName());
    }

    @Test
    public void getMessageTest() {
        assertEquals("Tick ... Tick ... Tick ... Boom! Everyone has three turns left! Defuse the Bomb and earn a reward by winning the round or let the Bomb explode and everyone's points in this round get doubled!", timeBomb.getMessage());
    }

    @Test
    public void performEventTest() {

    }
}