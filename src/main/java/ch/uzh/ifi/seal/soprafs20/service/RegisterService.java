package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.EventChat;
import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.exceptions.PlayerServiceException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyJoinDTO;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.RegisterDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.RegisteredDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handles authentication / registration dance
 */
@Service
@Transactional
public class RegisterService {

    private final WebSocketService webSocketService;
    private final PlayerService playerService;
    private final LobbyService lobbyService;

    private final Map<String, String[]> authMap = new HashMap<>();

    public RegisterService(WebSocketService webSocketService, PlayerService playerService, LobbyService lobbyService) {
        this.webSocketService = webSocketService;
        this.playerService = playerService;
        this.lobbyService = lobbyService;
    }

    public LobbyJoinDTO prepareLobby(String username) {
        return prepareLobby(null, username);
    }

    public LobbyJoinDTO prepareLobby(String lobbyId, String username) {
        String authToken = UUID.randomUUID().toString();
        addToAuthMap(authToken, username, lobbyId);

        LobbyJoinDTO response = new LobbyJoinDTO();
        response.setToken(authToken);
        response.setName(username + "'s lobby");
        response.setUsername(username);
        return response;
    }

    public synchronized void joinLobby(String identity, RegisterDTO register) {
        String username = getUsernameFromAuthToken(register.getToken());
        if (username == null) {
            throw new PlayerServiceException("Player not authenticated.");
        }
        Player player = this.playerService.createPlayer(identity, username);

        String lobbyId = getLobbyIdFromAuthToken(register.getToken());
        if (lobbyId == null) {
            lobbyId = this.lobbyService.createLobby(player);
        }
        else {
            this.lobbyService.joinLobby(lobbyId, player);
        }
        RegisteredDTO registeredDTO = this.playerService.registerPlayer(identity, player, lobbyId);
        removeFromAuthMap(register.getToken());

        this.webSocketService.sendToPlayer(identity, "/queue/register", registeredDTO);
        // wait for player to subscribe to channels
        FranticUtils.wait(500);
        // send initial lobby-state packet
        this.webSocketService.sendToLobby(lobbyId, "/lobby-state", lobbyService.getLobbyState(lobbyId));
        Chat chat = new EventChat("avatar:" + player.getUsername(), player.getUsername() + " joined the lobby.");
        this.webSocketService.sendChatMessage(lobbyId, chat);
    }

    private void addToAuthMap(String authToken, String username, String lobbyId) {
        this.authMap.put(authToken, new String[]{username, lobbyId});
    }

    public String getUsernameFromAuthToken(String authToken) {
        if (this.authMap.size() > 0) {
            return this.authMap.get(authToken)[0];
        }
        return null;
    }

    public String getLobbyIdFromAuthToken(String authToken) {
        if (this.authMap.containsKey(authToken)) {
            if (this.authMap.get(authToken).length > 1) {
                return this.authMap.get(authToken)[1];
            }
        }
        return null;
    }

    public void removeFromAuthMap(String authToken) {
        this.authMap.remove(authToken);
    }
}
