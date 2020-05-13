package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.ChatDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.KickDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.DisconnectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WebSocketService {

    protected final PlayerRepository playerRepository;
    private final LobbyService lobbyService;
    @Autowired
    protected SimpMessagingTemplate simp;

    public WebSocketService(@Qualifier("playerRepository") PlayerRepository playerRepository, @Lazy LobbyService lobbyService) {
        this.playerRepository = playerRepository;
        this.lobbyService = lobbyService;
    }

    public void sendChatMessage(String lobbyId, String identity, ChatDTO dto) {

        if (this.checkSender(lobbyId, identity)) {
            if (dto.getMessage() != null && !dto.getMessage().matches("^\\s*$")) {
                Player sender = this.playerRepository.findByIdentity(identity);
                if (sender.isAdmin() && dto.getMessage().matches("^/.*")) {
                    parseGameCommand(sender, dto.getMessage());
                }
                else {
                    dto.setType("msg");
                    dto.setUsername(sender.getUsername());

                    this.sendToLobby(lobbyId, "/chat", dto);
                }
            }
        }
    }

    private void parseGameCommand(Player sender, String message) {
        if (message.equals("/end")) {
            Game game = GameRepository.findByLobbyId(sender.getLobbyId());
            game.getCurrentGameRound().onRoundOver();
        }
        else if (message.matches("/kick\\s\\S+")) {
            String toKick = message.split(" ")[1];
            if (this.playerRepository.findByUsernameAndLobbyId(toKick, sender.getLobbyId()) != null) {
                KickDTO simulateDTO = new KickDTO();
                simulateDTO.setUsername(toKick);
                lobbyService.kickPlayer(sender.getLobbyId(), sender.getIdentity(), simulateDTO);
            }
        }
    }

    protected boolean checkSender(String lobbyId, String identity) {
        Player toCheck = playerRepository.findByIdentity(identity);
        return toCheck.getLobbyId().equals(lobbyId);
    }

    protected void sendChatMessage(String lobbyId, List<Chat> chats) {
        for (Chat chat : chats) {
            sendChatMessage(lobbyId, chat);
        }
    }

    protected void sendChatMessage(String lobbyId, Chat chat) {
        ChatDTO dto = new ChatDTO(chat.getType(), chat.getUsername(), chat.getIcon(), chat.getMessage());
        sendToLobby(lobbyId, "/chat", dto);
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
