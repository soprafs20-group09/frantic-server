package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.exceptions.LobbyServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerScoreDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.LobbySettingsDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.DisconnectDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.LobbyPlayerDTO;
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
            return this.lobbyRepository.findByNameContainsOrCreatorContains(filter, filter);
        }
        return this.lobbyRepository.findAll();
    }

    public List<PlayerScoreDTO> getScores(long id) {
        return null;
    }

    public long createLobby(Player creator) {

        String lobbyName = creator.getUsername() + "'s lobby";

        Lobby newLobby = new Lobby();
        newLobby.setCreator(creator.getUsername());
        newLobby.addPlayer(creator);
        newLobby.setName(lobbyName);
        newLobby = this.lobbyRepository.save(newLobby);
        this.lobbyRepository.flush();

        long lobbyId = newLobby.getLobbyId();
        creator.setLobbyId(lobbyId);
        playerRepository.save(creator);
        playerRepository.flush();

        return lobbyId;
    }

    public void joinLobby(long lobbyId, Player player) {

        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        lobby.addPlayer(player);
        lobbyRepository.flush();
        player.setLobbyId(lobbyId);
        playerRepository.flush();
    }

    public DisconnectDTO kickPlayer(long lobbyId, Player player) {

        Lobby currentLobby = lobbyRepository.findByLobbyId(lobbyId);
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

    public LobbyStateDTO getLobbyState(long lobbyId) {
        Lobby lobby = this.lobbyRepository.findByLobbyId(lobbyId);
        LobbyStateDTO response = new LobbyStateDTO();

        LobbyPlayerDTO[] players = new LobbyPlayerDTO[lobby.getPlayers()];
        int c = 0;
        for (Player p : lobby.getListOfPlayers()) {
            LobbyPlayerDTO player = new LobbyPlayerDTO();
            player.setUsername(p.getUsername());
            player.setAdmin(p.isAdmin());
            players[c] = player;
        }
        response.setPlayers(players);

        LobbySettingsDTO settings = new LobbySettingsDTO();
        settings.setLobbyName(lobby.getName());
        settings.setDuration(lobby.getGameDuration());
        settings.setPublicLobby(lobby.isPublic());
        response.setSettings(settings);

        return response;
    }

    public boolean isUsernameAlreadyInLobby(long lobbyId, String username) {
        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        List<Player> players = lobby.getListOfPlayers();
        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void checkLobbyJoin(long lobbyId, String username) {

        if (username == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is missing.");
        }
        if (lobbyRepository.findByLobbyId(lobbyId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found.");
        }
        if (!lobbyRepository.findByLobbyId(lobbyId).isPublic()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lobby is private.");
        }
        if (isUsernameAlreadyInLobby(lobbyId, username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists.");
        }
    }
}
