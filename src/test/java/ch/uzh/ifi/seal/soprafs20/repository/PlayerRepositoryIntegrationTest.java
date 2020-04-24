package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PlayerRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PlayerRepository playerRepository;

    @Mock
    private Player player;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        //given
        player = new Player();
        player.setUsername("firstname@lastname");
        player.setIdentity("1234-ABCD-5678-EFGH-9012-IJKL");
        player.setAdmin(false);
        player.setLobbyId("A4F5gg3W");

        entityManager.persist(player);
        entityManager.flush();
    }

    @Test
    public void findByUsernameAndLobbyId_success() {
        // when
        Player found = playerRepository.findByUsernameAndLobbyId(player.getUsername(), player.getLobbyId());

        // then
        assertNotNull(found.getId());
        assertEquals(found.getUsername(), player.getUsername());
        assertEquals(found.getIdentity(), player.getIdentity());
        assertEquals(found.isAdmin(), player.isAdmin());
        assertEquals(found.getLobbyId(), player.getLobbyId());
    }

    @Test
    public void findByUsernameAndLobbyId_invalidUsername_unsuccessful() {
        // when
        Player found = playerRepository.findByUsernameAndLobbyId("poop", player.getLobbyId());

        // then
        assertNull(found);
    }

    @Test
    public void findByUsernameAndLobbyId_invalidLobbyId_unsuccessful() {
        // when
        Player found = playerRepository.findByUsernameAndLobbyId(player.getUsername(), "poop");

        // then
        assertNull(found);
    }

    @Test
    public void findByUsernameAndLobbyId_invalidUsernameAndLobbyId_unsuccessful() {
        // when
        Player found = playerRepository.findByUsernameAndLobbyId("poop", "poop");

        // then
        assertNull(found);
    }

    @Test
    public void findByIdentity_success() {
        // when
        Player found = playerRepository.findByIdentity(player.getIdentity());

        // then
        assertNotNull(found.getId());
        assertEquals(found.getUsername(), player.getUsername());
        assertEquals(found.getIdentity(), player.getIdentity());
        assertEquals(found.isAdmin(), player.isAdmin());
        assertEquals(found.getLobbyId(), player.getLobbyId());
    }

    @Test
    public void findByIdentity_invalidIdentity_unsuccessful() {
        // when
        Player found = playerRepository.findByIdentity("poop");

        // then
        assertNull(found);
    }

    @Test
    public void findByLobbyId_successful() {
        // when
        List<Player> found = playerRepository.findByLobbyId(player.getLobbyId());

        // then
        assertEquals(1, found.size());
        for (Player foundPlayer : found) {
            assertNotNull(foundPlayer.getId());
            assertEquals(foundPlayer.getUsername(), player.getUsername());
            assertEquals(foundPlayer.getIdentity(), player.getIdentity());
            assertEquals(foundPlayer.isAdmin(), player.isAdmin());
            assertEquals(foundPlayer.getLobbyId(), player.getLobbyId());
        }
    }

    @Test
    public void findByLobbyId_invalidLobbyId_unsuccessful() {
        // when
        List<Player> found = playerRepository.findByLobbyId("poop");
        List<Player> empty = new ArrayList<>();

        // then
        assertEquals(empty, found);
    }
}