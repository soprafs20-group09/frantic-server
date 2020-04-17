package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.PlayCardDTO;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
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
    public void endTurnTest() {
        gameService.endTurn("testLobbyId", "testIdentity");

        Mockito.verify(gameRound, Mockito.times(1)).finishTurn();
    }

    @Test
    public void sendChatEventMessageTest() {
        gameService.sendChatEventMessage("testLobbyId", "testMessage");

        Mockito.verify(webSocketService, Mockito.times(1)).sendChatEventMessage("testLobbyId", "testMessage");
    }

    @Test
    public void sendPlayerMessageTest() {
        gameService.sendChatPlayerMessage("testLobbyId", "testMessage", "testUsername");

        Mockito.verify(webSocketService, Mockito.times(1)).sendChatPlayerMessage("testLobbyId", "testMessage", "testUsername");
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
}
