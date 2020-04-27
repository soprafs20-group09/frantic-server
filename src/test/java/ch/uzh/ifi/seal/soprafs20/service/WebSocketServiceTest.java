package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.ChatDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

public class WebSocketServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;
    @Mock
    private PlayerRepository playerRepository;
    @InjectMocks
    private Player testPlayer;
    @Spy
    @InjectMocks
    private WebSocketService webSocketService;
    @InjectMocks
    private Lobby testLobby;
    @InjectMocks
    private ChatDTO chat;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testPlayer = new Player();
        testPlayer.setUsername("testPlayer");
        testPlayer.setAdmin(false);
        testPlayer.setIdentity("testIdentity");

        testLobby = new Lobby();
        testLobby.setName("testLobby");
        testLobby.setCreator("Brian");
        testLobby.addPlayer(testPlayer);
        testLobby.addPlayer(testPlayer);

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.doReturn(true).when(webSocketService).checkSender(Mockito.any(), Mockito.any());
        Mockito.when(webSocketService.checkSender(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.any())).thenReturn(testLobby);
        Mockito.when(playerRepository.save(Mockito.any())).thenReturn(testPlayer);
        Mockito.when(playerRepository.findByIdentity(Mockito.any())).thenReturn(testPlayer);
        Mockito.when(playerRepository.findByUsernameAndLobbyId(Mockito.any(), Mockito.any())).thenReturn(testPlayer);
    }

    @Test
    void sendChatMessage_invalidContent_notSendToLobby() {
        this.chat.setMessage(" ");

        webSocketService.sendChatMessage("lobbyId", "identity", this.chat);

        Mockito.verify(webSocketService, Mockito.times(0)).sendToLobby(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void sendChatMessage_validContent_SendToLobby() {
        this.chat.setMessage("message");

        webSocketService.sendChatMessage("lobbyId", "identity", this.chat);

        Mockito.verify(webSocketService, Mockito.times(1)).sendToLobby(Mockito.matches("lobbyId"), Mockito.matches("/chat"), Mockito.any());
    }
}
