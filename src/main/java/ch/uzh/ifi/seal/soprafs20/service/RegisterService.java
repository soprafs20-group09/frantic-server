package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.EventChat;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.exceptions.PlayerServiceException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyJoinDTO;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.RegisterDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.RegisteredDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        response.setName(username + "'s lobby"); // could perform lookup to get changed lobby name
        response.setUsername(username);
        return response;
    }

    private void addToAuthMap(String authToken, String username, String lobbyId) {
        authMap.put(authToken, new String[]{username, lobbyId});
    }

    public synchronized void joinLobby(String identity, RegisterDTO register) {
        String username = getUsernameFromAuthToken(register.getToken());
        if (username == null) {
            throw new PlayerServiceException("Player not authenticated.");
        }
        Player player = playerService.createPlayer(identity, username);

        String lobbyId = getLobbyIdFromAuthToken(register.getToken());
        if (lobbyId == null) {
            lobbyId = lobbyService.createLobby(player);
        }
        else {
            lobbyService.joinLobby(lobbyId, player);
        }
        RegisteredDTO registeredDTO = playerService.registerPlayer(identity, player, lobbyId);
        removeFromAuthMap(register.getToken());

        webSocketService.sendToPlayer(identity, "/queue/register", registeredDTO);
        // wait for player to subscribe to channels
        FranticUtils.wait(500);
        // send initial lobby-state packet
        webSocketService.sendToLobby(lobbyId, "/lobby-state", lobbyService.getLobbyState(lobbyId));
        Chat chat = new EventChat("avatar:" + player.getUsername(), player.getUsername() + " joined the lobby.");
        webSocketService.sendChatMessage(lobbyId, chat);
    }

    public String getUsernameFromAuthToken(String authToken) {
        if (authMap.size() > 0) {
            return authMap.get(authToken)[0];
        }
        else {
            return null;
        }
    }

    public String getLobbyIdFromAuthToken(String authToken) {
        if (authMap.containsKey(authToken)) {
            if (authMap.get(authToken).length > 1) {
                return authMap.get(authToken)[1];
            }
            return null;
        }
        return null;
    }

    public void removeFromAuthMap(String authToken) {
        authMap.remove(authToken);
    }
}
