package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.ChatDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.EndRoundDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.TimerDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebSocketServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private Game game;
    @Mock
    private GameRound gameRound;
    @Mock
    private LobbyService lobbyService;
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
        GameRepository.addGame("testLobbyId", game);

        // given
        testPlayer = new Player();
        testPlayer.setUsername("testPlayer");
        testPlayer.setAdmin(false);
        testPlayer.setIdentity("testIdentity");
        testPlayer.setLobbyId("testLobbyId");

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
        Mockito.when(playerRepository.findByLobbyId(Mockito.any())).thenReturn(Collections.singletonList(testPlayer));
        Mockito.when(playerRepository.findByUsernameAndLobbyId(Mockito.any(), Mockito.any())).thenReturn(testPlayer);
        Mockito.when(game.getCurrentGameRound()).thenReturn(gameRound);
        Mockito.doNothing().when(webSocketService).sendToPlayer(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(webSocketService).sendToPlayerInLobby(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void sendChatMessage_invalidContent_notSendToLobby() {
        this.chat.setMessage(" ");

        webSocketService.sendChatMessage("lobbyId", "identity", this.chat);

        Mockito.verify(webSocketService, Mockito.times(0)).sendToLobby(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void sendChatMessage_validContent_SendToLobby() {
        this.chat.setMessage("message");

        webSocketService.sendChatMessage("lobbyId", "identity", this.chat);

        Mockito.verify(webSocketService, Mockito.times(1)).sendToLobby(Mockito.matches("lobbyId"), Mockito.matches("/chat"), Mockito.any());
    }

    @Test
    public void parseEndCommandTest() {
        Mockito.doNothing().when(gameRound).onRoundOver(Mockito.anyBoolean());

        testPlayer.setAdmin(true);

        ChatDTO dto = new ChatDTO();
        dto.setMessage("/end");
        webSocketService.sendChatMessage("testLobby", "testIdentity", dto);

        Mockito.verify(gameRound).onRoundOver(Mockito.anyBoolean());
    }

    @Test
    public void parseKickCommandTest() {
        Mockito.doNothing().when(lobbyService).kickPlayer(Mockito.any(), Mockito.any(), Mockito.any());

        testPlayer.setAdmin(true);

        ChatDTO dto = new ChatDTO();
        dto.setMessage("/kick testPlayer");
        webSocketService.sendChatMessage("testLobby", "testIdentity", dto);

        Mockito.verify(lobbyService, Mockito.times(1)).kickPlayer(Mockito.any(), Mockito.matches("testIdentity"), Mockito.any());
    }

    @Test
    public void checkSenderTest() {
        assertTrue(webSocketService.checkSender("testLobbyId", "testIdentity"));
    }

    @Test
    public void endChatListMessageTest() {
        Chat chat1 = new Chat("msg", "testIcon", "testMessage1");
        Chat chat2 = new Chat("msg", "testIcon", "testMessage2");
        List<Chat> chats = new ArrayList<>();
        chats.add(chat1);
        chats.add(chat2);

        webSocketService.sendChatMessage("testLobbyId", chats);
        Mockito.verify(webSocketService, Mockito.times(2)).sendChatMessage(Mockito.any(), (Chat) Mockito.any());
    }

    @Test
    public void sendToLobbyTest() {
        webSocketService.sendToLobby("testLobbyId", "testPath");
        Mockito.verify(webSocketService).sendToPlayerInLobby(Mockito.matches("testLobbyId"), Mockito.matches("testIdentity"), Mockito.matches("testPath"), Mockito.any());
    }
}
