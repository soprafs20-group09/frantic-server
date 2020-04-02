package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class PlayerRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    public void findByName_success() {
        // given
        Player player = new Player();
        player.setUsername("firstname@lastname");
        player.setIdentity("1234-ABCD-5678-EFGH-9012-IJKL");
        player.setLobbyId((long) 100);

        entityManager.persist(player);
        entityManager.flush();

        // when
        Player found = playerRepository.findByUsernameAndLobbyId(player.getUsername(), player.getLobbyId());

        // then
        assertNotNull(found.getId());
        assertEquals(found.getUsername(), player.getUsername());
        assertEquals(found.getIdentity(), player.getIdentity());
        assertEquals(found.getLobbyId(), player.getLobbyId());
    }
}