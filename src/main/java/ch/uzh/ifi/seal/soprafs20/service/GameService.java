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
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.CardDTO;
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

    public void surpriseParty(String lobbyId, String identity, SurprisePartyDTO dto) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().prepareSurpriseParty(identity, dto.getCard(), dto.getTarget());
        }
    }

    public void merryChristmas(String lobbyId, String identity, MerryChristmasDTO dto) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().prepareMerryChristmas(identity, dto.getTargets());
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
            game.getCurrentGameRound().prepareMarket(identity, dto.getCard());
        }
    }

    public void gamblingMan(String lobbyId, String identity, GamblingManDTO dto) {
        if (webSocketService.checkSender(lobbyId, identity)) {
            Game game = GameRepository.findByLobbyId(lobbyId);
            game.getCurrentGameRound().prepareGamblingMan(identity, dto.getCard());
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

    public void sendGameState(String lobbyId, Card discardPile, List<Player> players, boolean up) {
        GameStateDTO dto = new GameStateDTO(cardToDTO(discardPile), playersToDTO(players, up));
        webSocketService.sendToLobby(lobbyId, "/game-state", dto);
    }

    public void sendHand(String lobbyId, Player player) {
        CardDTO[] cards = new CardDTO[player.getHandSize()];
        for (int i = 0; i < player.getHandSize(); i++) {
            cards[i] = cardToDTO(player.peekCard(i));
        }
        HandDTO dto = new HandDTO(cards);
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/hand", dto);
    }

    public void sendStartTurn(String lobbyId, String currentPlayer) {
        sendStartTurn(lobbyId, currentPlayer, 0);
    }

    public void sendStartTurn(String lobbyId, String currentPlayer, int timebombRounds) {
        StartTurnDTO dto = new StartTurnDTO(currentPlayer, timebombRounds);
        webSocketService.sendToLobby(lobbyId, "/start-turn", dto);
    }

    public void sendPlayable(String lobbyId, Player player, int[] cards, boolean canDraw, boolean canEnd) {
        PlayableDTO dto = new PlayableDTO(cards, canDraw, canEnd);
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/playable", dto);
    }

    public void sendTimer(String lobbyId, int seconds) {
        TimerDTO dto = new TimerDTO(seconds);
        webSocketService.sendToLobby(lobbyId, "/timer", dto);
    }

    public void sendAnimationSpeed(String lobbyId, int hand) {
        AnimationSpeedDTO dto = new AnimationSpeedDTO(hand);
        webSocketService.sendToLobby(lobbyId, "/animation-speed", dto);
    }

    public void sendDrawAnimation(String lobbyId, int amount) {
        DrawDTO dto = new DrawDTO(amount);
        webSocketService.sendToLobby(lobbyId, "/draw", dto);
    }

    public void sendActionResponse(String lobbyId, Player player, Card card) {
        ActionResponseDTO dto = new ActionResponseDTO(FranticUtils.getStringRepresentation(card.getValue()));
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/action-response", dto);
    }

    public void sendEventActionResponse(String lobbyId, String event) {
        ActionResponseDTO dto = new ActionResponseDTO(event);
        webSocketService.sendToLobby(lobbyId, "/action-response", dto);
    }

    public void sendAttackTurn(String lobbyId, String currentPlayer) {
        AttackTurnDTO dto = new AttackTurnDTO(currentPlayer);
        webSocketService.sendToLobby(lobbyId, "/attack-turn", dto);
    }

    public void sendOverlay(String lobbyId, Player player, String icon, String title, String message, int duration) {
        OverlayDTO dto = new OverlayDTO(icon, title, message, duration);
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/overlay", dto);
    }

    public void sendEvent(String lobbyId, Event event) {
        EventDTO dto = new EventDTO(event);
        webSocketService.sendToLobby(lobbyId, "/event", dto);
    }

    public void sendRecession(String lobbyId, Player player, int amount) {
        RecessionAmountDTO dto = new RecessionAmountDTO(amount);
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/recession" , dto);
    }

    public void sendGamblingMan(String lobbyId, Player player, int[] playable) {
        GamblingManWindowDTO dto = new GamblingManWindowDTO(playable);
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/gambling-man-window", dto);
    }

    public void sendMarketWindow(String lobbyId, Player player, List<Card> cards) {
        CardDTO[] cardDTO = new CardDTO[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            cardDTO[i] = cardToDTO(cards.get(i));
        }
        MarketWindowDTO dto = new MarketWindowDTO(cardDTO);
        webSocketService.sendToPlayerInLobby(lobbyId, player.getIdentity(), "/market-window" , dto);
    }

    public void sendEndRound(String lobbyId, List<Player> players, int pointLimit) {
        EndRoundDTO dto = new EndRoundDTO(generatePlayerScoreDTO(players), pointLimit);
        webSocketService.sendToLobby(lobbyId, "/end-round", dto);
    }

    public void sendEndGame(String lobbyId, List<Player> players) {
        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        lobby.setIsPlaying(false);

        EndGameDTO dto = new EndGameDTO(generatePlayerScoreDTO(players));
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
