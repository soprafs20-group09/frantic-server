package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;
import ch.uzh.ifi.seal.soprafs20.entity.Chat;
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
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
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
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
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
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
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
            assertEquals(HttpStatus.CONFLICT, ex.getStatus());
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
            assertEquals(HttpStatus.GONE, ex.getStatus());
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
            assertEquals(HttpStatus.GONE, ex.getStatus());
            return;
        }
        fail("ResponseStatusException expected");
    }

    @Test
    void kickPlayer_notAdmin_throwPlayerServiceException() {
        KickDTO kick = Mockito.mock(KickDTO.class);
        try {
            lobbyService.kickPlayer("abc", "identity", kick);
        }
        catch (PlayerServiceException ex) {
            assertEquals("Invalid action. Not admin.", ex.getMessage());
        }
    }

    @Test
    void kickPlayer_admin_success() {
        testPlayer.setAdmin(true);
        KickDTO kick = Mockito.mock(KickDTO.class);

        lobbyService.kickPlayer("abc", testPlayer.getIdentity(), kick);

        Mockito.verify(webSocketService, Mockito.times(1)).sendToPlayer(Mockito.matches(testPlayer.getIdentity()), Mockito.matches("/queue/disconnect"), Mockito.any());
        Mockito.verify(webSocketService, Mockito.times(1)).sendChatMessage(Mockito.matches("abc"), (Chat) Mockito.any());
        Mockito.verify(webSocketService, Mockito.times(1)).sendToLobby(Mockito.matches("abc"), Mockito.matches("/lobby-state"), Mockito.any());
    }

    @Test
    void handleDisconnect_setNewAdmin_sendLobbyState() {
        Mockito.when(playerService.removePlayer(Mockito.any())).thenReturn("lobbyId");

        assertTrue(lobbyRepository.findByLobbyId("abc").getPlayers() > 1);
        lobbyService.handleDisconnect("abc");
        Mockito.verify(webSocketService, Mockito.times(1)).sendChatMessage(Mockito.matches("lobbyId"), (Chat) Mockito.any());
        Mockito.verify(webSocketService, Mockito.times(1)).sendToLobby(Mockito.matches("lobbyId"), Mockito.matches("/lobby-state"), Mockito.any());
    }

    @Test
    void handleDisconnect_onePlayerInGame_sendLobbyState() {
        testLobby.removePlayer(testPlayer);
        testLobby.setIsPlaying(true);
        Mockito.when(playerService.removePlayer(Mockito.any())).thenReturn("lobbyId");

        assertEquals(1, lobbyRepository.findByLobbyId("abc").getPlayers());
        lobbyService.handleDisconnect("abc");
        Mockito.verify(webSocketService, Mockito.times(1)).sendToPlayer(Mockito.matches("testIdentity"), Mockito.matches("/queue/disconnect"), Mockito.any());
    }
}