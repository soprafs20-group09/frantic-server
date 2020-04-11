package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.DrawDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.StartGameDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.PlayCardDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GameService {

    private final WebSocketService webSocketService;

    private final PlayerRepository playerRepository;
    private final LobbyRepository lobbyRepository;

    public GameService(WebSocketService webSocketService,
                       @Qualifier("playerRepository") PlayerRepository playerRepository,
                       @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.webSocketService = webSocketService;
        this.playerRepository = playerRepository;
        this.lobbyRepository = lobbyRepository;
    }

    public void startGame(String lobbyId, String identity) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
            sendStartGame(lobbyId);
            lobby.startGame(this);
        }
    }

    public void playCard(String lobbyId, String identity, PlayCardDTO play) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Player player = playerRepository.findByIdentity(identity);
            Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
            Game game = lobby.getGame();
            // game.getGameRound().playCard(player, play.getIndex())
        }
    }

    public void drawCard(String lobbyId, String identity, DrawDTO draw) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Player player = playerRepository.findByIdentity(identity);
            Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
            Game game = lobby.getGame();
            // game.getGameRound().drawCardsFromStack(player, draw.getAmount())
        }
    }

    public void sendStartGame(String lobbyId) {
        webSocketService.sendToLobby(lobbyId, "/start-game", new StartGameDTO());
    }

    public void sendStartGameRound(String lobbyId) {
        webSocketService.sendToLobby(lobbyId, "/start-round", new StartGameRoundDTO());
    }

    public void sendGameState(String lobbyId, Card[] discardPile, List<Player> players) {
        GameStateDTO gameState = new GameStateDTO();
        // transform to DTO
        webSocketService.sendToLobby(lobbyId, "/game-state", gameState);
    }

    public void sendHand(String lobbyId, Player player) {
        HandDTO hand = new HandDTO();
        // transform to DTO
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/hand", hand);
    }

    public void sendStartTurn(String lobbyId, String currentPlayer, int time) {
        StartTurnDTO start = new StartTurnDTO();
        start.setCurrentPlayer(currentPlayer);
        start.setTime(time);
        webSocketService.sendToLobby(lobbyId, "start-turn", start);
    }

    public void sendPlayableCards(String lobbyId, Player player, int[] playable) {
        PlayableCardsDTO playableCards = new PlayableCardsDTO();
        playableCards.setPlayable(playable);
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/playable-cards", playableCards);
    }
}
