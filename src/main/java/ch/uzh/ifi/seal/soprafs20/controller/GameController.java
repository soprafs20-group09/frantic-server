package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.DrawDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.StartGameDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.PlayCardDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @MessageMapping("/lobby/{lobbyId}/start-game")
    public void startGame(@DestinationVariable String lobbyId,
                                    SimpMessageHeaderAccessor sha, StartGameDTO startGameDTO) throws Exception {
        String identity = sha.getUser().getName();
        gameService.startGame(lobbyId, identity);
    }

    @MessageMapping("/lobby/{lobbyId}/play")
    public void playCard(@DestinationVariable String lobbyId,
                           SimpMessageHeaderAccessor sha, PlayCardDTO playCardDTO) throws Exception {
        String identity = sha.getUser().getName();
        gameService.playCard(lobbyId, identity, playCardDTO);
    }

    @MessageMapping("/lobby/{lobbyId}/draw")
    public void newChatMessage(@DestinationVariable String lobbyId,
                               SimpMessageHeaderAccessor sha, DrawDTO drawDTO) throws Exception {
        String identity = sha.getUser().getName();
        gameService.drawCard(lobbyId, identity, drawDTO);
    }
}
