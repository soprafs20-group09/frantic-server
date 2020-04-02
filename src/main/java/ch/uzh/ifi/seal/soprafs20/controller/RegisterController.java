package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.RegisterDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.RegisteredDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class RegisterController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final LobbyService lobbyService;
    private final PlayerService playerService;

    RegisterController(LobbyService lobbyService, PlayerService playerService) {
        this.lobbyService = lobbyService;
        this.playerService = playerService;
    }

    @MessageMapping("/register")
    public void registerPlayer(SimpMessageHeaderAccessor sha, RegisterDTO registerDTO) throws Exception {

        String identity = sha.getUser().getName();
        RegisteredDTO registeredDTO = playerService.registerPlayer(identity, registerDTO);

        simpMessagingTemplate.convertAndSendToUser(
                identity, "/queue/register", registeredDTO);
    }
}
