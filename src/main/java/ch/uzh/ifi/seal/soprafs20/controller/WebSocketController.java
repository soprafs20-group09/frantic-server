package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.service.RegisterService;
import ch.uzh.ifi.seal.soprafs20.service.WebSocketService;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.ChatDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.RegisterDTO;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static ch.uzh.ifi.seal.soprafs20.utils.FranticUtils.getIdentity;

@Controller
public class WebSocketController {

    private final RegisterService registerService;
    private final LobbyService lobbyService;
    private final WebSocketService webSocketService;
    private final Map<String, Timer> reconnectWindow;

    public WebSocketController(RegisterService registerService, LobbyService lobbyService, WebSocketService webSocketService) {
        this.registerService = registerService;
        this.lobbyService = lobbyService;
        this.webSocketService = webSocketService;
        this.reconnectWindow = new HashMap<>();
    }

    @MessageMapping("/register")
    public synchronized void registerPlayer(SimpMessageHeaderAccessor sha, RegisterDTO dto) {
        if (this.reconnectWindow.get(getIdentity(sha)) == null) {
            registerService.joinLobby(getIdentity(sha), dto);
        }
        else {
            this.reconnectWindow.get(getIdentity(sha)).cancel();
            webSocketService.reconnect(getIdentity(sha), dto);
        }
    }

    @MessageMapping("/lobby/{lobbyId}/chat")
    public void newChatMessage(@DestinationVariable String lobbyId,
                               SimpMessageHeaderAccessor sha, ChatDTO dto) {
        webSocketService.sendChatMessage(lobbyId, getIdentity(sha), dto);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        Principal p = event.getUser();
        if (p != null) {
            startReconnectTimer(2, p.getName());
        }
    }

    private void startReconnectTimer(int seconds, String identity) {
        int milliseconds = seconds * 1000;
        this.reconnectWindow.put(identity, new Timer());
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                lobbyService.handleDisconnect(identity);
            }
        };
        this.reconnectWindow.get(identity).schedule(timerTask, milliseconds);
    }
}
