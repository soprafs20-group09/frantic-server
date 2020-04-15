package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.ChatDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.DisconnectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WebSocketService {

    @Autowired
    protected SimpMessagingTemplate simp;

    protected final PlayerRepository playerRepository;

    public WebSocketService(@Qualifier("playerRepository") PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    protected boolean checkSender(String lobbyId, String identity) {
        Player toCheck = playerRepository.findByIdentity(identity);
        return toCheck.getLobbyId().equals(lobbyId);
    }

    protected void sendChatEventMessage(String lobbyId, String message) {
        ChatDTO chat = new ChatDTO();
        chat.setType("event");
        chat.setMessage(message);
        sendToLobby(lobbyId, "/chat", chat);
    }

    protected void sendChatPlayerMessage(String lobbyId, String message, String username) {
        ChatDTO chat = new ChatDTO();
        chat.setType("event");
        chat.setMessage(message);
        chat.setIcon("avatar:" + username);
        sendToLobby(lobbyId, "/chat", chat);
    }

    protected void sendToPlayer(String identity, String path, Object dto) {

        simp.convertAndSendToUser(identity, path, dto);
    }

    protected void sendToPlayerInLobby(String lobbyId, String identity, String destination, Object dto) {

        simp.convertAndSendToUser(identity, "/queue/lobby/" + lobbyId + destination, dto);
    }

    protected void sendToLobby(String lobbyId, String destination) {
        sendToLobby(lobbyId, destination, "{}");
    }

    protected void sendToLobby(String lobbyId, String destination, Object dto) {
        List<Player> lobby = playerRepository.findByLobbyId(lobbyId);
        for (Player player : lobby) {
            sendToPlayerInLobby(lobbyId, player.getIdentity(), destination, dto);
        }
    }

    protected void sendDisconnectToLobby(String lobbyId, DisconnectDTO message) {
        List<Player> lobby = playerRepository.findByLobbyId(lobbyId);
        for (Player player : lobby) {
            simp.convertAndSendToUser(player.getIdentity(),
                    "/queue/disconnect", message);
        }
    }
}
