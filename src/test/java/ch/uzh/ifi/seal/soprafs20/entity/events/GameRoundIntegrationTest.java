package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameRoundIntegrationTest {

    @Mock
    private Game game;

    @Mock
    private GameService gameService;

    private GameRound testRound;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        List<Player> playerList = new ArrayList<>();
        this.player1 = new Player();
        player1.setUsername("player1");
        player1.setIdentity("id1");
        player1.pushCardToHand(new Card(Color.BLACK, Type.NUMBER, Value.EIGHT, true, 0));
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 1));
        player1.pushCardToHand(new Card(Color.YELLOW, Type.NUMBER, Value.NINE, false, 2));
        player1.setPoints(42);
        playerList.add(player1);
        this.player2 = new Player();
        player2.setUsername("player2");
        player2.setIdentity("id2");
        player2.pushCardToHand(new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 3));
        player2.pushCardToHand(new Card(Color.YELLOW, Type.NUMBER, Value.NINE, false, 4));
        player2.setPoints(104);
        playerList.add(player2);
        this.player3 = new Player();
        player3.setUsername("player3");
        player3.setIdentity("id3");
        player3.pushCardToHand(new Card(Color.BLUE, Type.SPECIAL, Value.EXCHANGE, true, 5));
        player3.pushCardToHand(new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTICFOUR, true, 6));
        player3.setPoints(44);
        playerList.add(player3);
        this.player4 = new Player();
        player4.setUsername("player4");
        player4.setIdentity("id4");
        player4.pushCardToHand(new Card(Color.RED, Type.SPECIAL, Value.EXCHANGE, true, 7));
        player4.pushCardToHand(new Card(Color.MULTICOLOR, Type.SPECIAL, Value.NICETRY, false, 8));
        player4.pushCardToHand(new Card(Color.BLACK, Type.NUMBER, Value.ONE, false, 9));
        player4.setPoints(21);
        playerList.add(player4);

        this.testRound = new GameRound(this.game, "lobbyId", playerList, player1);
        this.testRound.setGameService(this.gameService);
    }

    @Test
    public void playCard_playerNotAllowedToPlay() {
        Card pileCard = new Card(Color.YELLOW, Type.NUMBER, Value.EIGHT, false, 10);
        testRound.getDiscardPile().push(pileCard);
        testRound.startTurnTimer(30);
        testRound.playCard("id2", 1);

        Mockito.verify(this.gameService, Mockito.never()).sendHand("lobbyId", player1);
        Mockito.verify(this.gameService, Mockito.never()).sendChatMessage(Mockito.any(), (Chat) Mockito.any());
        assertEquals(pileCard, testRound.getDiscardPile().peek());
        assertEquals(player1, testRound.getCurrentPlayer());
    }

    @Test
    public void playNumberCard_success() {
        Card card = player1.peekCard(1);
        testRound.getDiscardPile().push(new Card(Color.BLUE, Type.NUMBER, Value.EIGHT, false, 10));
        testRound.startTurnTimer(30);
        testRound.playCard("id1", 1);

        Mockito.verify(this.gameService).sendHand("lobbyId", player1);
        Mockito.verify(this.gameService).sendChatMessage(Mockito.any(), (Chat) Mockito.any());
        assertEquals(card, testRound.getDiscardPile().peek());
        assertEquals(player2, testRound.getCurrentPlayer());
    }

    @Test
    public void playBlackNumberCard_success_prepareEvent() {
        Card card = player1.peekCard(0);
        testRound.getDiscardPile().push(new Card(Color.BLUE, Type.NUMBER, Value.EIGHT, false, 10));
        testRound.startTurnTimer(30);
        testRound.playCard("id1", 0);

        Mockito.verify(this.gameService).sendHand("lobbyId", player1);
        Mockito.verify(this.gameService).sendChatMessage(Mockito.any(), (Chat) Mockito.any());
        Mockito.verify(this.gameService).sendPlayable("lobbyId", player1, new int[0], false, false);
        Mockito.verify(this.gameService).sendGameState("lobbyId", card, this.testRound.getListOfPlayers(), false);
        Mockito.verify(this.gameService).sendEvent(Mockito.any(), Mockito.any());
        Mockito.verify(this.gameService).sendTimer("lobbyId", 11);

        assertEquals(card, testRound.getDiscardPile().peek());
        assertEquals(player1, testRound.getCurrentPlayer());
    }

    @Test
    public void playNumberCard_cardNotPlayable() {
        Card pileCard = new Card(Color.YELLOW, Type.NUMBER, Value.EIGHT, false, 10);
        testRound.getDiscardPile().push(pileCard);
        testRound.startTurnTimer(30);
        testRound.playCard("id1", 1);

        Mockito.verify(this.gameService, Mockito.never()).sendHand("lobbyId", player1);
        Mockito.verify(this.gameService, Mockito.never()).sendChatMessage(Mockito.any(), (Chat) Mockito.any());
        assertEquals(pileCard, testRound.getDiscardPile().peek());
        assertEquals(player1, testRound.getCurrentPlayer());
    }

    @Test
    public void playFuckYou_success() {
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 10));
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 11));
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 12));
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 13));
        player1.pushCardToHand(new Card(Color.BLACK, Type.SPECIAL, Value.FUCKYOU, false, 14));
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 15));
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 16));

        Card card = player1.peekCard(7);
        testRound.getDiscardPile().push(new Card(Color.BLUE, Type.NUMBER, Value.EIGHT, false, 10));
        testRound.startTurnTimer(30);
        testRound.playCard("id1", 7);

        Mockito.verify(this.gameService).sendHand("lobbyId", player1);
        Mockito.verify(this.gameService).sendChatMessage(Mockito.any(), (Chat) Mockito.any());
        assertEquals(card, testRound.getDiscardPile().peek());
        assertEquals(player2, testRound.getCurrentPlayer());
    }

    @Test
    public void playFuckYou_not10Cards() {
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 10));
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 11));
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 12));
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 13));
        player1.pushCardToHand(new Card(Color.BLACK, Type.SPECIAL, Value.FUCKYOU, false, 14));

        Card card = player1.peekCard(7);
        Card pileCard = new Card(Color.YELLOW, Type.NUMBER, Value.EIGHT, false, 10);
        testRound.getDiscardPile().push(pileCard);
        testRound.startTurnTimer(30);
        testRound.playCard("id1", 7);

        Mockito.verify(this.gameService, Mockito.never()).sendHand("lobbyId", player1);
        Mockito.verify(this.gameService, Mockito.never()).sendChatMessage(Mockito.any(), (Chat) Mockito.any());
        assertEquals(pileCard, testRound.getDiscardPile().peek());
        assertEquals(player1, testRound.getCurrentPlayer());
    }

    @Test
    public void play2ChanceCard_success() {
        player1.pushCardToHand(new Card(Color.BLUE, Type.SPECIAL, Value.SECONDCHANCE, false, 10));
        Card card = player1.peekCard(3);
        testRound.getDiscardPile().push(new Card(Color.BLUE, Type.NUMBER, Value.EIGHT, false, 11));
        testRound.startTurnTimer(30);
        testRound.playCard("id1", 3);

        Mockito.verify(this.gameService).sendHand("lobbyId", player1);
        Mockito.verify(this.gameService).sendChatMessage(Mockito.any(), (Chat) Mockito.any());
        Mockito.verify(this.gameService).sendPlayable("lobbyId", player1, player1.getPlayableCards(card), true, false);
        assertEquals(card, testRound.getDiscardPile().peek());
        assertEquals(player1, testRound.getCurrentPlayer());
    }
}
