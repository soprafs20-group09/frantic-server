package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.exceptions.PlayerServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
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
            allLobbies = lobbyRepository.findByNameContainsOrCreatorContains(q, q);
        }
        else {
            allLobbies = lobbyRepository.findAll();
        }
        allLobbies.removeIf(lobby -> !lobby.isPublic());
        allLobbies.removeIf(Lobby::isPlaying);
        return allLobbies;
    }

    public List<PlayerScoreDTO> getScores(String lobbyId) {
        return null;
    }

    public synchronized String createLobby(Player creator) {

        String lobbyName = creator.getUsername() + "'s lobby";

        Lobby newLobby = new Lobby();
        newLobby.setCreator(creator.getUsername());
        newLobby.addPlayer(creator);
        newLobby.setName(lobbyName);
        newLobby = this.lobbyRepository.save(newLobby);
        this.lobbyRepository.flush();

        String lobbyId = newLobby.getLobbyId();
        creator.setLobbyId(lobbyId);
        creator.setAdmin(true);
        playerRepository.save(creator);
        playerRepository.flush();

        return lobbyId;
    }

    public synchronized void joinLobby(String lobbyId, Player player) {

        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        lobby.addPlayer(player);
        lobbyRepository.flush();
        player = playerRepository.findByIdentity(player.getIdentity());
        player.setLobbyId(lobbyId);
        playerRepository.flush();
    }

    public synchronized void kickPlayer(String lobbyId, String identity, KickDTO dto) {

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

            Chat chat = new Chat("event", "avatar:" + toKick.getUsername(), toKick.getUsername() + " was kicked!");
            webSocketService.sendChatMessage(lobbyId, chat);
            webSocketService.sendToLobby(lobbyId, "/lobby-state", getLobbyState(lobbyId));
        }
    }

    public void handleDisconnect(String identity) {

        Player player = playerRepository.findByIdentity(identity);

        if (player != null) {
            String lobbyId = playerService.removePlayer(player);
            Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
            if (lobby != null) {
                Chat chat = new Chat("event", "avatar:" + player.getUsername(), player.getUsername() + " left the lobby.");
                webSocketService.sendChatMessage(lobbyId, chat);
                List<String> playerList = lobby.getListOfPlayers();
                if (playerList.size() > 1) {
                    if (player.isAdmin()) {
                        setNewHost(lobbyId, playerList.get(0));
                    }
                    webSocketService.sendToLobby(lobbyId, "/lobby-state", getLobbyState(lobbyId));
                }
                else if (playerList.size() == 1) {
                    if (lobby.isPlaying()) {
                        Player last = playerRepository.findByUsernameAndLobbyId(playerList.get(0), lobbyId);
                        DisconnectDTO disconnectDTO = new DisconnectDTO();
                        disconnectDTO.setReason("Not enough players to play.");
                        webSocketService.sendToPlayer(last.getIdentity(), "/queue/disconnect", disconnectDTO);
                        playerService.removePlayer(last);
                        lobbyRepository.delete(lobby);
                        GameRepository.removeGame(lobbyId);
                    }
                    else {
                        if (player.isAdmin()) {
                            setNewHost(lobbyId, playerList.get(0));
                        }
                        webSocketService.sendToLobby(lobbyId, "/lobby-state", getLobbyState(lobbyId));
                    }
                }
                else {
                    lobbyRepository.delete(lobby);
                }
            }
        }
    }

    private synchronized void setNewHost(String lobbyId, String newHostUsername) {
        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        Player newHost = playerRepository.findByUsernameAndLobbyId(newHostUsername, lobbyId);
        newHost.setAdmin(true);
        lobby.setCreator(newHost.getUsername());
        playerRepository.flush();
        lobbyRepository.flush();
        Chat chat = new Chat("event", "avatar:" + newHost.getUsername(), newHost.getUsername() + " is now host.");
        webSocketService.sendChatMessage(lobbyId, chat);
    }

    public synchronized void updateLobbySettings(String lobbyId, String identity, LobbySettingsDTO dto) {

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
