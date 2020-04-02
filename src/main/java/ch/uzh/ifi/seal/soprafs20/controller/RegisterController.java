package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.exceptions.PlayerServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.RegisterDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.LobbyStateDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.RegisteredDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class RegisterController extends WebSocketController {

    public RegisterController(LobbyService lobbyService, PlayerService playerService, @Qualifier("playerRepository") PlayerRepository playerRepository,
                           @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        super(lobbyService, playerService, playerRepository, lobbyRepository);
    }

    @MessageMapping("/register")
    public void registerPlayer(SimpMessageHeaderAccessor sha, RegisterDTO registerDTO) throws Exception {

        String identity = sha.getUser().getName();
        String username = checkAuthentication(registerDTO.getToken());
        Player player = playerService.createPlayer(username);

        Long lobbyId = RESTController.getLobbyIdFromAuthToken(registerDTO.getToken());
        if (lobbyId == null) {
            lobbyId = lobbyService.createLobby(player);
        } else {
            lobbyService.joinLobby(lobbyId, player);
        }
        RegisteredDTO registeredDTO = playerService.registerPlayer(identity, player, lobbyId);
        RESTController.removeFromAuthMap(registerDTO.getToken());

        simp.convertAndSendToUser(
                identity, "/queue/register", registeredDTO);
        // wait for player to subscribe to channels
        Thread.sleep(500);
        // send initial lobby-state packet
        LobbyStateDTO lobbyStateDTO = lobbyService.getLobbyState(lobbyId);
        sendToLobby(lobbyId, "/topic/lobby", "/lobby-state", lobbyStateDTO);
    }

    private String checkAuthentication(String authToken) {
        String username = RESTController.getUsernameFromAuthToken(authToken);
        if (username == null) {
            throw new PlayerServiceException("Player not authenticated.");
        }
        return username;
    }
}
