package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.cards.NumberCard;

import java.util.List;

public class GameRound {

    private List<Player> listOfPlayers;
    private Player currentPlayer;
    private int remainingTurns;
    private ActionStack actionStack;
    //private Pile drawStack;
    //private Pile discardPile


    public GameRound(List<Player> listOfPlayers, Player firstPlayer) {
        this.listOfPlayers = listOfPlayers;
        this.currentPlayer = firstPlayer;
        this.actionStack = new ActionStack();
        this.remainingTurns = -1; //indicates that there is no limit
    }

    public void initializeGameRound() {
        // create Stacks, Hands etc.
    }

    public void startGameRound() {
        // main game loop
    }

    private boolean playCard(Card card) {
        // put card on stack
        return true;
    }

    private void drawCardFromStack(Hand playersHand, int amount) {
        // moves #amount cards from Stack to players hand
    }

    private boolean moveCard(Card card, Pile pileA, Pile pileB) {
        // moves card from pileA to pileB
        return true;
    }

    private Card takeRandomCard(Pile pile) {
        // takes random card from pile and returns it. Do not remove card
        return new NumberCard(Color.BLACK, 5);
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
        //goes through each players hand and looks if there are 0 cards
        return true;
    }

    private void performTurn() {
        //performs turn
    }

    private void performEvent() {
        //performs event
    }

}
