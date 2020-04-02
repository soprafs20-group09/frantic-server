package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.exceptions.LobbyServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyJoinDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerScoreDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerUsernameDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.LobbySettingsDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.DisconnectDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.LobbyStateDTO;
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

        if (filter != null) {
            List<Lobby> response = this.lobbyRepository.findByName(filter);
            response.addAll(this.lobbyRepository.findByHost(filter));
            return response;
        }
        return this.lobbyRepository.findAll();
    }

    public List<PlayerScoreDTO> getScores(long id) {
        return null;
    }

    public long createLobby(Player creator) {

        String lobbyName = creator.getUsername() + "'s lobby";

        //create Lobby
        Lobby newLobby = new Lobby();
        newLobby.setHost(creator);
        newLobby.addPlayer(creator);
        newLobby.setName(lobbyName);
        newLobby = this.lobbyRepository.save(newLobby);
        this.lobbyRepository.flush();

        Long lobbyInRepoId = newLobby.getLobbyId();
        creator.setLobbyId(lobbyInRepoId);
        playerRepository.save(creator);
        playerRepository.flush();

        return lobbyInRepoId;
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

            //finds the lobby name for the response and adds +1 to amount of players in the lobby-repository.
            Lobby lobby = lobbyRepository.findByLobbyId(id);
            lobby.addPlayer(newPlayer);
            lobbyRepository.flush();
            response.setName(lobby.getName());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This username is already taken.");
        }
        return response;
    }

    public DisconnectDTO kickPlayer(Player player) {
        Long currentLobbyId = player.getLobbyId();
        if (currentLobbyId == null) {
            throw new LobbyServiceException("There is no lobbyId associated with the given player");
        }
        Lobby currentLobby = lobbyRepository.findByLobbyId(currentLobbyId);
        if (currentLobby == null) {
            throw new LobbyServiceException("The lobby associated with the given player does not exist");
        }

        //remove player from lobby
        currentLobby.removePlayer(player);
        lobbyRepository.flush();

        //remove player from PlayerRepository
        playerRepository.delete(player);
        playerRepository.flush();

        DisconnectDTO response = new DisconnectDTO();
        response.setReason("You were kicked out of the Lobby.");
        return response;
    }

    public LobbyStateDTO updateLobbySettings(Lobby lobbyToUpdate, LobbySettingsDTO newSettings) {
        if (newSettings.getLobbyName() != null) {
            lobbyToUpdate.setName(newSettings.getLobbyName());
        }
        if (newSettings.getDuration() != null) {
            lobbyToUpdate.setGameDuration(newSettings.getDuration());
        }
        if (newSettings.getPublicLobby() != null) {
            lobbyToUpdate.setIsPublic(newSettings.getPublicLobby());
        }
        lobbyRepository.flush();

        LobbyStateDTO response = new LobbyStateDTO();
        response.setSettings(newSettings);
        return response;
    }
}
