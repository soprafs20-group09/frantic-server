package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.service.RegisterService;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.RegisterDTO;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class WebSocketController {

    private final RegisterService registerService;
    private final LobbyService lobbyService;

    public WebSocketController(RegisterService registerService, LobbyService lobbyService) {
        this.registerService = registerService;
        this.lobbyService = lobbyService;
    }

    @MessageMapping("/register")
    public void registerPlayer(SimpMessageHeaderAccessor sha, RegisterDTO registerDTO) throws Exception {

        String identity = sha.getUser().getName();
        registerService.joinLobby(identity, registerDTO);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String identity = event.getUser().getName();
        lobbyService.handleDisconnect(identity);
    }
}
