package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.entity.events.FridayTheThirteenthEvent;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

        Mockito.verify(lobby).startGame();
    }

    @Test
    public void playCardTest() {
        PlayCardDTO playCardDTO = Mockito.mock(PlayCardDTO.class);
        Mockito.when(playCardDTO.getIndex()).thenReturn(5);

        gameService.playCard("testLobbyId", "testIdentity", playCardDTO);

        Mockito.verify(gameRound).playCard("testIdentity", 5);
    }

    @Test
    public void drawCardTest() {
        gameService.drawCard("testLobbyId", "testIdentity");

        Mockito.verify(gameRound).currentPlayerDrawCard("testIdentity");
    }

    @Test
    public void exchangeActionTest() {
        ExchangeDTO exchangeDTO = Mockito.mock(ExchangeDTO.class);
        Mockito.when(exchangeDTO.getCards()).thenReturn(new int[]{1, 2});
        Mockito.when(exchangeDTO.getTarget()).thenReturn("testTarget");

        gameService.exchange("testLobbyId", "testIdentity", exchangeDTO);

        Mockito.verify(gameRound).storeExchangeAction("testIdentity",
                exchangeDTO.getCards(), exchangeDTO.getTarget());
    }

    @Test
    public void giftActionTest() {
        GiftDTO giftDTO = Mockito.mock(GiftDTO.class);
        Mockito.when(giftDTO.getCards()).thenReturn(new int[]{1, 2});
        Mockito.when(giftDTO.getTarget()).thenReturn("testTarget");

        gameService.gift("testLobbyId", "testIdentity", giftDTO);

        Mockito.verify(gameRound).storeGiftAction("testIdentity",
                giftDTO.getCards(), giftDTO.getTarget());
    }

    @Test
    public void skipActionTest() {
        SkipDTO skipDTO = Mockito.mock(SkipDTO.class);
        Mockito.when(skipDTO.getTarget()).thenReturn("testTarget");

        gameService.skip("testLobbyId", "testIdentity", skipDTO);

        Mockito.verify(gameRound).storeSkipAction("testIdentity",
                skipDTO.getTarget());
    }

    @Test
    public void fantasticColorActionTest() {
        FantasticDTO fantasticDTO = Mockito.mock(FantasticDTO.class);
        Mockito.when(fantasticDTO.getColor()).thenReturn(Color.BLUE);

        gameService.fantastic("testLobbyId", "testIdentity", fantasticDTO);

        Mockito.verify(gameRound).storeFantasticAction("testIdentity",
                fantasticDTO.getNumber(), fantasticDTO.getColor());
    }

    @Test
    public void fantasticNumberActionTest() {
        FantasticDTO fantasticDTO = Mockito.mock(FantasticDTO.class);
        Mockito.when(fantasticDTO.getColor()).thenReturn(null);
        Mockito.when(fantasticDTO.getNumber()).thenReturn(1);

        gameService.fantastic("testLobbyId", "testIdentity", fantasticDTO);

        Mockito.verify(gameRound).storeFantasticAction("testIdentity",
                fantasticDTO.getNumber(), fantasticDTO.getColor());
    }

    @Test
    public void fantasticFourColorActionTest() {
        FantasticFourDTO fantasticFourDTO = Mockito.mock(FantasticFourDTO.class);
        Mockito.when(fantasticFourDTO.getColor()).thenReturn(Color.RED);
        Mockito.when(fantasticFourDTO.getTargets()).thenReturn(Collections.singletonMap("testIdentity", 4));

        gameService.fantasticFour("testLobbyId", "testIdentity", fantasticFourDTO);

        Mockito.verify(gameRound).storeFantasticFourAction("testIdentity",
                fantasticFourDTO.getNumber(), fantasticFourDTO.getColor(), fantasticFourDTO.getTargets());
    }

    @Test
    public void fantasticFourNumberActionTest() {
        FantasticFourDTO fantasticFourDTO = Mockito.mock(FantasticFourDTO.class);
        Mockito.when(fantasticFourDTO.getColor()).thenReturn(null);
        Mockito.when(fantasticFourDTO.getNumber()).thenReturn(1);
        Mockito.when(fantasticFourDTO.getTargets()).thenReturn(Collections.singletonMap("testIdentity", 4));

        gameService.fantasticFour("testLobbyId", "testIdentity", fantasticFourDTO);

        Mockito.verify(gameRound).storeFantasticFourAction("testIdentity",
                fantasticFourDTO.getNumber(), fantasticFourDTO.getColor(), fantasticFourDTO.getTargets());
    }

    @Test
    public void equalityActionTest() {
        EqualityDTO equalityDTO = Mockito.mock(EqualityDTO.class);
        Mockito.when(equalityDTO.getColor()).thenReturn(Color.RED);
        Mockito.when(equalityDTO.getTarget()).thenReturn("testTarget");

        gameService.equality("testLobbyId", "testIdentity", equalityDTO);

        Mockito.verify(gameRound).storeEqualityAction("testIdentity",
                equalityDTO.getColor(), equalityDTO.getTarget());
    }

    @Test
    public void counterActionTest() {
        CounterAttackDTO counterAttackDTO = Mockito.mock(CounterAttackDTO.class);
        Mockito.when(counterAttackDTO.getColor()).thenReturn(Color.RED);

        gameService.counterAttack("testLobbyId", "testIdentity", counterAttackDTO);

        Mockito.verify(gameRound).storeCounterAttackAction("testIdentity",
                counterAttackDTO.getColor());
    }

    @Test
    public void niceTryTest() {
        NiceTryDTO niceTryDTO = Mockito.mock(NiceTryDTO.class);
        Mockito.when(niceTryDTO.getColor()).thenReturn(Color.RED);

        gameService.niceTry("testLobbyId", "testIdentity", niceTryDTO);

        Mockito.verify(gameRound).storeNiceTryAction("testIdentity",
                niceTryDTO.getColor());
    }

    @Test
    public void surprisePartyTest() {
        SurprisePartyDTO surprisePartyDTO = Mockito.mock(SurprisePartyDTO.class);
        Mockito.when(surprisePartyDTO.getCard()).thenReturn(3);
        Mockito.when(surprisePartyDTO.getTarget()).thenReturn("testTarget");

        gameService.surpriseParty("testLobbyId", "testIdentity", surprisePartyDTO);

        Mockito.verify(gameRound).prepareSurpriseParty("testIdentity", 3, "testTarget");
    }

    @Test
    public void merryChristmasTest() {
        MerryChristmasDTO merryChristmasDTO = Mockito.mock(MerryChristmasDTO.class);
        Mockito.when(merryChristmasDTO.getTargets()).thenReturn(new HashMap<>());

        gameService.merryChristmas("testLobbyId", "testIdentity", merryChristmasDTO);

        Mockito.verify(gameRound).prepareMerryChristmas("testIdentity", merryChristmasDTO.getTargets());
    }

    @Test
    public void recessionTest() {
        RecessionDTO recessionDTO = Mockito.mock(RecessionDTO.class);
        Mockito.when(recessionDTO.getCards()).thenReturn(new int[]{1});

        gameService.recession("testLobbyId", "testIdentity", recessionDTO);

        Mockito.verify(gameRound).prepareRecession("testIdentity", recessionDTO.getCards());
    }

    @Test
    public void marketTest() {
        MarketDTO marketDTO = Mockito.mock(MarketDTO.class);
        Mockito.when(marketDTO.getCard()).thenReturn(1);

        gameService.market("testLobbyId", "testIdentity", marketDTO);

        Mockito.verify(gameRound).prepareMarket("testIdentity", marketDTO.getCard());
    }

    @Test
    public void gamblingManTest() {
        GamblingManDTO gamblingManDTO = Mockito.mock(GamblingManDTO.class);
        Mockito.when(gamblingManDTO.getCard()).thenReturn(1);

        gameService.gamblingMan("testLobbyId", "testIdentity", gamblingManDTO);

        Mockito.verify(gameRound).prepareGamblingMan("testIdentity", gamblingManDTO.getCard());
    }

    @Test
    public void endTurnTest() {
        gameService.endTurn("testLobbyId", "testIdentity");

        Mockito.verify(gameRound).playerFinishesTurn(Mockito.any());
    }

    @Test
    public void sendChatMessageTest() {
        Chat chat = new Chat("msg", "avatar:testPlayer", "testMessage");
        gameService.sendChatMessage("testLobbyId", chat);

        Mockito.verify(webSocketService).sendChatMessage("testLobbyId", chat);
    }

    @Test
    public void sendChatListMessageTest() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("msg", "avatar:testPlayer", "testMessage"));
        gameService.sendChatMessage("testLobbyId", chat);

        Mockito.verify(webSocketService).sendChatMessage("testLobbyId", chat);
    }

    @Test
    public void sendStartGameTest() {
        gameService.sendStartGame("testLobbyId");

        Mockito.verify(webSocketService).sendToLobby("testLobbyId", "/start-game");
    }

    @Test
    public void sendStartGameRoundTest() {
        gameService.sendStartGameRound("testLobbyId");

        Mockito.verify(webSocketService).sendToLobby("testLobbyId", "/start-round");
    }

    @Test
    public void sendGameStateTest() {
        List<Player> players = new ArrayList<>();
        player.pushCardToHand(new Card(Color.RED, Type.NUMBER, Value.THREE));
        players.add(player);
        players.add(player);

        gameService.sendGameState("testLobbyId", card, players, false);

        Mockito.verify(webSocketService).sendToLobby(Mockito.matches("testLobbyId"), Mockito.matches("/game-state"), Mockito.any());
    }

    @Test
    public void sendHandTest() {
        Mockito.when(player.getHandSize()).thenReturn(5);
        Mockito.when(player.peekCard(Mockito.anyInt())).thenReturn(card);

        gameService.sendHand("testLobbyId", player);

        Mockito.verify(webSocketService).sendToPlayerInLobby(Mockito.matches("testLobbyId"), Mockito.matches("testIdentity"), Mockito.matches("/hand"), Mockito.any());
    }

    @Test
    public void sendStartTurnTest() {
        gameService.sendStartTurn("testLobbyId", "testPlayer");

        Mockito.verify(webSocketService).sendToLobby(Mockito.matches("testLobbyId"), Mockito.matches("/start-turn"), Mockito.any());
    }

    @Test
    public void sendPlayableCardsTest() {
        int[] playable = {1, 4};

        gameService.sendPlayable("testLobbyId", player, playable, false, false);
        Mockito.verify(webSocketService).sendToPlayerInLobby(Mockito.matches("testLobbyId"), Mockito.matches("testIdentity"), Mockito.matches("/playable"), Mockito.any());
    }

    @Test
    public void sendTimerTest() {
        gameService.sendTimer("testLobbyId", 30);
        Mockito.verify(webSocketService).sendToLobby(Mockito.matches("testLobbyId"), Mockito.matches("/timer"), Mockito.any());
    }

    @Test
    public void sendAnimationSpeedTest() {
        gameService.sendAnimationSpeed("testLobbyId", 500);
        Mockito.verify(webSocketService).sendToLobby(Mockito.matches("testLobbyId"), Mockito.matches("/animation-speed"), Mockito.any());
    }

    @Test
    public void sendDrawAnimationTest() {
        gameService.sendDrawAnimation("testLobbyId", 4);
        Mockito.verify(webSocketService).sendToLobby(Mockito.matches("testLobbyId"), Mockito.matches("/draw"), Mockito.any());
    }

    @Test
    public void sendActionResponseTest() {
        gameService.sendActionResponse("testLobbyId", player, card);
        Mockito.verify(webSocketService).sendToPlayerInLobby(Mockito.matches("testLobbyId"), Mockito.matches("testIdentity"), Mockito.matches("/action-response"), Mockito.any());
    }

    @Test
    public void sendEventActionResponseTest() {
        gameService.sendEventActionResponse("testLobbyId", "testEvent");
        Mockito.verify(webSocketService).sendToLobby(Mockito.matches("testLobbyId"), Mockito.matches("/action-response"), Mockito.any());
    }

    @Test
    public void sendAttackTurnTest() {
        gameService.sendAttackTurn("testLobbyId", "testPlayer");
        Mockito.verify(webSocketService).sendToLobby(Mockito.matches("testLobbyId"), Mockito.matches("/attack-turn"), Mockito.any());
    }

    @Test
    public void sendOverlayTest() {
        gameService.sendOverlay("testLobbyId", player, "testIcon", "testTitle", "testMessage", 10);
        Mockito.verify(webSocketService).sendToPlayerInLobby(Mockito.matches("testLobbyId"), Mockito.matches("testIdentity"), Mockito.matches("/overlay"), Mockito.any());
    }

    @Test
    public void sendEventTest() {
        gameService.sendEvent("testLobbyId", new FridayTheThirteenthEvent(this.gameRound));
        Mockito.verify(webSocketService).sendToLobby(Mockito.matches("testLobbyId"), Mockito.matches("/event"), Mockito.any());
    }

    @Test
    public void sendRecessionTest() {
        gameService.sendRecession("testLobbyId", player, 2);
        Mockito.verify(webSocketService).sendToPlayerInLobby(Mockito.matches("testLobbyId"), Mockito.matches("testIdentity"), Mockito.matches("/recession"), Mockito.any());
    }

    @Test
    public void sendGamblingManTest() {
        gameService.sendGamblingMan("testLobbyId", player, new int[]{1});
        Mockito.verify(webSocketService).sendToPlayerInLobby(Mockito.matches("testLobbyId"), Mockito.matches("testIdentity"), Mockito.matches("/gambling-man-window"), Mockito.any());
    }

    @Test
    public void sendMarketWindowTest() {
        gameService.sendMarketWindow("testLobbyId", player, Collections.singletonList(card));
        Mockito.verify(webSocketService).sendToPlayerInLobby(Mockito.matches("testLobbyId"), Mockito.matches("testIdentity"), Mockito.matches("/market-window"), Mockito.any());
    }

    @Test
    public void sendEndRoundTest() {
        gameService.sendEndRound("testLobbyId", Collections.singletonList(player), new HashMap<>(), 154, 20,null, null);
        Mockito.verify(webSocketService).sendToLobby(Mockito.matches("testLobbyId"), Mockito.matches("/end-round"), Mockito.any());
    }

    @Test
    public void sendEndGameTest() {
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyString())).thenReturn(lobby);

        gameService.sendEndGame("testLobbyId", Collections.singletonList(player));
        Mockito.verify(webSocketService).sendToLobby(Mockito.matches("testLobbyId"), Mockito.matches("/end-game"), Mockito.any());
    }
}
