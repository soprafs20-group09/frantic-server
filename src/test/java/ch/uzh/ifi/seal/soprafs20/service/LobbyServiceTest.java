package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;
import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.exceptions.PlayerServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.ChatDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.KickDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.LobbySettingsDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.LobbyStateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;
    @Mock
    private PlayerRepository playerRepository;
    @InjectMocks
    private Player testPlayer;
    @InjectMocks
    private LobbyService lobbyService;
    @Mock
    private WebSocketService webSocketService;
    @Mock
    private PlayerService playerService;
    @InjectMocks
    private Lobby testLobby;
    @InjectMocks
    private LobbySettingsDTO testSettings;
    @InjectMocks
    private LobbyStateDTO testState;
    @InjectMocks
    private ChatDTO chat;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testPlayer = new Player();
        testPlayer.setUsername("testPlayer");
        testPlayer.setAdmin(false);

        testLobby = new Lobby();
        testLobby.setName("testLobby");
        testLobby.setCreator("Brian");
        testLobby.addPlayer(testPlayer);
        testLobby.addPlayer(testPlayer);

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.doNothing().when(webSocketService).sendToLobby(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.when(webSocketService.checkSender(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.any())).thenReturn(testLobby);
        Mockito.when(playerRepository.save(Mockito.any())).thenReturn(testPlayer);
        Mockito.when(playerRepository.findByIdentity(Mockito.any())).thenReturn(testPlayer);
        Mockito.when(playerRepository.findByUsernameAndLobbyId(Mockito.any(), Mockito.any())).thenReturn(testPlayer);
    }

    @Test
    public void getLobbiesWithoutFilter_returnLobbies() {
        //setup
        Lobby lobby1 = new Lobby();
        Lobby lobby2 = new Lobby();
        Lobby lobby3 = new Lobby();
        List<Lobby> listOfLobbies = new ArrayList<>();
        listOfLobbies.add(lobby1);
        listOfLobbies.add(lobby2);
        listOfLobbies.add(lobby3);
        Mockito.when(lobbyRepository.findAll()).thenReturn(listOfLobbies);

        //when
        List<Lobby> returnedList = lobbyService.getLobbies(null);

        // then
        assertEquals(listOfLobbies.size(), returnedList.size());
        assertEquals(listOfLobbies.get(0), returnedList.get(0));
        assertEquals(listOfLobbies.get(1), returnedList.get(1));
        assertEquals(listOfLobbies.get(2), returnedList.get(2));
    }

    @Test
    public void getLobbiesWithFilter_returnLobbies() {
        //setup
        Lobby lobby1 = new Lobby();
        lobby1.setName("alpha");
        lobby1.setCreator("Brian");
        Lobby lobby2 = new Lobby();
        lobby2.setName("beta");
        lobby2.setCreator("Peter");

        List<Lobby> listOfLobbies = new ArrayList<>();
        listOfLobbies.add(lobby1);
        listOfLobbies.add(lobby2);
        Mockito.when(lobbyRepository.findByNameContainsOrCreatorContains("alpha", "alpha")).thenReturn(listOfLobbies);

        //when
        List<Lobby> returnedList = lobbyService.getLobbies("alpha");

        // then
        assertEquals(listOfLobbies.size(), returnedList.size());
        assertEquals(listOfLobbies.get(0), returnedList.get(0));
    }

    @Test
    public void getLobbies_returnPublicLobbies() {
        //setup
        Lobby lobby1 = new Lobby();
        lobby1.setName("alpha");
        lobby1.setCreator("Brian");
        lobby1.setIsPublic(false);
        Lobby lobby2 = new Lobby();
        lobby2.setName("beta");
        lobby2.setCreator("Peter");

        List<Lobby> listOfLobbies = new ArrayList<>();
        listOfLobbies.add(lobby1);
        listOfLobbies.add(lobby2);
        Mockito.when(lobbyRepository.findAll()).thenReturn(listOfLobbies);

        //when
        List<Lobby> reference = new ArrayList<>(listOfLobbies);
        List<Lobby> returnedList = lobbyService.getLobbies(null);

        // then
        assertEquals(1, returnedList.size());
        assertEquals(reference.get(1), returnedList.get(0));
    }

    @Test
    public void createLobby_returnLobbyId() {
        String response = lobbyService.createLobby(testPlayer);

        Mockito.verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(playerRepository, Mockito.times(1)).save(Mockito.any());

        assertNotNull(response);
    }

    @Test
    public void joinLobby_addPlayer() {
        lobbyService.joinLobby("abc", testPlayer);

        Mockito.verify(lobbyRepository, Mockito.times(1)).findByLobbyId(Mockito.any());
        Mockito.verify(playerRepository, Mockito.times(1)).findByIdentity(Mockito.any());
    }

    @Test
    public void closeLobby_deletePlayer() {
        lobbyService.closeLobby("abc");

        Mockito.verify(playerRepository, Mockito.times(2)).delete(Mockito.any());
        Mockito.verify(playerRepository, Mockito.times(2)).findByUsernameAndLobbyId(Mockito.any(), Mockito.any());
        Mockito.verify(lobbyRepository, Mockito.times(1)).delete(Mockito.any());
    }

    @Test
    public void getLobbyState_returnLobbyStateDTO() {
        LobbyStateDTO response = lobbyService.getLobbyState("abc");

        assertEquals(testLobby.getGameDuration(), response.getSettings().getDuration());
        assertEquals(testLobby.isPublic(), response.getSettings().getPublicLobby());
        assertEquals(testLobby.getName(), response.getSettings().getLobbyName());
        assertNotNull(response.getPlayers());
    }

    @Test
    public void updateLobbySettings_returnLobbyState() {
        //setup
        testSettings.setDuration(GameLength.LONG);
        testSettings.setLobbyName("newName");
        testSettings.setPublicLobby(false);
        testState.setSettings(testSettings);

        lobbyService.updateLobbySettings("abc", "abc", testSettings);
        LobbyStateDTO response = lobbyService.getLobbyState("abc");

        assertEquals(testSettings.getDuration(), response.getSettings().getDuration());
        assertEquals(testSettings.getLobbyName(), response.getSettings().getLobbyName());
        assertEquals(testSettings.getPublicLobby(), response.getSettings().getPublicLobby());
        assertNotNull(response.getPlayers());
    }

    @Test
    public void isUsernameAlreadyInLobby_false() {
        assertFalse(lobbyService.isUsernameAlreadyInLobby("abc", "someUsername"));
    }

    @Test
    public void isUsernameAlreadyInLobby_true() {
        assertTrue(lobbyService.isUsernameAlreadyInLobby("abc", "testPlayer"));
    }

    @Test
    public void checkLobbyCreate_inputNull_throwResponseStatusException() {
        try {
            lobbyService.checkLobbyCreate(null);
        }
        catch (ResponseStatusException ex) {
            assertEquals(ex.getStatus(), HttpStatus.BAD_REQUEST);
            return;
        }
        fail("ResponseStatusException expected");
    }

    @Test
    public void checkLobbyCreate_inputNotValid_throwResponseStatusException() {
        try {
            lobbyService.checkLobbyCreate(" ");
        }
        catch (ResponseStatusException ex) {
            assertEquals(ex.getStatus(), HttpStatus.BAD_REQUEST);
            return;
        }
        fail("ResponseStatusException expected");
    }

    @Test
    void checkLobbyJoin_lobbyNotFound_throwResponseStatusException() {
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.any())).thenReturn(null);
        try {
            lobbyService.checkLobbyJoin("abc", "username");
        }
        catch (ResponseStatusException ex) {
            assertEquals(ex.getStatus(), HttpStatus.NOT_FOUND);
            return;
        }
        fail("ResponseStatusException expected");
    }

    @Test
    void checkLobbyJoin_usernameAlreadyInLobby_throwResponseStatusException() {
        try {
            lobbyService.checkLobbyJoin("abc", "testPlayer");
        }
        catch (ResponseStatusException ex) {
            assertEquals(ex.getStatus(), HttpStatus.CONFLICT);
            return;
        }
        fail("ResponseStatusException expected");
    }

    @Test
    void checkLobbyJoin_lobbyFull_throwResponseStatusException() {
        testLobby.addPlayer(testPlayer);
        testLobby.addPlayer(testPlayer);
        testLobby.addPlayer(testPlayer);
        testLobby.addPlayer(testPlayer);
        testLobby.addPlayer(testPlayer);
        testLobby.addPlayer(testPlayer);
        try {
            lobbyService.checkLobbyJoin("abc", "username");
        }
        catch (ResponseStatusException ex) {
            assertEquals(ex.getStatus(), HttpStatus.GONE);
            return;
        }
        fail("ResponseStatusException expected");
    }

    @Test
    void checkLobbyJoin_gameAlreadyStarted_throwResponseStatusException() {
        testLobby.setIsPlaying(true);
        try {
            lobbyService.checkLobbyJoin("abc", "username");
        }
        catch (ResponseStatusException ex) {
            assertEquals(ex.getStatus(), HttpStatus.GONE);
            return;
        }
        fail("ResponseStatusException expected");
    }

    @Test
    void kickPlayer_notAdmin_throwPlayerServiceException() {
        KickDTO kick = new KickDTO();
        try {
            lobbyService.kickPlayer("abc", "identity", kick);
        }
        catch (PlayerServiceException ex) {
            assertEquals("Invalid action. Not admin.", ex.getMessage());
        }
    }

    @Test
    void handleDisconnect_notAdmin_sendToLobby() {
        Mockito.when(playerService.removePlayer(Mockito.any())).thenReturn("lobbyId");

        lobbyService.handleDisconnect("abc");

        Mockito.verify(webSocketService, Mockito.times(1)).sendToLobby(Mockito.matches("lobbyId"), Mockito.matches("/lobby-state"), Mockito.any());
    }

    @Test
    void handleDisconnect_admin_sendDisconnectToLobby() {
        testPlayer.setAdmin(true);
        Mockito.when(playerService.removePlayer(Mockito.any())).thenReturn("lobbyId");

        lobbyService.handleDisconnect("abc");

        Mockito.verify(webSocketService, Mockito.times(1)).sendDisconnectToLobby(Mockito.matches("lobbyId"), Mockito.any());
    }

    @Test
    void sendChatMessage_invalidContent_notSendToLobby() {
        this.chat.setMessage(" ");

        lobbyService.sendChatMessage("lobbyId", "identity", this.chat);

        Mockito.verify(webSocketService, Mockito.times(0)).sendToLobby(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void sendChatMessage_validContent_SendToLobby() {
        this.chat.setMessage("message");

        lobbyService.sendChatMessage("lobbyId", "identity", this.chat);

        Mockito.verify(webSocketService, Mockito.times(1)).sendToLobby(Mockito.matches("lobbyId"), Mockito.matches("/chat"), Mockito.any());
    }
}