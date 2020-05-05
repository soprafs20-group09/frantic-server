package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.service.RegisterService;
import ch.uzh.ifi.seal.soprafs20.service.WebSocketService;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.ChatDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.RegisterDTO;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

import static ch.uzh.ifi.seal.soprafs20.utils.FranticUtils.getIdentity;

@Controller
public class WebSocketController {

    private final RegisterService registerService;
    private final LobbyService lobbyService;
    private final WebSocketService webSocketService;

    public WebSocketController(RegisterService registerService, LobbyService lobbyService, WebSocketService webSocketService) {
        this.registerService = registerService;
        this.lobbyService = lobbyService;
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/register")
    public synchronized void registerPlayer(SimpMessageHeaderAccessor sha, RegisterDTO dto) throws Exception {
        registerService.joinLobby(getIdentity(sha), dto);
    }

    @MessageMapping("/lobby/{lobbyId}/chat")
    public void newChatMessage(@DestinationVariable String lobbyId,
                               SimpMessageHeaderAccessor sha, ChatDTO dto) {
        webSocketService.sendChatMessage(lobbyId, getIdentity(sha), dto);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) throws InterruptedException {
        Principal p = event.getUser();
        if (p != null) {
            Thread.sleep(50);
            lobbyService.handleDisconnect(p.getName());
        }
    }
}
