package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;
import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.KickDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.LobbySettingsDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.LobbyStateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class LobbyServiceIntegrationTest {

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @MockBean
    private WebSocketService webSocketService;

    @Autowired
    private LobbyService lobbyService;

    private Player player1;
    private Player player2;
    private Player player3;

    @BeforeEach
    void setup() {
        Mockito.doNothing().when(webSocketService).sendToLobby(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.when(webSocketService.checkSender(Mockito.any(), Mockito.any())).thenReturn(true);

        lobbyRepository.deleteAll();
        playerRepository.deleteAll();

        this.player1 = new Player();
        player1.setUsername("Ben");
        player1.setIdentity("kRWWljUZ6F");
        playerRepository.save(player1);
        playerRepository.flush();

        this.player2 = new Player();
        player2.setUsername("Peter");
        player2.setIdentity("Z6ETpRjYS");
        playerRepository.save(player2);
        playerRepository.flush();

        this.player3 = new Player();
        player3.setUsername("Leila");
        player3.setIdentity("BRve6Z4S60");
        playerRepository.save(player3);
        playerRepository.flush();
    }

    @Test
    void getLobbies_FilteredByName_success() {
        lobbyService.createLobby(player1);
        String lobbyId = lobbyService.createLobby(player2);
        lobbyService.createLobby(player3);

        Lobby reference = lobbyRepository.findByLobbyId(lobbyId);

        List<Lobby> response = lobbyService.getLobbies("Peter's lobby");
        assertEquals(1, response.size());
        Lobby responseLobby = response.get(0);
        assertEquals(reference.getName(), responseLobby.getName());
        assertEquals(reference.getCreator(), responseLobby.getCreator());
        assertEquals(reference.getLobbyId(), responseLobby.getLobbyId());
    }

    @Test
    void getLobbies_FilteredByCreator_success() {
        lobbyService.createLobby(player1);
        lobbyService.createLobby(player2);
        String lobbyId = lobbyService.createLobby(player3);

        Lobby reference = lobbyRepository.findByLobbyId(lobbyId);

        List<Lobby> response = lobbyService.getLobbies("Leila");
        assertEquals(1, response.size());
        Lobby responseLobby = response.get(0);
        assertEquals(reference.getName(), responseLobby.getName());
        assertEquals(reference.getCreator(), responseLobby.getCreator());
        assertEquals(reference.getLobbyId(), responseLobby.getLobbyId());
    }

    @Test
    void createLobby() {
        String lobbyId = lobbyService.createLobby(player1);
        Lobby referenceLobby = lobbyRepository.findByLobbyId(lobbyId);
        assertEquals(referenceLobby.getLobbyId(), lobbyId);
    }

    @Test
    void joinLobby_addPlayer() {
        String lobbyId = lobbyService.createLobby(player1);
        Lobby referenceLobby = lobbyRepository.findByLobbyId(lobbyId);
        assertEquals(1, referenceLobby.getPlayers());

        lobbyService.joinLobby(lobbyId, player2);
        referenceLobby = lobbyRepository.findByLobbyId(lobbyId);
        assertEquals(2, referenceLobby.getPlayers());
    }

    @Test
    void asAdmin_kickPlayer() {
        player1.setAdmin(true);
        String lobbyId = lobbyService.createLobby(player1);
        lobbyService.joinLobby(lobbyId, player2);

        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        assertEquals(2, lobby.getPlayers());

        KickDTO kickDTO = new KickDTO();
        kickDTO.setUsername(player2.getUsername());
        lobbyService.kickPlayer(lobbyId, player1.getIdentity(), kickDTO);

        lobby = lobbyRepository.findByLobbyId(lobbyId);
        assertEquals(1, lobby.getPlayers());
        assertEquals(player1.getUsername(), lobby.getListOfPlayers().get(0));
    }

    @Test
    void updateLobbySettings_updateLobby() {
        String lobbyId = lobbyService.createLobby(player1);
        LobbySettingsDTO newSettings = new LobbySettingsDTO();
        newSettings.setPublicLobby(false);
        newSettings.setDuration(GameLength.SHORT);
        newSettings.setLobbyName("Best Lobby");

        //before
        Lobby reference = lobbyRepository.findByLobbyId(lobbyId);
        assertEquals("Ben's lobby", reference.getName());
        assertEquals(GameLength.MEDIUM, reference.getGameDuration());
        assertTrue(reference.isPublic());

        lobbyService.updateLobbySettings(lobbyId, "abc", newSettings);

        //after
        reference = lobbyRepository.findByLobbyId(lobbyId);
        assertEquals("Best Lobby", reference.getName());
        assertEquals(GameLength.SHORT, reference.getGameDuration());
        assertFalse(reference.isPublic());
    }

    @Test
    public void getLobbyState_ReturnCorrectState() {
        String lobbyId = lobbyService.createLobby(player1);
        Lobby reference = lobbyRepository.findByLobbyId(lobbyId);
        LobbyStateDTO response = lobbyService.getLobbyState(lobbyId);
        assertEquals(reference.getName(), response.getSettings().getLobbyName());
        assertEquals(reference.getGameDuration(), response.getSettings().getDuration());
        assertEquals(reference.isPublic(), response.getSettings().getPublicLobby());
    }

    @Test
    public void isUsernameAlreadyInLobby_ReturnTrue() {
        String lobbyId = lobbyService.createLobby(player1);
        assertTrue(lobbyService.isUsernameAlreadyInLobby(lobbyId, player1.getUsername()));
    }

    @Test
    public void isUsernameAlreadyInLobby_ReturnFalse() {
        String lobbyId = lobbyService.createLobby(player1);
        assertFalse(lobbyService.isUsernameAlreadyInLobby(lobbyId, player2.getUsername()));
    }
}
