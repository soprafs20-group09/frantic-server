package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.ChatDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.KickDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.LobbySettingsDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @MessageMapping("/lobby/{lobbyId}/settings")
    public void changeLobbySettings(@DestinationVariable String lobbyId,
                                    SimpMessageHeaderAccessor sha, LobbySettingsDTO lobbySettingsDTO) throws Exception {
        String identity = sha.getUser().getName();
        lobbyService.updateLobbySettings(lobbyId, identity, lobbySettingsDTO);
    }

    @MessageMapping("/lobby/{lobbyId}/kick")
    public void kickPlayer(@DestinationVariable String lobbyId,
                           SimpMessageHeaderAccessor sha, KickDTO kickDTO) throws Exception {
        String identity = sha.getUser().getName();
        lobbyService.kickPlayer(lobbyId, identity, kickDTO);
    }

    @MessageMapping("/lobby/{lobbyId}/chat")
    public void newChatMessage(@DestinationVariable String lobbyId,
                               SimpMessageHeaderAccessor sha, ChatDTO chatDTO) throws Exception {
        String identity = sha.getUser().getName();
        lobbyService.sendChatMessage(lobbyId, identity, chatDTO);
    }
}
