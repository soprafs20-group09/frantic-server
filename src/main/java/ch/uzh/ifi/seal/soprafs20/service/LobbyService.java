package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyJoinDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerScoreDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerUsernameDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.DisconnectDTO;
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

    public List<Lobby> getLobbies(String filter) {
        return this.lobbyRepository.findAll();
    }

    public List<PlayerScoreDTO> getScores(long id) {
        return null;
    }

    public LobbyJoinDTO createLobby(PlayerUsernameDTO playerUsernameDTO) {

        String hostname = playerUsernameDTO.getUsername();
        String lobbyName = hostname + "'s lobby";

        if (hostname == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is missing or invalid.");
        }

        //create Lobby
        Lobby newLobby = new Lobby();
        newLobby.setCreator(hostname);
        newLobby.setPlayers(1);
        newLobby.setName(lobbyName);
        lobbyRepository.save(newLobby);
        lobbyRepository.flush();

        //create Player
        Player newPlayer = new Player();
        newPlayer.setUsername(hostname);
        String token = UUID.randomUUID().toString();
        newPlayer.setToken(token);

        Lobby lobbyInRepo = lobbyRepository.findByName(lobbyName);
        Long lobbyInRepoId = lobbyInRepo.getLobbyId();
        newPlayer.setLobbyId(lobbyInRepoId);
        playerRepository.save(newPlayer);
        playerRepository.flush();

        LobbyJoinDTO response = new LobbyJoinDTO();
        response.setName(lobbyName);
        response.setUsername(hostname);
        response.setToken(token);
        return response;
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

    public DisconnectDTO kickPlayer(Player player) {
        Long currentLobbyId = player.getLobbyId();
        Lobby currentLobby = lobbyRepository.findByLobbyId(currentLobbyId);

        //remove player from lobby
        currentLobby.setPlayers(currentLobby.getPlayers() - 1);
        lobbyRepository.flush();

        //remove lobby reference from player
        player.setLobbyId(null);
        playerRepository.flush();

        DisconnectDTO response = new DisconnectDTO();
        response.setReason("You were kicked out of the Lobby.");
        return response;
    }
}
