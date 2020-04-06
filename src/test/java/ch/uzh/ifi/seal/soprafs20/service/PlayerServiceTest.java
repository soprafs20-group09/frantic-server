package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.RegisteredDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private Player testPlayer;

    @InjectMocks
    private PlayerService playerService;

    @InjectMocks
    private Lobby testLobby;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testPlayer.setUsername("testPlayer");
        testPlayer.setIdentity("ABCD-1234-EFGH-5678");
        testPlayer.setLobbyId("A3G45F3S");

        testLobby.setLobbyId("A3G45F3S");
        testLobby.setName("testLobby");
        testLobby.setCreator("Brian");
        testLobby.addPlayer(testPlayer);

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(testPlayer.getUsername()).thenReturn("testPlayer");
        Mockito.when(testPlayer.getIdentity()).thenReturn("ABCD-1234-EFGH-5678");
        Mockito.when(testPlayer.getLobbyId()).thenReturn("A3G45F3S");
    }

    @Test
    public void createPlayer_returnPlayer() {
        Player foundPlayer = playerService.createPlayer(testPlayer.getIdentity(), testPlayer.getUsername());

        assertEquals(testPlayer.getUsername(), foundPlayer.getUsername());
        assertEquals(testPlayer.getIdentity(), foundPlayer.getIdentity());
    }

    @Test
    public void registerPlayer_returnRegisteredDTO() {
        RegisteredDTO returnedRegisteredDTO = playerService.registerPlayer(testPlayer.getIdentity(), testPlayer, testPlayer.getLobbyId());

        assertEquals(testPlayer.getUsername(), returnedRegisteredDTO.getUsername());
        assertEquals(testPlayer.getLobbyId(), returnedRegisteredDTO.getLobbyId());
    }

    @Test
    public void removePlayer_returnString() {
        Mockito.when(lobbyRepository.findByLobbyId(testPlayer.getLobbyId())).thenReturn(testLobby);

        String response = playerService.removePlayer(testPlayer);

        assertNotNull(response);
        assertEquals(testPlayer.getLobbyId(), response);
    }
}