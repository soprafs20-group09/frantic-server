package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.cards.NumberCard;
import ch.uzh.ifi.seal.soprafs20.entity.events.Event;

import java.util.*;

public class GameRound {

    private List<Player> listOfPlayers;
    private Player currentPlayer;
    private boolean turnIsRunning;
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

    //creates Piles & player hands
    public void initializeGameRound() {
        this.drawStack = new DrawStack();
        this.discardPile = new DiscardPile();

        //move 7 initial cards to player hands
        for (Player player : listOfPlayers) {
            for (int i = 1; i<=7; i++) {
                //player.pushCardToHand(drawStack.pop())/
            }
        }
        //move initial card to discardPile
        this.discardPile.push(this.drawStack.pop());
    }

    private void sendGameState() {
        //TODO: send current Game state to all players
        //TODO: send Hand to each player individually
    }

    public void startGameRound() {
        initializeGameRound();
        sendGameState();
        startTurn();
    }

    private void prepareNewTurn() {
        if (!isRoundOver()) {
            changePlayer();
            sendGameState();
            startTurn();
        } else {
            onRoundOver();
        }
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
        prepareNewTurn();
    }

    //this method is called when the timer runs out
    private  void abortTurn() {
        timer.cancel();
        drawCardFromStack(currentPlayer, 1);
        turnIsRunning = false;
        prepareNewTurn();
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

    /*
    private void playCard(Player player, int index) {
        Card uppermostCard = (Card)discardPile.peek();
        Card cardToPlay = player.getHand().peek();
        if (player == currentPlayer && cardToPlay != null) {
            if (cardToPlay.isPlayable(uppermostCard) {
                discardPile.push(player.getHand().pop(index);
                finishTurn();
            }
        } else {
            // needed later for special cards
        }
    }
     */

    // moves #amount cards from Stack to players hand
    private void drawCardFromStack(Player player, int amount) {
        for (int i = 1; i<=amount; i++) {
            //player.getHand().push(drawStack.pop())/
        }
        //TODO: Send new Hand state to player
    }

    private Card takeRandomCard(Player player) {
        Random r = new Random();
        int handSize = player.getHandSize();
        int index = r.nextInt(handSize);
        //return player.getHand().pop(index)
        return new NumberCard(Color.BLACK, 3, 1); //just a random example
    }

    private void performEvent() {
        Event event = (Event)events.remove(0);
        //TODO: Send event information to clients
        event.performEvent();
    }

    private List<Integer> getPlayableCards (Player player) {
        //List playableCards = player.getPlayableCards(this.discardPile.peek())
        return new ArrayList<>();
    }

    private Map<Player, Integer> getHandSizes() {
        Map<Player, Integer> mappedPlayers = new HashMap<>();
        for (Player player : listOfPlayers) {
            int handSize = player.getHandSize();
            mappedPlayers.put(player, handSize);
        }
        return mappedPlayers;
    }

    private void changePlayer() {
        int playersIndex = this.listOfPlayers.indexOf(this.currentPlayer);
        playersIndex = (playersIndex + 1)%listOfPlayers.size();
        this.currentPlayer = listOfPlayers.get(playersIndex);
    }

    //a Gameround is over, if someone has 0 cards in his hand (and no nice-try was played)
    // or in case of the time-bomb event, if the 3 rounds are played
    private boolean isRoundOver() { return (getHandSizes().containsValue(0) || remainingTurns == 0); }

    private void onRoundOver() {
        //TODO: Somehow call endGameRound in Game class
    }

    //If a player loses connection he/she is removed from the listOfPlayers
    public void playerLostConnection(Player player) {
        if (player == currentPlayer) {
            timer.cancel();
            turnIsRunning = false;
            prepareNewTurn();
        }
        listOfPlayers.remove(player);
    }
}