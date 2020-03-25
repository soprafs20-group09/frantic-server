package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("playerRepository")
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByUsername(String username);
}
