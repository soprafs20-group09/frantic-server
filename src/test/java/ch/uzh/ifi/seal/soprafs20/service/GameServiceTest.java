package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.entity.events.FridayTheThirteenthEvent;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameServiceTest {

    @Mock
    WebSocketService webSocketService;
    @Mock
    PlayerRepository playerRepository;
    @Mock
    LobbyRepository lobbyRepository;
    @Mock
    Lobby lobby;
    @Mock
    Player player;
    @Mock
    Game game;
    @Mock
    GameRound gameRound;
    @Mock
    Card card;
    @InjectMocks
    GameService gameService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GameRepository.addGame("testLobbyId", game);

        //when
        Mockito.when(webSocketService.checkSender(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(game.getCurrentGameRound()).thenReturn(gameRound);
        Mockito.when(player.getIdentity()).thenReturn("testIdentity");
    }

    @AfterEach
    public void teardown() {
        GameRepository.removeGame("testLobbyId");
    }

    @Test
    public void startGame_notAdmin_notStart() {
        Mockito.when(playerRepository.findByIdentity(Mockito.anyString())).thenReturn(player);
        Mockito.when(player.isAdmin()).thenReturn(false);
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyString())).thenReturn(lobby);

        gameService.startGame("testLobbyId", "testIdentity");

        Mockito.verify(lobby, Mockito.times(0)).startGame();
    }

    @Test
    public void startGame_success() {
        Mockito.when(playerRepository.findByIdentity(Mockito.anyString())).thenReturn(player);
        Mockito.when(player.isAdmin()).thenReturn(true);
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyString())).thenReturn(lobby);

        gameService.startGame("testLobbyId", "testIdentity");

        Mockito.verify(lobby, Mockito.times(1)).startGame();
    }

    @Test
    public void playCardTest() {
        PlayCardDTO playCardDTO = Mockito.mock(PlayCardDTO.class);
        Mockito.when(playCardDTO.getIndex()).thenReturn(5);

        gameService.playCard("testLobbyId", "testIdentity", playCardDTO);

        Mockito.verify(gameRound, Mockito.times(1)).playCard("testIdentity", 5);
    }

    @Test
    public void drawCardTest() {
        gameService.drawCard("testLobbyId", "testIdentity");

        Mockito.verify(gameRound, Mockito.times(1)).currentPlayerDrawCard("testIdentity");
    }

    @Test
    public void exchangeActionTest() {
        ExchangeDTO exchangeDTO = Mockito.mock(ExchangeDTO.class);
        Mockito.when(exchangeDTO.getCards()).thenReturn(new int[]{1,2});
        Mockito.when(exchangeDTO.getTarget()).thenReturn("testTarget");

        gameService.exchange("testLobbyId", "testIdentity", exchangeDTO);

        Mockito.verify(gameRound, Mockito.times(1)).storeExchangeAction("testIdentity",
                exchangeDTO.getCards(), exchangeDTO.getTarget());
    }

    @Test
    public void giftActionTest() {
        GiftDTO giftDTO = Mockito.mock(GiftDTO.class);
        Mockito.when(giftDTO.getCards()).thenReturn(new int[]{1,2});
        Mockito.when(giftDTO.getTarget()).thenReturn("testTarget");

        gameService.gift("testLobbyId", "testIdentity", giftDTO);

        Mockito.verify(gameRound, Mockito.times(1)).storeGiftAction("testIdentity",
                giftDTO.getCards(), giftDTO.getTarget());
    }

    @Test
    public void skipActionTest() {
        SkipDTO skipDTO = Mockito.mock(SkipDTO.class);
        Mockito.when(skipDTO.getTarget()).thenReturn("testTarget");

        gameService.skip("testLobbyId", "testIdentity", skipDTO);

        Mockito.verify(gameRound, Mockito.times(1)).storeSkipAction("testIdentity",
                skipDTO.getTarget());
    }

    @Test
    public void fantasticColorActionTest() {
        FantasticDTO fantasticDTO = Mockito.mock(FantasticDTO.class);
        Mockito.when(fantasticDTO.getColor()).thenReturn(Color.BLUE);

        gameService.fantastic("testLobbyId", "testIdentity", fantasticDTO);

        Mockito.verify(gameRound, Mockito.times(1)).storeFantasticAction("testIdentity",
                fantasticDTO.getNumber(), fantasticDTO.getColor());
    }

    @Test
    public void fantasticNumberActionTest() {
        FantasticDTO fantasticDTO = Mockito.mock(FantasticDTO.class);
        Mockito.when(fantasticDTO.getColor()).thenReturn(null);
        Mockito.when(fantasticDTO.getNumber()).thenReturn(1);

        gameService.fantastic("testLobbyId", "testIdentity", fantasticDTO);

        Mockito.verify(gameRound, Mockito.times(1)).storeFantasticAction("testIdentity",
                fantasticDTO.getNumber(), fantasticDTO.getColor());
    }

    @Test
    public void fantasticFourColorActionTest() {
        FantasticFourDTO fantasticFourDTO = Mockito.mock(FantasticFourDTO.class);
        Mockito.when(fantasticFourDTO.getColor()).thenReturn(Color.RED);
        Mockito.when(fantasticFourDTO.getTargets()).thenReturn(Collections.singletonMap("testIdentity", 4));

        gameService.fantasticFour("testLobbyId", "testIdentity", fantasticFourDTO);

        Mockito.verify(gameRound, Mockito.times(1)).storeFantasticFourAction("testIdentity",
                fantasticFourDTO.getNumber(), fantasticFourDTO.getColor(), fantasticFourDTO.getTargets());
    }

    @Test
    public void fantasticFourNumberActionTest() {
        FantasticFourDTO fantasticFourDTO = Mockito.mock(FantasticFourDTO.class);
        Mockito.when(fantasticFourDTO.getColor()).thenReturn(null);
        Mockito.when(fantasticFourDTO.getNumber()).thenReturn(1);
        Mockito.when(fantasticFourDTO.getTargets()).thenReturn(Collections.singletonMap("testIdentity", 4));

        gameService.fantasticFour("testLobbyId", "testIdentity", fantasticFourDTO);

        Mockito.verify(gameRound, Mockito.times(1)).storeFantasticFourAction("testIdentity",
                fantasticFourDTO.getNumber(), fantasticFourDTO.getColor(), fantasticFourDTO.getTargets());
    }

    @Test
    public void equalityActionTest() {
        EqualityDTO equalityDTO = Mockito.mock(EqualityDTO.class);
        Mockito.when(equalityDTO.getColor()).thenReturn(Color.RED);
        Mockito.when(equalityDTO.getTarget()).thenReturn("testTarget");

        gameService.equality("testLobbyId", "testIdentity", equalityDTO);

        Mockito.verify(gameRound, Mockito.times(1)).storeEqualityAction("testIdentity",
                equalityDTO.getColor(), equalityDTO.getTarget());
    }

    @Test
    public void counterActionTest() {
        CounterAttackDTO counterAttackDTO = Mockito.mock(CounterAttackDTO.class);
        Mockito.when(counterAttackDTO.getColor()).thenReturn(Color.RED);

        gameService.counterAttack("testLobbyId", "testIdentity", counterAttackDTO);

        Mockito.verify(gameRound, Mockito.times(1)).storeCounterAttackAction("testIdentity",
                counterAttackDTO.getColor());
    }

    @Test
    public void niceTryTest() {
        NiceTryDTO niceTryDTO = Mockito.mock(NiceTryDTO.class);
        Mockito.when(niceTryDTO.getColor()).thenReturn(Color.RED);

        gameService.niceTry("testLobbyId", "testIdentity", niceTryDTO);

        Mockito.verify(gameRound, Mockito.times(1)).storeNiceTryAction("testIdentity",
                niceTryDTO.getColor());
    }

    @Test
    public void endTurnTest() {
        gameService.endTurn("testLobbyId", "testIdentity");

        Mockito.verify(gameRound, Mockito.times(1)).playerFinishesTurn(Mockito.any());
    }

    @Test
    public void sendChatMessageTest() {
        Chat chat = new Chat("msg", "avatar:testPlayer", "testMessage");
        gameService.sendChatMessage("testLobbyId", chat);

        Mockito.verify(webSocketService, Mockito.times(1)).sendChatMessage("testLobbyId", chat);
    }

    @Test
    public void sendChatListMessageTest() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("msg", "avatar:testPlayer", "testMessage"));
        gameService.sendChatMessage("testLobbyId", chat);

        Mockito.verify(webSocketService, Mockito.times(1)).sendChatMessage("testLobbyId", chat);
    }

    @Test
    public void sendStartGameTest() {
        gameService.sendStartGame("testLobbyId");

        Mockito.verify(webSocketService, Mockito.times(1)).sendToLobby("testLobbyId", "/start-game");
    }

    @Test
    public void sendStartGameRoundTest() {
        gameService.sendStartGameRound("testLobbyId");

        Mockito.verify(webSocketService, Mockito.times(1)).sendToLobby("testLobbyId", "/start-round");
    }

    @Test
    public void sendGameStateTest() {
        List<Player> players = new ArrayList<>();
        players.add(player);
        players.add(player);

        gameService.sendGameState("testLobbyId", card, players);

        Mockito.verify(webSocketService, Mockito.times(1)).sendToLobby(Mockito.matches("testLobbyId"), Mockito.matches("/game-state"), Mockito.any());
    }

    @Test
    public void sendHandTest() {
        Mockito.when(player.getHandSize()).thenReturn(5);
        Mockito.when(player.peekCard(Mockito.anyInt())).thenReturn(card);

        gameService.sendHand("testLobbyId", player);

        Mockito.verify(webSocketService, Mockito.times(1)).sendToPlayerInLobby(Mockito.matches("testLobbyId"), Mockito.matches("testIdentity"), Mockito.matches("/hand"), Mockito.any());
    }

    @Test
    public void sendStartTurnTest() {
        gameService.sendStartTurn("testLobbyId", "testPlayer", 5, 10);

        Mockito.verify(webSocketService, Mockito.times(1)).sendToLobby(Mockito.matches("testLobbyId"), Mockito.matches("/start-turn"), Mockito.any());
    }

    @Test
    public void sendPlayableCardsTest() {
        int[] playable = {1, 4};

        gameService.sendPlayableCards("testLobbyId", player, playable);

        Mockito.verify(webSocketService, Mockito.times(1)).sendToPlayerInLobby(Mockito.matches("testLobbyId"), Mockito.matches("testIdentity"), Mockito.matches("/playable-cards"), Mockito.any());
    }

    @Test
    public void sendDrawAnimationTest() {
        gameService.sendDrawAnimation("testLobbyId", 4);
        Mockito.verify(webSocketService, Mockito.times(1)).sendToLobby(Mockito.matches("testLobbyId"), Mockito.matches("/draw"), Mockito.any());
    }

    @Test
    public void sendActionResponseTest() {
        gameService.sendActionResponse("testLobbyId", player, card);

        Mockito.verify(webSocketService, Mockito.times(1)).sendToPlayerInLobby(Mockito.matches("testLobbyId"), Mockito.matches("testIdentity"), Mockito.matches("/action-response"), Mockito.any());
    }

    @Test
    public void sendAttackWindowTest() {
        gameService.sendAttackWindow("testLobbyId", player, new int[]{1}, 5);

        Mockito.verify(webSocketService, Mockito.times(1)).sendToPlayerInLobby(Mockito.matches("testLobbyId"), Mockito.matches("testIdentity"), Mockito.matches("/attack-window"), Mockito.any());
    }

    @Test
    public void sendEventTest() {
        gameService.sendEvent("testLobbyId", new FridayTheThirteenthEvent());

        Mockito.verify(webSocketService, Mockito.times(1)).sendToLobby(Mockito.matches("testLobbyId"), Mockito.matches("/event"), Mockito.any());
    }

    @Test
    public void sendEndRoundTest() {
        gameService.sendEndRound("testLobbyId", Collections.singletonList(player));

        Mockito.verify(webSocketService, Mockito.times(1)).sendToLobby(Mockito.matches("testLobbyId"), Mockito.matches("/end-round"), Mockito.any());
    }

    @Test
    public void sendEndGameTest() {
        gameService.sendEndGame("testLobbyId", Collections.singletonList(player));

        Mockito.verify(webSocketService, Mockito.times(1)).sendToLobby(Mockito.matches("testLobbyId"), Mockito.matches("/end-game"), Mockito.any());
    }
}
