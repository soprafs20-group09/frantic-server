package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.cards.NumberCard;

import java.util.*;

public class GameRound {

    private List<Player> listOfPlayers;
    private Player currentPlayer;
    private boolean turnIsRunning;
    private boolean remainingTime;
    private Timer timer;
    private boolean timebomb; // indicates if the timebomb-event is currently running
    private int remainingTurns;
    private List events;
    private ActionStack actionStack;
    private Pile drawStack;
    private Pile discardPile;


    public GameRound(List<Player> listOfPlayers, Player firstPlayer, List events) {
        this.listOfPlayers = listOfPlayers;
        this.currentPlayer = firstPlayer;
        this.actionStack = new ActionStack();
        this.remainingTurns = -1; //indicates that there is no limit
        this.events = events;
    }

    //creates card stacks & player hands
    public void initializeGameRound() {
        this.drawStack = new DrawStack();
        this.discardPile = new DiscardPile();

        //move 7 initial cards to player hands
        for (Player player : listOfPlayers) {
            drawCardFromStack(player, 7);
        }
        //move initial card to discardPile
        this.discardPile.push(this.drawStack.pop());
    }

    public void startGameRound() {
        initializeGameRound();
        startTurn();
    }

    private void startTurn() {
        //TODO: send start turn package with this.currentPlayer as content
        //TODO: playableCards package to currentPlayer with getPlayableCards(currentPlayer) as content
        startTimer(30);
        this.turnIsRunning = true;
    }

    private void finishTurn() {
        timer.cancel();
        this.turnIsRunning = false;
    }

    private List<Integer> getPlayableCards (Player player) {
        List playableCards = new ArrayList();
        //player.getPlayableCards(this.discardPile.peek())
        return playableCards;
    }

    private boolean playCard(Player player, Card card) {
        Card uppermostCard = (Card)discardPile.peek();
        if (player == currentPlayer) {
            if (card.isPlayable(uppermostCard) && moveCardFromPlayerToDiscardPile(player, card)) {
                return true;
            }
        } else {
            // needed later for special cards
        }
        return false;
    }

    // moves #amount cards from Stack to players hand
    private void drawCardFromStack(Player player, int amount) {
        for (int i = 1; i<=amount; i++) {
            //player.pushCardToHand(drawStack.pop());
        }
    }

    private boolean moveCardFromPlayerToDiscardPile(Player player, Card card) {
        // moves card from Players hand to discardPile
        // return true if possible
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

    //a Gameround is over, if someone has 0 cards in his hand (and no nice-try was played)
    // or in case of the time-bomb event, if the 3 rounds are played
    private boolean isRoundOver() {
        return (getHandSizes().containsValue(0) || remainingTurns == 0);
    }

    private Map<Player, Integer> getHandSizes() {
        Map<Player, Integer> mappedPlayers = new HashMap<>();
        for (Player player : listOfPlayers) {
            int handSize = player.getHandSize();
            mappedPlayers.put(player, handSize);
        }
        return mappedPlayers;
    }

    private void performEvent() {
        //performs event
    }

    private void removeCardsFromHands() {
        // makes sure, that after each round, all players have 0 cards
    }

    //this method is called when the timer runs out
    private  void abortTurn() {
        timer.cancel();
        drawCardFromStack(currentPlayer, 1);
        turnIsRunning = false;
        changePlayer();
    }

    public void startTimer(int seconds) {
        int milliseconds = seconds * 1000;
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                abortTurn();
            }
        };
        timer.schedule(timerTask, milliseconds);
    }

}
