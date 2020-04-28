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
        String identity = sha.getUser().getName();
        registerService.joinLobby(identity, dto);
    }

    @MessageMapping("/lobby/{lobbyId}/chat")
    public void newChatMessage(@DestinationVariable String lobbyId,
                               SimpMessageHeaderAccessor sha, ChatDTO dto) {
        String identity = sha.getUser().getName();
        webSocketService.sendChatMessage(lobbyId, identity, dto);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) throws InterruptedException {
        String identity = event.getUser().getName();
        Thread.sleep(50);
        lobbyService.handleDisconnect(identity);
    }
}
