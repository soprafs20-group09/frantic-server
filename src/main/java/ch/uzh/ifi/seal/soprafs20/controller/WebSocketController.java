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

        long lobbyId = lobbyService.removePlayer(player);
        sendChatNotification(lobbyId, player.getUsername() + " left the lobby!");
    }

    protected boolean checkSender(String identity, long lobbyId) {
        Player toCheck = this.playerRepository.findByIdentity(identity);
        return toCheck.getLobbyId().equals(lobbyId);
    }

    protected void sendChatNotification(long lobbyId, String message) {
        ChatDTO chat = new ChatDTO();
        chat.setType("event");
        chat.setMessage(message);
        sendToLobby(lobbyId, "/topic/lobby/", "/chat", chat);
    }

    protected void sendToLobby(long lobbyId, String base, String destination, Object dto) {
        List<Player> lobby = this.playerRepository.findByLobbyId(lobbyId);
        for (Player player : lobby) {
            simp.convertAndSendToUser(player.getIdentity(),
                    base + lobbyId + destination, dto);
        }
    }
}
