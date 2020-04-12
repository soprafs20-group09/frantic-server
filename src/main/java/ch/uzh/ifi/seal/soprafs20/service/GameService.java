package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.PlayCardDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static GameService instance;

    @Autowired
    public GameService(WebSocketService webSocketService,
                       @Qualifier("playerRepository") PlayerRepository playerRepository,
                       @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.webSocketService = webSocketService;
        this.playerRepository = playerRepository;
        this.lobbyRepository = lobbyRepository;
        instance = this;
    }

    public static GameService getInstance() {
        return instance;
    }

    public void startGame(String lobbyId, String identity) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            if (playerRepository.findByIdentity(identity).isAdmin()) {
                Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
                sendStartGame(lobbyId);
                lobby.startGame();
            }
        }
    }

    public void playCard(String lobbyId, String identity, PlayCardDTO play) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Player player = playerRepository.findByIdentity(identity);
            Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
            lobby.getGame().getCurrentGameRound().playCard(player, play.getIndex());
        }
    }

    public void drawCard(String lobbyId, String identity) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Player player = playerRepository.findByIdentity(identity);
            Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
            lobby.getGame().getCurrentGameRound().currentPlayerDrawCard();
        }
    }

    public void sendStartGame(String lobbyId) {
        webSocketService.sendToLobby(lobbyId, "/start-game", "{}");
    }

    public void sendStartGameRound(String lobbyId) {
        webSocketService.sendToLobby(lobbyId, "/start-round", "{}");
    }

    public void sendGameState(String lobbyId, Card discardPile, List<Player> players) {
        GameStateDTO gameState = new GameStateDTO();
        gameState.setDiscardPile(cardToDTO(discardPile));
        gameState.setPlayers(playersToDTO(players));
        webSocketService.sendToLobby(lobbyId, "/game-state", gameState);
    }

    public void sendHand(String lobbyId, Player player) {
        HandDTO hand = new HandDTO();
        CardDTO[] cards = new CardDTO[player.getHandSize()];
        for (int i = 0; i < player.getHandSize(); i++) {
            Card card = player.peekCard(i);
            cards[i] = cardToDTO(card);
        }
        hand.setCards(cards);
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

    private CardDTO cardToDTO(Card card) {
        CardDTO response = new CardDTO();
        response.setColor(FranticUtils.getStringRepresentation(card.getColor()));
        response.setKey(card.getKey());
        response.setType(FranticUtils.getStringRepresentation(card.getType()));
        response.setValue(FranticUtils.getStringRepresentation(card.getValue()));
        return response;
    }

    private CardDTO[] generateCardBackDTO(int n) {
        CardDTO[] response = new CardDTO[n];
        for (int i = 0; i < n; i++) {
            response[i] = new CardDTO();
            response[i].setType("back");
        }
        return response;
    }

    private PlayerStateDTO[] playersToDTO(List<Player> players) {
        PlayerStateDTO[] response = new PlayerStateDTO[players.size()];
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            response[i] = new PlayerStateDTO();
            response[i].setUsername(p.getUsername());
            response[i].setPoints(p.getPoints());
            response[i].setSkipped(p.isBlocked());
            response[i].setCards(generateCardBackDTO(p.getHandSize()));
        }
        return response;
    }
}
