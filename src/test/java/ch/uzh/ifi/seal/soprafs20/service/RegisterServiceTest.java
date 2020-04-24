package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyJoinDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.RegisterDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.LobbyStateDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.RegisteredDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegisterServiceTest {

    @InjectMocks
    private Player testPlayer;
    @Spy
    @InjectMocks
    private RegisterService registerService;
    @Mock
    private LobbyService lobbyService;
    @Mock
    private WebSocketService webSocketService;
    @Mock
    private PlayerService playerService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void prepareLobby_returnDTO() {
        LobbyJoinDTO lobbyJoinDTO = new LobbyJoinDTO();
        lobbyJoinDTO.setName("testPlayer's lobby");
        lobbyJoinDTO.setUsername("testPlayer");

        LobbyJoinDTO response = registerService.prepareLobby("testPlayer");

        assertNotNull(response.getToken());
        assertEquals(lobbyJoinDTO.getName(), response.getName());
        assertEquals(lobbyJoinDTO.getUsername(), response.getUsername());
    }

    @Test
    public void joinLobbyTest() throws InterruptedException {
        RegisteredDTO registeredDTO = new RegisteredDTO();
        registeredDTO.setLobbyId("testLobbyId");
        registeredDTO.setUsername("testPlayer");

        testPlayer = new Player();
        testPlayer.setUsername("testPlayer");
        testPlayer.setAdmin(false);
        testPlayer.setIdentity("testIdentity");

        Mockito.when(playerService.createPlayer(Mockito.any(), Mockito.any())).thenReturn(testPlayer);
        Mockito.doNothing().when(lobbyService).joinLobby(Mockito.any(), Mockito.any());
        Mockito.when(playerService.registerPlayer(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(registeredDTO);
        Mockito.when(lobbyService.getLobbyState(Mockito.any())).thenReturn(new LobbyStateDTO());
        Mockito.doReturn("testPlayer").when(registerService).getUsernameFromAuthToken(Mockito.anyString());
        Mockito.doNothing().when(registerService).removeFromAuthMap(Mockito.anyString());

        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setToken("abc");

        registerService.joinLobby("testIdentity", registerDTO);

        Mockito.verify(webSocketService).sendToPlayer(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(webSocketService).sendToLobby(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(webSocketService).sendChatMessage(Mockito.any(), (Chat) Mockito.any());
    }
}