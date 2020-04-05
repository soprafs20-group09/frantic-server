package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class LobbyRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LobbyRepository lobbyRepository;

    @Test
    public void find_single_lobby_ByName_success() {
        //given two lobbies
        Player testPlayer1 = new Player();
        testPlayer1.setUsername("testPlayer1");

        Lobby lobby1 = new Lobby();
        lobby1.setName("alpha");
        lobby1.setCreator(testPlayer1.getUsername());
        lobby1.addPlayer(testPlayer1);

        Player testPlayer2 = new Player();
        testPlayer2.setUsername("testPlayer2");

        Lobby lobby2 = new Lobby();
        lobby2.setName("beta");
        lobby2.setCreator(testPlayer2.getUsername());
        lobby2.addPlayer(testPlayer2);

        entityManager.persist(lobby1);
        entityManager.persist(lobby2);
        entityManager.flush();

        // when
        String filter = "alpha";
        List<Lobby> foundLobbies = lobbyRepository.findByNameContainsOrCreatorContains(filter, filter);

        // then
        assertEquals(foundLobbies.size() , 1);
        Lobby foundLobby = foundLobbies.get(0);
        assertEquals(foundLobby.getName(), lobby1.getName());
        assertEquals(foundLobby.getCreator(), lobby1.getCreator());
    }

    @Test
    public void find_single_lobby_ByName_unsuccessful() {
        //given a lobby
        Player testPlayer1 = new Player();
        testPlayer1.setUsername("testPlayer1");

        Lobby lobby1 = new Lobby();
        lobby1.setName("testLobby1");
        lobby1.setCreator(testPlayer1.getUsername());
        lobby1.addPlayer(testPlayer1);

        entityManager.persist(lobby1);
        entityManager.flush();

        // when
        String filter = "alpha";
        List<Lobby> foundLobbies = lobbyRepository.findByNameContainsOrCreatorContains(filter, filter);

        // then
        assertEquals(foundLobbies.size() , 0);
    }

    @Test
    public void find_multiple_lobbies_ByName_success() {
        //given two lobbies
        Player testPlayer1 = new Player();
        testPlayer1.setUsername("testPlayer1");

        Lobby lobby1 = new Lobby();
        lobby1.setName("testLobby");
        lobby1.setCreator(testPlayer1.getUsername());
        lobby1.addPlayer(testPlayer1);

        Player testPlayer2 = new Player();
        testPlayer2.setUsername("testPlayer2");

        Lobby lobby2 = new Lobby();
        lobby2.setName("testLobby");
        lobby2.setCreator(testPlayer2.getUsername());
        lobby2.addPlayer(testPlayer2);

        entityManager.persist(lobby1);
        entityManager.persist(lobby2);
        entityManager.flush();

        // when
        String filter = "testLobby";
        List<Lobby> foundLobbies = lobbyRepository.findByNameContainsOrCreatorContains(filter, filter);

        // then
        assertEquals(foundLobbies.size() , 2);
        Lobby foundLobby1 = foundLobbies.get(0);
        assertEquals(foundLobby1.getName(), lobby1.getName());
        assertEquals(foundLobby1.getCreator(), lobby1.getCreator());

        Lobby foundLobby2 = foundLobbies.get(1);
        assertEquals(foundLobby2.getName(), lobby2.getName());
        assertEquals(foundLobby2.getCreator(), lobby2.getCreator());
    }

    @Test
    public void find_lobby_ByCreator_success() {
        //given two lobbies
        Player testPlayer1 = new Player();
        testPlayer1.setUsername("testPlayer1");

        Lobby lobby1 = new Lobby();
        lobby1.setName("testLobby");
        lobby1.setCreator(testPlayer1.getUsername());
        lobby1.addPlayer(testPlayer1);

        Player testPlayer2 = new Player();
        testPlayer2.setUsername("testPlayer2");

        Lobby lobby2 = new Lobby();
        lobby2.setName("testLobby");
        lobby2.setCreator(testPlayer2.getUsername());
        lobby2.addPlayer(testPlayer2);

        entityManager.persist(lobby1);
        entityManager.persist(lobby2);
        entityManager.flush();

        // when
        String filter = "testPlayer1";
        List<Lobby> foundLobbies = lobbyRepository.findByNameContainsOrCreatorContains(filter, filter);

        // then
        assertEquals(foundLobbies.size() , 1);
        Lobby foundLobby1 = foundLobbies.get(0);
        assertEquals(foundLobby1.getName(), lobby1.getName());
        assertEquals(foundLobby1.getCreator(), lobby1.getCreator());
    }

    @Test
    public void findById_success() {
        //given a lobby
        Player testPlayer1 = new Player();
        testPlayer1.setUsername("testPlayer1");

        Lobby lobby1 = new Lobby();
        lobby1.setName("testLobby1");
        lobby1.setCreator(testPlayer1.getUsername());
        lobby1.addPlayer(testPlayer1);
        lobby1.setLobbyId("ZFwZGdG8eL");

        entityManager.persist(lobby1);
        entityManager.flush();

        // when
        Lobby foundLobby = lobbyRepository.findByLobbyId("ZFwZGdG8eL");

        // then
        assertNotNull(foundLobby.getLobbyId());
        assertEquals(foundLobby.getName(), lobby1.getName());
        assertEquals(foundLobby.getCreator(), lobby1.getCreator());
        assertEquals(foundLobby.getLobbyId(), lobby1.getLobbyId());
    }
}
