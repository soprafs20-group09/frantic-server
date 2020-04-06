package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LobbyServiceTest {
    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
    private LobbyService lobbyService;

    private Lobby testLobby;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
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
}
