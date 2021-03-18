package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.ChatDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.RegisterDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.KickDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.RegisteredDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * - Handles general WebSocket stuff (chat, reconnect)
 * - Provides utility functions for sending packets
 */
@Service
@Transactional
public class WebSocketService {

    Logger log = LoggerFactory.getLogger(WebSocketService.class);

    protected final PlayerRepository playerRepository;
    private final LobbyService lobbyService;
    private final PlayerService playerService;

    private final Map<String, String> reconnectMap = new HashMap<>();
    private final Map<String, Timer> reconnectTimer = new HashMap<>();

    @Autowired
    protected SimpMessagingTemplate simp;

    public WebSocketService(@Qualifier("playerRepository") PlayerRepository playerRepository,
                            @Lazy LobbyService lobbyService, @Lazy PlayerService playerService) {
        this.playerRepository = playerRepository;
        this.lobbyService = lobbyService;
        this.playerService = playerService;
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
            game.getCurrentGameRound().onRoundOver(false);
        }
        else if (message.matches("/kick\\s\\S+")) {
            String toKick = message.split(" ")[1];
            if (this.playerRepository.findByUsernameAndLobbyId(toKick, sender.getLobbyId()) != null) {
                KickDTO simulateDTO = new KickDTO();
                simulateDTO.setUsername(toKick);
                this.lobbyService.kickPlayer(sender.getLobbyId(), sender.getIdentity(), simulateDTO);
            }
        }
    }

    public void sendReconnect(String identity) {
        String token = UUID.randomUUID().toString();
        RegisterDTO dto = new RegisterDTO();
        dto.setToken(token);
        this.reconnectMap.put(token, identity);
        this.sendToPlayer(identity, "/queue/reconnect", dto);
    }

    public synchronized void reconnect(String newIdentity, String token) {
        String oldIdentity = this.reconnectMap.get(token);
        if (oldIdentity != null) {
            Player player = this.playerRepository.findByIdentity(oldIdentity);
            if (player != null) {
                this.reconnectTimer.get(oldIdentity).cancel();

                log.info("Lobby " + player.getLobbyId() + ": Reconnect received from Player " + oldIdentity + " as " + newIdentity);

                RegisteredDTO registeredDTO = this.playerService.registerPlayer(newIdentity, player, player.getLobbyId());
                this.sendToPlayer(newIdentity, "/queue/register", registeredDTO);
                this.reconnectMap.remove(token);
            }
        }
    }

    public boolean isReconnecting(String token) {
        return this.reconnectMap.containsKey(token);
    }

    public void startReconnectTimer(int seconds, String identity) {
        int milliseconds = seconds * 1000;
        this.reconnectTimer.put(identity, new Timer());
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                lobbyService.handleDisconnect(identity);
            }
        };
        this.reconnectTimer.get(identity).schedule(timerTask, milliseconds);
    }

    protected boolean checkSender(String lobbyId, String identity) {
        Player toCheck = this.playerRepository.findByIdentity(identity);
        return toCheck != null && toCheck.getLobbyId().equals(lobbyId);
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

        this.simp.convertAndSendToUser(identity, path, dto);
    }

    protected void sendToPlayerInLobby(String lobbyId, String identity, String destination, Object dto) {

        this.simp.convertAndSendToUser(identity, "/queue/lobby/" + lobbyId + destination, dto);
    }

    protected void sendToLobby(String lobbyId, String destination) {
        sendToLobby(lobbyId, destination, "{}");
    }

    protected void sendToLobby(String lobbyId, String destination, Object dto) {
        List<Player> lobby = this.playerRepository.findByLobbyId(lobbyId);
        for (Player player : lobby) {
            sendToPlayerInLobby(lobbyId, player.getIdentity(), destination, dto);
        }
    }
}
