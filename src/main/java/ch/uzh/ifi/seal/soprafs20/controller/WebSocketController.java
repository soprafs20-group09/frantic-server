package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.ChatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;

public class WebSocketController {

    @Autowired
    protected SimpMessagingTemplate simp;

    protected final LobbyService lobbyService;
    protected final PlayerService playerService;

    protected final PlayerRepository playerRepository;
    protected final LobbyRepository lobbyRepository;

    public WebSocketController(LobbyService lobbyService, PlayerService playerService, @Qualifier("playerRepository") PlayerRepository playerRepository,
                           @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.lobbyService = lobbyService;
        this.playerService = playerService;
        this.playerRepository = playerRepository;
        this.lobbyRepository = lobbyRepository;
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String identity = event.getUser().getName();
        Player player = playerRepository.findByIdentity(identity);

        String lobbyId = lobbyService.removePlayer(player);
        if (lobbyId != null) {
            sendChatPlayerNotification(lobbyId, player.getUsername() + " left the lobby.", player.getUsername());
            sendToLobby(lobbyId, "/queue/lobby/","/lobby-state", this.lobbyService.getLobbyState(lobbyId));
        }
    }

    protected boolean checkSender(String identity, String lobbyId) {
        Player toCheck = this.playerRepository.findByIdentity(identity);
        return toCheck.getLobbyId().equals(lobbyId);
    }

    protected void sendChatNotification(String lobbyId, String message) {
        ChatDTO chat = new ChatDTO();
        chat.setType("event");
        chat.setMessage(message);
        sendToLobby(lobbyId, "/topic/lobby/", "/chat", chat);
    }

    protected void sendChatPlayerNotification(String lobbyId, String message, String username) {
        ChatDTO chat = new ChatDTO();
        chat.setType("event");
        chat.setMessage(message);
        chat.setIcon("avatar:" + username);
        sendToLobby(lobbyId, "/queue/lobby/", "/chat", chat);
    }

    protected void sendToLobby(String lobbyId, String base, String destination, Object dto) {
        List<Player> lobby = this.playerRepository.findByLobbyId(lobbyId);
        /*simp.convertAndSendToUser(lobby.get(0).getIdentity(),
                base + lobbyId + destination, dto);
        */
        for (Player player : lobby) {
            System.out.println(player.getUsername() + "  " + destination);
            simp.convertAndSendToUser(player.getIdentity(),
                    base + lobbyId + destination, dto);
        }
    }
}
