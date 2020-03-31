package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyJoinDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerScoreDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerUsernameDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class LobbyService {

    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final PlayerRepository playerRepository;
    private final LobbyRepository lobbyRepository;

    @Autowired
    public LobbyService(@Qualifier("playerRepository") PlayerRepository playerRepository,
                        @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.playerRepository = playerRepository;
        this.lobbyRepository = lobbyRepository;
    }

    public List<Lobby> getLobbies(String name, String creator) {
        return this.lobbyRepository.findAll();
    }

    public List<PlayerScoreDTO> getScores(long id) {
        return null;
    }

    public LobbyJoinDTO createLobby(PlayerUsernameDTO playerUsernameDTO) {
        return new LobbyJoinDTO();
    }

    public LobbyJoinDTO joinLobby(long id, PlayerUsernameDTO playerUsernameDTO) {
        LobbyJoinDTO response = new LobbyJoinDTO();

        //creates a new Player, if there is no other Player with the same username in the lobby.
        if (this.playerRepository.findByUsernameAndLobbyId(playerUsernameDTO.getUsername(), id) == null) {
            Player newPlayer = new Player();
            newPlayer.setUsername(playerUsernameDTO.getUsername());
            newPlayer.setToken(UUID.randomUUID().toString());
            newPlayer.setLobbyId(id);

            // saves the given entity but data is only persisted in the database once flush() is called
            newPlayer = playerRepository.save(newPlayer);
            playerRepository.flush();

            log.debug("Created a new Player: {}", newPlayer);

            response.setUsername(newPlayer.getUsername());
            response.setToken(newPlayer.getToken());

            //finds the lobby name for the response and adds +1 to players in the lobby.
            Lobby lobby = lobbyRepository.findByLobbyId(id);
            lobby.setPlayers(lobby.getPlayers() + 1);
            lobbyRepository.save(lobby);
            lobbyRepository.flush();
            response.setName(lobby.getName());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This username is already taken.");
        }
        return response;
    }
}
