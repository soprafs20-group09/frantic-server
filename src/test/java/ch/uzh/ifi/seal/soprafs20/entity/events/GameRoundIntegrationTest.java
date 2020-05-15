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
        assertEquals(2, player2.getHandSize());
        assertEquals(pileCard, testRound.getDiscardPile().peek());
        assertEquals(player1, testRound.getCurrentPlayer());
    }

    @Test
    public void playNumberCard_success() {
        Card cardToPlay = player1.peekCard(1);
        testRound.getDiscardPile().push(new Card(Color.BLUE, Type.NUMBER, Value.EIGHT, false, 10));
        testRound.startTurnTimer(30);
        testRound.playCard("id1", 1);

        Mockito.verify(this.gameService).sendHand("lobbyId", player1);
        Mockito.verify(this.gameService).sendChatMessage(Mockito.any(), (Chat) Mockito.any());
        assertEquals(2, player1.getHandSize());
        assertEquals(cardToPlay, testRound.getDiscardPile().peek());
        assertEquals(player2, testRound.getCurrentPlayer());
    }

    @Test
    public void playBlackNumberCard_success_prepareEvent() {
        Card cardToPlay = player1.peekCard(0);
        testRound.getDiscardPile().push(new Card(Color.BLUE, Type.NUMBER, Value.EIGHT, false, 10));
        testRound.startTurnTimer(30);
        testRound.playCard("id1", 0);

        Mockito.verify(this.gameService).sendHand("lobbyId", player1);
        Mockito.verify(this.gameService).sendChatMessage(Mockito.any(), (Chat) Mockito.any());
        Mockito.verify(this.gameService).sendPlayable("lobbyId", player1, new int[0], false, false);
        Mockito.verify(this.gameService).sendGameState("lobbyId", cardToPlay, this.testRound.getListOfPlayers(), false);
        Mockito.verify(this.gameService).sendEvent(Mockito.any(), Mockito.any());
        Mockito.verify(this.gameService).sendTimer("lobbyId", 11);

        assertEquals(2, player1.getHandSize());
        assertEquals(cardToPlay, testRound.getDiscardPile().peek());
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
        assertEquals(3, player1.getHandSize());
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
        assertEquals(10, player1.getHandSize());

        Card fuckYou = player1.peekCard(7);
        testRound.getDiscardPile().push(new Card(Color.BLUE, Type.NUMBER, Value.EIGHT, false, 10));
        testRound.startTurnTimer(30);
        testRound.playCard("id1", 7);

        Mockito.verify(this.gameService).sendHand("lobbyId", player1);
        Mockito.verify(this.gameService).sendChatMessage(Mockito.any(), (Chat) Mockito.any());
        assertEquals(9, player1.getHandSize());
        assertEquals(fuckYou, testRound.getDiscardPile().peek());
        assertEquals(player2, testRound.getCurrentPlayer());
    }

    @Test
    public void playFuckYou_not10Cards() {
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 10));
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 11));
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 12));
        player1.pushCardToHand(new Card(Color.BLUE, Type.NUMBER, Value.THREE, false, 13));
        player1.pushCardToHand(new Card(Color.BLACK, Type.SPECIAL, Value.FUCKYOU, false, 14));

        Card pileCard = new Card(Color.YELLOW, Type.NUMBER, Value.EIGHT, false, 10);
        testRound.getDiscardPile().push(pileCard);
        testRound.startTurnTimer(30);
        testRound.playCard("id1", 7);

        Mockito.verify(this.gameService, Mockito.never()).sendHand("lobbyId", player1);
        Mockito.verify(this.gameService, Mockito.never()).sendChatMessage(Mockito.any(), (Chat) Mockito.any());
        assertEquals(8, player1.getHandSize());
        assertEquals(pileCard, testRound.getDiscardPile().peek());
        assertEquals(player1, testRound.getCurrentPlayer());
    }

    @Test
    public void play2ChanceCard_success() {
        Card secondChance = new Card(Color.BLUE, Type.SPECIAL, Value.SECONDCHANCE, false, 10);
        player1.pushCardToHand(secondChance);
        testRound.getDiscardPile().push(new Card(Color.BLUE, Type.NUMBER, Value.EIGHT, false, 11));
        testRound.startTurnTimer(30);
        testRound.playCard("id1", 3);

        Mockito.verify(this.gameService).sendHand("lobbyId", player1);
        Mockito.verify(this.gameService).sendChatMessage(Mockito.any(), (Chat) Mockito.any());
        Mockito.verify(this.gameService).sendPlayable("lobbyId", player1, player1.getPlayableCards(secondChance), true, false);
        assertEquals(3, player1.getHandSize());
        assertEquals(secondChance, testRound.getDiscardPile().peek());
        assertEquals(player1, testRound.getCurrentPlayer());
    }

    @Test
    public void playFantasticFourCard_success_45sec() {
        Card fantasticFour = new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTICFOUR, true, 10);
        player1.pushCardToHand(fantasticFour);
        testRound.getDiscardPile().push(new Card(Color.BLUE, Type.NUMBER, Value.EIGHT, false, 11));
        testRound.startTurnTimer(30);
        testRound.playCard("id1", 3);

        Mockito.verify(this.gameService).sendHand("lobbyId", player1);
        Mockito.verify(this.gameService).sendChatMessage(Mockito.any(), (Chat) Mockito.any());
        Mockito.verify(this.gameService).sendTimer("lobbyId", 45);
        Mockito.verify(this.gameService).sendActionResponse("lobbyId", player1, fantasticFour);
        assertEquals(3, player1.getHandSize());
        assertEquals(fantasticFour, testRound.getDiscardPile().peek());
        assertEquals(player1, testRound.getCurrentPlayer());
    }

    @Test
    public void playEqualityCard_success_30sec() {
        Card equality = new Card(Color.MULTICOLOR, Type.SPECIAL, Value.EQUALITY, false, 10);
        player1.pushCardToHand(equality);
        testRound.getDiscardPile().push(new Card(Color.BLUE, Type.NUMBER, Value.EIGHT, false, 11));
        testRound.startTurnTimer(30);
        testRound.playCard("id1", 3);

        Mockito.verify(this.gameService).sendHand("lobbyId", player1);
        Mockito.verify(this.gameService).sendChatMessage(Mockito.any(), (Chat) Mockito.any());
        Mockito.verify(this.gameService).sendTimer("lobbyId", 30);
        Mockito.verify(this.gameService).sendActionResponse("lobbyId", player1, equality);
        assertEquals(3, player1.getHandSize());
        assertEquals(equality, testRound.getDiscardPile().peek());
        assertEquals(player1, testRound.getCurrentPlayer());
    }

    @Test
    public void playNiceTry_niceTryOpportunity_success() {
        player4.popCard();
        player4.popCard();
        player4.popCard();
        assertEquals(0, player4.getHandSize());

        Card niceTry = new Card(Color.MULTICOLOR, Type.SPECIAL, Value.NICETRY, false, 10);
        player3.pushCardToHand(niceTry);
        testRound.getDiscardPile().push(new Card(Color.BLUE, Type.NUMBER, Value.EIGHT, false, 11));
        testRound.startTurnTimer(30);
        testRound.playCard("id3", 2);

        Mockito.verify(this.gameService).sendPlayable("lobbyId", player3, new int[0], false, false);
        Mockito.verify(this.gameService).sendHand("lobbyId", player3);
        Mockito.verify(this.gameService, Mockito.times(2)).sendChatMessage(Mockito.any(), (Chat) Mockito.any()); //invocation for card played & drawn cards
        Mockito.verify(this.gameService, Mockito.times(2)).sendHand("lobbyId", player4); //invocation for card played & drawn cards
        Mockito.verify(this.gameService).sendActionResponse("lobbyId", player3, niceTry);
        Mockito.verify(this.gameService).sendTimer("lobbyId", 30);

        assertEquals(2, player3.getHandSize());
        assertEquals(3, player4.getHandSize());
        assertEquals(niceTry, testRound.getDiscardPile().peek());
        assertEquals(player1, testRound.getCurrentPlayer());
    }

    @Test
    public void playCounterattack_niceTryOpportunity_unsuccessful() {
        player4.popCard();
        player4.popCard();
        player4.popCard();
        assertEquals(0, player4.getHandSize());

        Card counterattack = new Card(Color.MULTICOLOR, Type.SPECIAL, Value.COUNTERATTACK, false, 10);
        player3.pushCardToHand(counterattack);
        Card pileCard = new Card(Color.YELLOW, Type.NUMBER, Value.EIGHT, false, 11);
        testRound.getDiscardPile().push(pileCard);
        testRound.startTurnTimer(30);
        testRound.playCard("id3", 2);

        Mockito.verify(this.gameService, Mockito.never()).sendPlayable("lobbyId", player3, new int[0], false, false);
        Mockito.verify(this.gameService, Mockito.never()).sendHand("lobbyId", player3);

        assertEquals(3, player3.getHandSize());
        assertEquals(0, player4.getHandSize());
        assertEquals(pileCard, testRound.getDiscardPile().peek());
        assertEquals(player1, testRound.getCurrentPlayer());
    }

    @Test
    public void playCounterAttack_counterAttackOpportunity_success() {
        testRound.startTurnTimer(30);
        testRound.storeSkipAction("id1", "player3");

        Card counterattack = new Card(Color.MULTICOLOR, Type.SPECIAL, Value.COUNTERATTACK, false, 10);
        player3.pushCardToHand(counterattack);
        Card actionCard = new Card(Color.BLUE, Type.SPECIAL, Value.SKIP, true, 11);
        testRound.getDiscardPile().push(actionCard);
        testRound.playCard("id3", 2);

        Mockito.verify(this.gameService, Mockito.times(2)).sendPlayable("lobbyId", player3, new int[0], false, false); //once in prepareCounterattack & playCounterattack
        Mockito.verify(this.gameService).sendHand("lobbyId", player3);
        Mockito.verify(this.gameService, Mockito.times(2)).sendChatMessage(Mockito.any(), (Chat) Mockito.any()); //once in prepareCounterattack & playCounterattack
        Mockito.verify(this.gameService).sendGameState("lobbyId", counterattack, this.testRound.getListOfPlayers(), false);
        Mockito.verify(this.gameService).sendAttackTurn("lobbyId", "player3");
        Mockito.verify(this.gameService).sendActionResponse("lobbyId", player3, actionCard);
        Mockito.verify(this.gameService).sendTimer("lobbyId", 30);

        assertEquals(2, player3.getHandSize());
        assertEquals(counterattack, testRound.getDiscardPile().peek());
        assertEquals(player1, testRound.getCurrentPlayer());
    }

    @Test
    public void playCounterAttack_counterAttackOpportunity_notTarget() {
        testRound.startTurnTimer(30);
        testRound.storeSkipAction("id1", "player2");

        Card counterattack = new Card(Color.MULTICOLOR, Type.SPECIAL, Value.COUNTERATTACK, false, 10);
        player3.pushCardToHand(counterattack);
        Card actionCard = new Card(Color.BLUE, Type.SPECIAL, Value.SKIP, true, 11);
        testRound.getDiscardPile().push(actionCard);
        testRound.playCard("id3", 2);

        Mockito.verify(this.gameService, Mockito.never()).sendHand("lobbyId", player3);

        assertEquals(3, player3.getHandSize());
        assertEquals(actionCard, testRound.getDiscardPile().peek());
        assertEquals(player1, testRound.getCurrentPlayer());
    }

    //if someone tries to play a nice try to block an attack
    @Test
    public void playNiceTry_counterAttackOpportunity_unsuccessful() {
        testRound.startTurnTimer(30);
        testRound.storeSkipAction("id1", "player3");

        Card niceTry = new Card(Color.MULTICOLOR, Type.SPECIAL, Value.NICETRY, false, 10);
        player3.pushCardToHand(niceTry);
        Card actionCard = new Card(Color.BLUE, Type.SPECIAL, Value.SKIP, true, 11);
        testRound.getDiscardPile().push(actionCard);
        testRound.playCard("id3", 2);

        Mockito.verify(this.gameService, Mockito.never()).sendHand("lobbyId", player3);

        assertEquals(3, player3.getHandSize());
        assertEquals(actionCard, testRound.getDiscardPile().peek());
        assertEquals(player1, testRound.getCurrentPlayer());
    }
}
