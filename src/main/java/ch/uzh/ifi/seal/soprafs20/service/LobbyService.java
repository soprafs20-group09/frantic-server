package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.exceptions.PlayerServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerScoreDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.ChatDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.KickDTO;
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

    private final WebSocketService webSocketService;
    private final PlayerService playerService;

    private final PlayerRepository playerRepository;
    private final LobbyRepository lobbyRepository;

    @Autowired
    public LobbyService(WebSocketService webSocketService, PlayerService playerService,
                        @Qualifier("playerRepository") PlayerRepository playerRepository,
                        @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.webSocketService = webSocketService;
        this.playerService = playerService;
        this.playerRepository = playerRepository;
        this.lobbyRepository = lobbyRepository;
    }

    public List<Lobby> getLobbies(String q) {

        List<Lobby> allLobbies;
        if (q != null) {
            allLobbies =  lobbyRepository.findByNameContainsOrCreatorContains(q, q);
        } else {
            allLobbies = lobbyRepository.findAll();
        }
        allLobbies.removeIf(lobby -> !lobby.isPublic());
        allLobbies.removeIf(Lobby::isPlaying);
        log.debug(String.format("Retrieved list of lobbies that contain '%s'", q));
        return allLobbies;
    }

    public List<PlayerScoreDTO> getScores(String lobbyId) {
        return null;
    }

    public String createLobby(Player creator) {

        String lobbyName = creator.getUsername() + "'s lobby";

        Lobby newLobby = new Lobby();
        newLobby.setCreator(creator.getUsername());
        newLobby.addPlayer(creator);
        newLobby.setName(lobbyName);
        newLobby = this.lobbyRepository.save(newLobby);
        this.lobbyRepository.flush();
        log.debug(String.format("'%s' created lobby '%s' with ID '%s'",
                creator.getUsername(), newLobby.getName(), newLobby.getLobbyId()));

        String lobbyId = newLobby.getLobbyId();
        creator.setLobbyId(lobbyId);
        creator.setAdmin(true);
        playerRepository.save(creator);
        playerRepository.flush();

        return lobbyId;
    }

    public void joinLobby(String lobbyId, Player player) {

        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        lobby.addPlayer(player);
        lobbyRepository.flush();
        player = playerRepository.findByIdentity(player.getIdentity());
        player.setLobbyId(lobbyId);
        playerRepository.flush();
        log.debug(String.format("'%s' joined lobby '%s' with ID '%s'",
                player.getUsername(), lobby.getName(), lobby.getLobbyId()));
    }

    public void kickPlayer(String lobbyId, String identity, KickDTO dto) {

        if (webSocketService.checkSender(lobbyId, identity)) {
            Player admin = playerRepository.findByIdentity(identity);
            if (!admin.isAdmin()) {
                throw new PlayerServiceException("Invalid action. Not admin.");
            }

            Player toKick = playerRepository.findByUsernameAndLobbyId(dto.getUsername(), lobbyId);
            DisconnectDTO disconnectDTO = new DisconnectDTO();
            disconnectDTO.setReason("You were kicked out of the Lobby.");
            webSocketService.sendToPlayer(toKick.getIdentity(), "/queue/disconnect", disconnectDTO);
            playerService.removePlayer(toKick);

            webSocketService.sendChatPlayerMessage(lobbyId, "was kicked!", toKick.getUsername());
            webSocketService.sendToLobby(lobbyId, "/lobby-state", getLobbyState(lobbyId));
        }
    }

    public void handleDisconnect(String identity) {

        Player player = playerRepository.findByIdentity(identity);

        if (player != null) {
            String lobbyId = playerService.removePlayer(player);
            if (lobbyId != null) {
                webSocketService.sendChatPlayerMessage(lobbyId, "left the lobby.", player.getUsername());
                if (player.isAdmin()) {
                    DisconnectDTO message = new DisconnectDTO();
                    message.setReason("Host left the lobby.");
                    webSocketService.sendDisconnectToLobby(lobbyId, message);
                    closeLobby(lobbyId);
                } else {
                    webSocketService.sendToLobby(lobbyId, "/lobby-state", getLobbyState(lobbyId));
                }
            }
        }
    }

    public void closeLobby(String lobbyId) {

        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);

        List<String> players = lobby.getListOfPlayers();
        for (String player : players) {
            Player p = playerRepository.findByUsernameAndLobbyId(player, lobby.getLobbyId());
            playerRepository.delete(p);
        }
        playerRepository.flush();
        lobbyRepository.delete(lobby);
        log.debug(String.format("Lobby '%s' with ID '%s' was closed", lobby.getName(), lobby.getLobbyId()));
    }

    public void updateLobbySettings(String lobbyId, String identity, LobbySettingsDTO dto) {

        if (webSocketService.checkSender(lobbyId, identity)) {
            Lobby lobbyToUpdate = lobbyRepository.findByLobbyId(lobbyId);
            if (dto.getLobbyName() != null && !dto.getLobbyName().matches("^\\s*$")) {
                lobbyToUpdate.setName(dto.getLobbyName());
            }
            if (dto.getDuration() != null) {
                lobbyToUpdate.setGameDuration(dto.getDuration());
            }
            if (dto.getPublicLobby() != null) {
                lobbyToUpdate.setIsPublic(dto.getPublicLobby());
            }
            lobbyRepository.flush();

            webSocketService.sendToLobby(lobbyId, "/lobby-state", getLobbyState(lobbyId));
        }
    }

    public LobbyStateDTO getLobbyState(String lobbyId) {
        Lobby lobby = this.lobbyRepository.findByLobbyId(lobbyId);
        LobbyStateDTO response = new LobbyStateDTO();

        LobbyPlayerDTO[] players = new LobbyPlayerDTO[lobby.getPlayers()];
        int c = 0;
        for (String p : lobby.getListOfPlayers()) {
            Player currentPlayer = playerRepository.findByUsernameAndLobbyId(p, lobbyId);
            LobbyPlayerDTO player = new LobbyPlayerDTO();
            player.setUsername(currentPlayer.getUsername());
            player.setAdmin(currentPlayer.isAdmin());
            players[c] = player;
            c++;
        }
        response.setPlayers(players);

        LobbySettingsDTO settings = new LobbySettingsDTO();
        settings.setLobbyName(lobby.getName());
        settings.setDuration(lobby.getGameDuration());
        settings.setPublicLobby(lobby.isPublic());
        response.setSettings(settings);
        return response;
    }

    public void sendChatMessage(String lobbyId, String identity, ChatDTO dto) {

        if (webSocketService.checkSender(lobbyId, identity)) {
            if (dto.getMessage() != null && !dto.getMessage().matches("^\\s*$")) {
                Player sender = this.playerRepository.findByIdentity(identity);

                dto.setType("msg");
                dto.setUsername(sender.getUsername());

                webSocketService.sendToLobby(lobbyId, "/chat", dto);
            }
        }
    }

    public boolean isUsernameAlreadyInLobby(String lobbyId, String username) {
        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        List<String> players = lobby.getListOfPlayers();
        for (String player : players) {
            if (player.equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void checkLobbyJoin(String lobbyId, String username) {

        checkLobbyCreate(username);
        if (lobbyRepository.findByLobbyId(lobbyId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found.");
        }
        if (isUsernameAlreadyInLobby(lobbyId, username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists.");
        }
        if (lobbyRepository.findByLobbyId(lobbyId).getPlayers() >= 8) {
            throw new ResponseStatusException(HttpStatus.GONE, "Lobby is full.");
        }
        if (lobbyRepository.findByLobbyId(lobbyId).isPlaying()) {
            throw new ResponseStatusException(HttpStatus.GONE, "The game has already started");
        }
    }

    public void checkLobbyCreate(String username) {

        if (username == null || !username.matches("^\\S{2,20}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username missing or invalid.");
        }
    }
}
