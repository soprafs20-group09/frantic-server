package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.DrawDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.*;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().playCard(identity, play.getIndex());
        }
    }

    public void drawCard(String lobbyId, String identity) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().currentPlayerDrawCard(identity);
        }
    }

    public void exchange(String lobbyId, String identity, ExchangeDTO dto) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().storeExchangeAction(identity, dto.getCards(), dto.getTarget());
        }
    }

    public void gift(String lobbyId, String identity, GiftDTO dto) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().storeGiftAction(identity, dto.getCards(), dto.getTarget());
        }
    }

    public void skip(String lobbyId, String identity, SkipDTO dto) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().storeSkipAction(identity, dto.getTarget());
        }
    }

    public void fantastic(String lobbyId, String identity, FantasticDTO dto) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().storeFantasticAction(identity, dto.getNumber(), dto.getColor());
        }
    }

    public void fantasticFour(String lobbyId, String identity, FantasticFourDTO dto) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().storeFantasticFourAction(identity, dto.getNumber(),
                    dto.getColor(), dto.getPlayers());
        }
    }

    public void equality(String lobbyId, String identity, EqualityDTO dto) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().storeEqualityAction(identity, dto.getColor(), dto.getTarget());
        }
    }

    public void endTurn(String lobbyId, String identity) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().finishTurn();
        }
    }

    public void sendChatEventMessage(String lobbyId, String message) {
        webSocketService.sendChatEventMessage(lobbyId, message);
    }

    public void sendChatPlayerMessage(String lobbyId, String message, String username) {
        webSocketService.sendChatPlayerMessage(lobbyId, message, username);
    }

    public void sendStartGame(String lobbyId) {
        webSocketService.sendToLobby(lobbyId, "/start-game");
    }

    public void sendStartGameRound(String lobbyId) {
        webSocketService.sendToLobby(lobbyId, "/start-round");
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
            cards[i] = cardToDTO(player.peekCard(i));
        }
        hand.setCards(cards);
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/hand", hand);
    }

    public void sendStartTurn(String lobbyId, String currentPlayer, int time, int turn) {
        StartTurnDTO start = new StartTurnDTO();
        start.setCurrentPlayer(currentPlayer);
        start.setTime(time);
        start.setTurn(turn);
        webSocketService.sendToLobby(lobbyId, "/start-turn", start);
    }

    public void sendPlayableCards(String lobbyId, Player player, int[] playable) {
        PlayableCardsDTO playableCards = new PlayableCardsDTO();
        playableCards.setPlayable(playable);
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/playable-cards", playableCards);
    }

    public void sendDrawAnimation(String lobbyId, int amount) {
        DrawDTO dto = new DrawDTO();
        dto.setAmount(amount);
        webSocketService.sendToLobby(lobbyId, "/draw", dto);
    }

    public void sendActionResponse(String lobbyId, Player player, Card card) {
        ActionResponseDTO dto = new ActionResponseDTO();
        dto.setAction(FranticUtils.getStringRepresentation(card.getValue()));
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/action-response", dto);
    }

    public void sendEndRound(String lobbyId, List<Player> players) {
        EndRoundDTO dto = new EndRoundDTO();
        dto.setPlayers(generatePlayerScoreDTO(players));
        webSocketService.sendToLobby(lobbyId, "/end-round", dto);
    }

    public void sendEndGame(String lobbyId, List<Player> players) {
        EndGameDTO dto = new EndGameDTO();
        dto.setPlayers(generatePlayerScoreDTO(players));
        webSocketService.sendToLobby(lobbyId, "/end-game", dto);
    }

    private CardDTO cardToDTO(Card card) {
        CardDTO dto = new CardDTO();
        dto.setColor(FranticUtils.getStringRepresentation(card.getColor()));
        dto.setKey(card.getKey());
        dto.setType(FranticUtils.getStringRepresentation(card.getType()));
        dto.setValue(FranticUtils.getStringRepresentation(card.getValue()));
        return dto;
    }

    private CardDTO[] generateCardBackDTO(Player player) {
        CardDTO[] dto = new CardDTO[player.getHandSize()];
        for (int i = 0; i < player.getHandSize(); i++) {
            dto[i] = new CardDTO();
            dto[i].setType("back");
            dto[i].setKey(player.peekCard(i).getKey());
        }
        return dto;
    }

    private PlayerStateDTO[] playersToDTO(List<Player> players) {
        PlayerStateDTO[] dto = new PlayerStateDTO[players.size()];
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            dto[i] = new PlayerStateDTO();
            dto[i].setUsername(player.getUsername());
            dto[i].setPoints(player.getPoints());
            dto[i].setSkipped(player.isBlocked());
            dto[i].setCards(generateCardBackDTO(player));
        }
        return dto;
    }

    private Map<String, Integer> generatePlayerScoreDTO(List<Player> players) {
        Map<String, Integer> dto = new HashMap<>();
        for (Player player : players) {
            dto.put(player.getUsername(), player.getPoints());
        }
        return dto;
    }
}
