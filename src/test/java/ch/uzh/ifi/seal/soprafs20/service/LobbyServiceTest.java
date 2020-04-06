package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private Player testPlayer;

    @InjectMocks
    private LobbyService lobbyService;

    @InjectMocks
    private Lobby testLobby;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testLobby.setName("testLobby");
        testLobby.setCreator("Brian");

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);
        Mockito.when(testPlayer.getUsername()).thenReturn("testPlayer");
        Mockito.when(playerRepository.save(Mockito.any())).thenReturn(testPlayer);
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
        assertEquals(returnedList.size(), listOfLobbies.size());
        assertEquals(returnedList.get(0), listOfLobbies.get(0));
        assertEquals(returnedList.get(1), listOfLobbies.get(1));
        assertEquals(returnedList.get(2), listOfLobbies.get(2));
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
        assertEquals(returnedList.size(), listOfLobbies.size());
        assertEquals(returnedList.get(0), listOfLobbies.get(0));
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
        assertEquals(returnedList.size(), 1);
        assertEquals(returnedList.get(0), reference.get(1));
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
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.any())).thenReturn(testLobby);
        Mockito.when(playerRepository.findByIdentity(Mockito.any())).thenReturn(testPlayer);
        lobbyService.joinLobby("abc", testPlayer);

        Mockito.verify(lobbyRepository, Mockito.times(1)).findByLobbyId(Mockito.any());
        Mockito.verify(playerRepository, Mockito.times(1)).findByIdentity(Mockito.any());
    }
}
