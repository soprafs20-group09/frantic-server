package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.service.RegisterService;
import ch.uzh.ifi.seal.soprafs20.service.WebSocketService;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.ChatDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.RegisterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

import static ch.uzh.ifi.seal.soprafs20.utils.FranticUtils.getIdentity;

/**
 * Provides general WebSocket endpoints and handles session disconnects
 */
@Controller
public class WebSocketController {

    Logger log = LoggerFactory.getLogger(WebSocketController.class);

    private final RegisterService registerService;
    private final WebSocketService webSocketService;

    public WebSocketController(RegisterService registerService, WebSocketService webSocketService) {
        this.registerService = registerService;
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/register")
    public synchronized void registerPlayer(SimpMessageHeaderAccessor sha, RegisterDTO dto) {
        log.info("Player " + getIdentity(sha) + ": Connection established");

        if (this.webSocketService.isReconnecting(dto.getToken())) {
            webSocketService.reconnect(getIdentity(sha), dto.getToken());
        }
        else {
            registerService.joinLobby(getIdentity(sha), dto);
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
            log.info("Player " + p.getName() + ": Connection lost");

            this.webSocketService.startReconnectTimer(2, p.getName());
        }
    }
}
