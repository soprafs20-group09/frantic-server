package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import static ch.uzh.ifi.seal.soprafs20.utils.FranticUtils.getIdentity;

/**
 * Provides game-related WebSocket endpoints
 */
@Controller
public class GameController {

    Logger log = LoggerFactory.getLogger(GameController.class);

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @MessageMapping("/lobby/{lobbyId}/start-game")
    public void startGame(@DestinationVariable String lobbyId,
                          SimpMessageHeaderAccessor sha) {
        log.info("Lobby " + lobbyId + ": Game started");

        gameService.startGame(lobbyId, getIdentity(sha));
    }

    @MessageMapping("/lobby/{lobbyId}/play")
    public void playCard(@DestinationVariable String lobbyId,
                         SimpMessageHeaderAccessor sha, PlayCardDTO dto) {
        gameService.playCard(lobbyId, getIdentity(sha), dto);
    }

    @MessageMapping("/lobby/{lobbyId}/draw")
    public void newChatMessage(@DestinationVariable String lobbyId,
                               SimpMessageHeaderAccessor sha) {
        gameService.drawCard(lobbyId, getIdentity(sha));
    }

    @MessageMapping("/lobby/{lobbyId}/end-turn")
    public void endTurn(@DestinationVariable String lobbyId,
                        SimpMessageHeaderAccessor sha) {
        gameService.endTurn(lobbyId, getIdentity(sha));
    }

    @MessageMapping("/lobby/{lobbyId}/action/exchange")
    public void exchange(@DestinationVariable String lobbyId,
                         SimpMessageHeaderAccessor sha, ExchangeDTO dto) {
        gameService.exchange(lobbyId, getIdentity(sha), dto);
    }

    @MessageMapping("/lobby/{lobbyId}/action/gift")
    public void gift(@DestinationVariable String lobbyId,
                     SimpMessageHeaderAccessor sha, GiftDTO dto) {
        gameService.gift(lobbyId, getIdentity(sha), dto);
    }

    @MessageMapping("/lobby/{lobbyId}/action/skip")
    public void skip(@DestinationVariable String lobbyId,
                     SimpMessageHeaderAccessor sha, SkipDTO dto) {
        gameService.skip(lobbyId, getIdentity(sha), dto);
    }

    @MessageMapping("/lobby/{lobbyId}/action/fantastic")
    public void fantastic(@DestinationVariable String lobbyId,
                          SimpMessageHeaderAccessor sha, FantasticDTO dto) {
        gameService.fantastic(lobbyId, getIdentity(sha), dto);
    }

    @MessageMapping("/lobby/{lobbyId}/action/fantastic-four")
    public void fantasticFour(@DestinationVariable String lobbyId,
                              SimpMessageHeaderAccessor sha, FantasticFourDTO dto) {
        gameService.fantasticFour(lobbyId, getIdentity(sha), dto);
    }

    @MessageMapping("/lobby/{lobbyId}/action/equality")
    public void equality(@DestinationVariable String lobbyId,
                         SimpMessageHeaderAccessor sha, EqualityDTO dto) {
        gameService.equality(lobbyId, getIdentity(sha), dto);
    }

    @MessageMapping("/lobby/{lobbyId}/action/counterattack")
    public void counterAttack(@DestinationVariable String lobbyId,
                              SimpMessageHeaderAccessor sha, CounterAttackDTO dto) {
        gameService.counterAttack(lobbyId, getIdentity(sha), dto);
    }

    @MessageMapping("/lobby/{lobbyId}/action/nice-try")
    public void niceTry(@DestinationVariable String lobbyId,
                        SimpMessageHeaderAccessor sha, NiceTryDTO dto) {
        gameService.niceTry(lobbyId, getIdentity(sha), dto);
    }

    @MessageMapping("/lobby/{lobbyId}/action/surprise-party")
    public void surpriseParty(@DestinationVariable String lobbyId,
                              SimpMessageHeaderAccessor sha, SurprisePartyDTO dto) {
        gameService.surpriseParty(lobbyId, getIdentity(sha), dto);
    }

    @MessageMapping("/lobby/{lobbyId}/action/merry-christmas")
    public void merryChristmas(@DestinationVariable String lobbyId,
                               SimpMessageHeaderAccessor sha, MerryChristmasDTO dto) {
        gameService.merryChristmas(lobbyId, getIdentity(sha), dto);
    }

    @MessageMapping("/lobby/{lobbyId}/action/recession")
    public void recession(@DestinationVariable String lobbyId,
                          SimpMessageHeaderAccessor sha, RecessionDTO dto) {
        gameService.recession(lobbyId, getIdentity(sha), dto);
    }

    @MessageMapping("/lobby/{lobbyId}/action/market")
    public void market(@DestinationVariable String lobbyId,
                       SimpMessageHeaderAccessor sha, MarketDTO dto) {
        gameService.market(lobbyId, getIdentity(sha), dto);
    }

    @MessageMapping("/lobby/{lobbyId}/action/gambling-man")
    public void gamblingMan(@DestinationVariable String lobbyId,
                            SimpMessageHeaderAccessor sha, GamblingManDTO dto) {
        gameService.gamblingMan(lobbyId, getIdentity(sha), dto);
    }
}
