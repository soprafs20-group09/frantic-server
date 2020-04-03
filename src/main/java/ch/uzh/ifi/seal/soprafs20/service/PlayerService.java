package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.RegisteredDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class PlayerService {

    private final Logger log = LoggerFactory.getLogger(PlayerService.class);

    private final LobbyService lobbyService;

    private final PlayerRepository playerRepository;
    private final LobbyRepository lobbyRepository;

    @Autowired
    public PlayerService(LobbyService lobbyService, @Qualifier("playerRepository") PlayerRepository playerRepository,
                         @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.lobbyService = lobbyService;
        this.playerRepository = playerRepository;
        this.lobbyRepository = lobbyRepository;
    }

    public Player createPlayer(String identity, String username) {

        checkCreate(username);

        Player newPlayer = new Player();
        newPlayer.setUsername(username);
        newPlayer.setIdentity(identity);
        playerRepository.save(newPlayer);
        playerRepository.flush();
        return newPlayer;
    }

    public RegisteredDTO registerPlayer(String identity, Player player, long lobbyId) {

        player.setIdentity(identity);
        playerRepository.flush();

        RegisteredDTO registeredDTO = new RegisteredDTO();
        registeredDTO.setUsername(player.getUsername());
        registeredDTO.setLobbyId(Long.toString(lobbyId));

        return registeredDTO;
    }

    private void checkCreate(String username) {
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is missing or invalid.");
        }
    }

    private void checkCreateInLobby(long lobbyId, String username) {

        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);

        if (lobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found.");
        }
        if (!lobby.isPublic()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lobby is private.");
        }
        if (playerRepository.findByUsernameAndLobbyId(username, lobbyId) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
}
