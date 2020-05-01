package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.entity.events.Event;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.DrawDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.*;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.*;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.RecessionAmountDTO;
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

    private static GameService instance;
    private final WebSocketService webSocketService;
    private final PlayerRepository playerRepository;
    private final LobbyRepository lobbyRepository;

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
        if (webSocketService.checkSender(lobbyId, identity) && playerRepository.findByIdentity(identity).isAdmin()) {
            Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
            sendStartGame(lobbyId);
            lobby.startGame();
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
                    dto.getColor(), dto.getTargets());
        }
    }

    public void equality(String lobbyId, String identity, EqualityDTO dto) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().storeEqualityAction(identity, dto.getColor(), dto.getTarget());
        }
    }

    public void counterAttack(String lobbyId, String identity, CounterAttackDTO dto) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().storeCounterAttackAction(identity, dto.getColor());
        }
    }

    public void niceTry(String lobbyId, String identity, NiceTryDTO dto) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().storeNiceTryAction(identity, dto.getColor());
        }
    }

    public void recession(String lobbyId, String identity, RecessionDTO dto) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().performRecession(identity, dto.getCards());
        }
    }

    public void market(String lobbyId, String identity, MarketDTO dto) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            //game.getCurrentGameRound();
        }
    }

    public void endTurn(String lobbyId, String identity) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().playerFinishesTurn(identity);
        }
    }

    public void sendChatMessage(String lobbyId, Chat chat) {
        webSocketService.sendChatMessage(lobbyId, chat);
    }

    public void sendChatMessage(String lobbyId, List<Chat> chat) {
        webSocketService.sendChatMessage(lobbyId, chat);
    }

    public void sendStartGame(String lobbyId) {
        webSocketService.sendToLobby(lobbyId, "/start-game");
    }

    public void sendStartGameRound(String lobbyId) {
        webSocketService.sendToLobby(lobbyId, "/start-round");
    }

    public void sendGameState(String lobbyId, Card discardPile, List<Player> players) {
        this.sendGameState(lobbyId, discardPile, players, false);
    }

    public void sendGameState(String lobbyId, Card discardPile, List<Player> players, boolean up) {
        GameStateDTO dto = new GameStateDTO();
        dto.setDiscardPile(cardToDTO(discardPile));
        dto.setPlayers(playersToDTO(players, up));
        webSocketService.sendToLobby(lobbyId, "/game-state", dto);
    }

    public void sendHand(String lobbyId, Player player) {
        HandDTO dto = new HandDTO();
        CardDTO[] cards = new CardDTO[player.getHandSize()];
        for (int i = 0; i < player.getHandSize(); i++) {
            cards[i] = cardToDTO(player.peekCard(i));
        }
        dto.setCards(cards);
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/hand", dto);
    }

    public void sendStartTurn(String lobbyId, String currentPlayer, int time, int turn) {
        sendStartTurn(lobbyId, currentPlayer, time, turn, 0);
    }

    public void sendStartTurn(String lobbyId, String currentPlayer, int time, int turn, int timebombRounds) {
        StartTurnDTO dto = new StartTurnDTO();
        dto.setCurrentPlayer(currentPlayer);
        dto.setTime(time);
        dto.setTurn(turn);
        dto.setTimebombRounds(timebombRounds);
        webSocketService.sendToLobby(lobbyId, "/start-turn", dto);
    }

    public void sendPlayableCards(String lobbyId, Player player, int[] playable) {
        PlayableCardsDTO dto = new PlayableCardsDTO();
        dto.setPlayable(playable);
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/playable-cards", dto);
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

    public void sendAttackWindow(String lobbyId, Player player, int[] playable, int time) {
        AttackWindowDTO dto = new AttackWindowDTO();
        dto.setPlayable(playable);
        dto.setTime(time);
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/attack-window", dto);
    }

    public void sendAttackTurn(String lobbyId, String currentPlayer, int time, int turn) {
        AttackTurnDTO dto = new AttackTurnDTO();
        dto.setCurrentPlayer(currentPlayer);
        dto.setTime(time);
        dto.setTurn(turn);
        webSocketService.sendToLobby(lobbyId, "/attack-turn", dto);
    }

    public void sendOverlay(String lobbyId, Player player, String icon, String title, String message, int duration) {
        OverlayDTO dto = new OverlayDTO();
        dto.setIcon(icon);
        dto.setTitle(title);
        dto.setMessage(message);
        dto.setDuration(duration);
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/overlay", dto);
    }

    public void sendEvent(String lobbyId, Event event) {
        EventDTO dto = new EventDTO();
        dto.setEvent(event.getName());
        dto.setMessage(event.getMessage());
        webSocketService.sendToLobby(lobbyId, "/event", dto);
    }

    public void sendRecession(String lobbyId, Player player, int amount) {
        RecessionAmountDTO dto = new RecessionAmountDTO();
        dto.setAmount(amount);
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/recession" , dto);
    }

    public void sendGamblingMan(String lobbyId, Player player, int time, int[] playable) {
        GamblingManWindowDTO dto = new GamblingManWindowDTO();
        dto.setTime(time);
        dto.setPlayable(playable);
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/gambling-man-widndow", dto);
    }

    public void sendMarketWindow(String lobbyId, Player player, int time, List<Card> cards) {
        MarketWindowDTO dto = new MarketWindowDTO();
        dto.setTime(time);
        CardDTO[] cardDTO = new CardDTO[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            cardDTO[i] = cardToDTO(cards.get(i));
        }
        dto.setCards(cardDTO);
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/market-window" , dto);
    }

    public void sendEndRound(String lobbyId, List<Player> players, int pointLimit) {
        EndRoundDTO dto = new EndRoundDTO();
        dto.setPlayers(generatePlayerScoreDTO(players));
        dto.setPointLimit(pointLimit);
        webSocketService.sendToLobby(lobbyId, "/end-round", dto);
    }

    public void sendEndGame(String lobbyId, List<Player> players) {
        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        lobby.setIsPlaying(false);

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

    private CardDTO[] generateCardDTO(Player player, boolean up) {
        CardDTO[] dto = new CardDTO[player.getHandSize()];
        for (int i = 0; i < player.getHandSize(); i++) {
            dto[i] = new CardDTO();
            Card c = player.peekCard(i);
            if (up) {
                dto[i].setType(FranticUtils.getStringRepresentation(c.getType()));
                dto[i].setColor(FranticUtils.getStringRepresentation(c.getColor()));
                dto[i].setValue(FranticUtils.getStringRepresentation(c.getValue()));
            }
            else {
                dto[i].setType("back");
            }
            dto[i].setKey(c.getKey());
        }
        return dto;
    }

    private PlayerStateDTO[] playersToDTO(List<Player> players, boolean up) {
        PlayerStateDTO[] dto = new PlayerStateDTO[players.size()];
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            dto[i] = new PlayerStateDTO();
            dto[i].setUsername(player.getUsername());
            dto[i].setPoints(player.getPoints());
            dto[i].setSkipped(player.isBlocked());
            dto[i].setCards(generateCardDTO(player, up));
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
