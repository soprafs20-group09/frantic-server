package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("lobbyRepository")
public interface LobbyRepository extends JpaRepository<Lobby, Long> {

    List<Lobby> findByName(String name);
    List<Lobby> findByHost(String host);
    Lobby findByLobbyId(Long id);
}
