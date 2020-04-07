package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.cards.NumberCard;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameRound {

    private List<Player> listOfPlayers;
    private Player currentPlayer;
    private int remainingTurns;
    private ActionStack actionStack;
    private Pile drawStack;
    private Pile discardPile;


    public GameRound(List<Player> listOfPlayers, Player firstPlayer) {
        this.listOfPlayers = listOfPlayers;
        this.currentPlayer = firstPlayer;
        this.actionStack = new ActionStack();
        this.remainingTurns = -1; //indicates that there is no limit
    }

    //creates card stacks & player hands
    public void initializeGameRound() {
        this.drawStack = new DrawStack();
        this.discardPile = new DrawStack();
        // Add cards to stacks here...?

        //move 7 initial cards to player hands
        for (Player player : listOfPlayers) {
            drawCardFromStack(player, 7);
        }
    }

    //main game loop
    public void startGameRound() {
        while (!isRoundOver() && (remainingTurns == -1 || remainingTurns > 0)) {
            performTurn();
            changePlayer();
        }
        calculatePoints();
        removeCardsFromHands();
    }

    private boolean playCard(Player player, Card card) {
        // check in card-class if card can be put on discard pile
        // put card on pile
        return true;
    }

    // moves #amount cards from Stack to players hand
    private void drawCardFromStack(Player player, int amount) {
        for (int i = 1; i<=amount; i++) {
            //player.pushCardToHand(drawStack.pop());
        }
    }

    private boolean moveCard(Card card, Pile pileA, Pile pileB) {
        // moves card from pileA to pileB
        return true;
    }

    private Card takeRandomCard(Pile pile) {
        // takes random card from pile and returns it. Do not remove card
        return new NumberCard(Color.BLACK, 5); //just a random example
    }

    private void changePlayer() {
        int playersIndex = this.listOfPlayers.indexOf(this.currentPlayer);
        playersIndex = (playersIndex + 1)%listOfPlayers.size();
        this.currentPlayer = listOfPlayers.get(playersIndex);
    }

    private void calculatePoints() {
        //goes through cards of each player and adds up the points
    }

    private boolean isRoundOver() {
        return getHandSizes().containsValue(0);
    }

    private Map<Player, Integer> getHandSizes() {
        Map<Player, Integer> mappedPlayers = new HashMap<>();
        for (Player player : listOfPlayers) {
            int handSize = player.getHandSize();
            mappedPlayers.put(player, handSize);
        }
        return mappedPlayers;
    }

    private void performTurn() {
        //performs turn
    }

    private void performEvent() {
        //performs event
    }

    private void removeCardsFromHands() {
        // makes sure, that after each round, all players have 0 cards
    }

}
