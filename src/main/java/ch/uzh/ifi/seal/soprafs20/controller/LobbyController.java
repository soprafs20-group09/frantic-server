package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.KickDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.LobbySettingsDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import static ch.uzh.ifi.seal.soprafs20.utils.FranticUtils.getIdentity;

/**
 * Provides lobby-related WebSocket endpoints
 */
@Controller
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @MessageMapping("/lobby/{lobbyId}/settings")
    public void changeLobbySettings(@DestinationVariable String lobbyId,
                                    SimpMessageHeaderAccessor sha, LobbySettingsDTO dto) {
        lobbyService.updateLobbySettings(lobbyId, getIdentity(sha), dto);
    }

    @MessageMapping("/lobby/{lobbyId}/kick")
    public void kickPlayer(@DestinationVariable String lobbyId,
                           SimpMessageHeaderAccessor sha, KickDTO dto) {
        lobbyService.kickPlayer(lobbyId, getIdentity(sha), dto);
    }

    @MessageMapping("/lobby/{lobbyId}/rematch")
    public void rematch(@DestinationVariable String lobbyId,
                        SimpMessageHeaderAccessor sha) {
        lobbyService.rematch(lobbyId, getIdentity(sha));
    }
}
