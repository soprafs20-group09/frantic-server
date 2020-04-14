package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.entity.events.Event;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.*;

public class GameRound {

    private Game game;
    private String lobbyId;
    private List<Player> listOfPlayers;
    private Player currentPlayer;
    private boolean hasCurrentPlayerMadeMove;
    private Timer timer;
    private boolean timebomb; // indicates if the timebomb-event is currently running
    private int turnNumber;
    private int remainingTurns;
    private List events;
    private ActionStack actionStack;
    private Pile<Card> drawStack;
    private Pile<Card> discardPile;

    private final GameService gameService;


    public GameRound(Game game, String lobbyId, List<Player> listOfPlayers, Player firstPlayer, List events) {
        this.game = game;
        this.lobbyId = lobbyId;
        this.listOfPlayers = listOfPlayers;
        this.currentPlayer = firstPlayer;
        this.gameService = GameService.getInstance();
        this.actionStack = new ActionStack();
        this.turnNumber = 0;
        this.remainingTurns = -1; //indicates that there is no limit
        this.events = events;
    }

    //creates Piles & player hands
    public void initializeGameRound() {
        this.drawStack = new DrawStack();
        this.discardPile = new DiscardPile();

        //move 7 initial cards to player hands
        for (Player player : this.listOfPlayers) {
            for (int i = 1; i <= 7; i++) {
                Card card = this.drawStack.pop();
                player.pushCardToHand(card);
            }
        }
        //move initial card to discardPile
        this.discardPile.push(this.drawStack.pop());
    }

    private void sendGameState() {
        for (Player player : this.listOfPlayers) {
            this.gameService.sendHand(this.lobbyId, player);
        }
        this.gameService.sendGameState(this.lobbyId, this.discardPile.peek(), this.listOfPlayers);
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
            this.hasCurrentPlayerMadeMove = false;
            startTurn();
        }
        else {
            onRoundOver();
        }
    }

    private void startTurn() {
        this.turnNumber += 1;
        this.gameService.sendStartTurn(this.lobbyId, this.currentPlayer.getUsername(), 30, turnNumber);
        this.gameService.sendPlayableCards(this.lobbyId, this.currentPlayer, getPlayableCards(this.currentPlayer));
        startTimer(30);
    }

    private void finishTurn() {
        if (!this.hasCurrentPlayerMadeMove) {
            drawCardFromStack(this.currentPlayer, 1);
        }
        this.timer.cancel();
        //return empty playable cards after turn finished
        this.gameService.sendPlayableCards(this.lobbyId, this.currentPlayer, new int[0]);
        prepareNewTurn();
    }

    public void startTimer(int seconds) {
        int milliseconds = seconds * 1000;
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                finishTurn();
            }
        };
        this.timer.schedule(timerTask, milliseconds);
    }

    public void playCard(String identity, int index) {
        Player player = getPlayerByIdentity(identity);
        Card uppermostCard = this.discardPile.peek();
        Card cardToPlay = player.peekCard(index);
        if (cardToPlay == null || !cardToPlay.isPlayable(uppermostCard)) {
            return;
        }
        if (player == this.currentPlayer) {
            this.discardPile.push(player.popCard(index));
            this.hasCurrentPlayerMadeMove = true;
            finishTurn();
        }
        else {
            // needed later for Counter attack & nice try
        }
    }

    // in a turn, the current player can choose to draw a card

    public void currentPlayerDrawCard() {
        drawCardFromStack(this.currentPlayer, 1);
        this.hasCurrentPlayerMadeMove = true;
    }
    // moves #amount cards from Stack to players hand

    private void drawCardFromStack(Player player, int amount) {
        for (int i = 1; i <= amount; i++) {
            //if the drawStack is empty and a player has to draw a card, the gameround is over
            if (this.drawStack.size() <= 0) {
                this.timer.cancel();
                onRoundOver();
            }
            player.pushCardToHand(this.drawStack.pop());
        }
        this.gameService.sendHand(this.lobbyId, player);
        this.gameService.sendPlayableCards(this.lobbyId, player, getPlayableCards(player));
    }
    private Card takeRandomCard(Player player) {
        Random r = new Random();
        int handSize = player.getHandSize();
        int index = r.nextInt(handSize);
        return player.popCard(index);
    }

    private void performEvent() {
        Event event = (Event) this.events.remove(0);
        //TODO: Send event information to clients
        event.performEvent();
    }

    private int[] getPlayableCards(Player player) {
        return player.getPlayableCards(this.discardPile.peek());
    }

    private Map<Player, Integer> getHandSizes() {
        Map<Player, Integer> mappedPlayers = new HashMap<>();
        for (Player player : this.listOfPlayers) {
            int handSize = player.getHandSize();
            mappedPlayers.put(player, handSize);
        }
        return mappedPlayers;
    }

    private void changePlayer() {
        int playersIndex = this.listOfPlayers.indexOf(this.currentPlayer);
        playersIndex = (playersIndex + 1) % this.listOfPlayers.size();
        this.currentPlayer = this.listOfPlayers.get(playersIndex);

        //go to the next player, if the current player is skipped
        if (this.currentPlayer.isBlocked()) {
            this.currentPlayer.setBlocked(false);
            changePlayer();
        }
    }

    //A player can only be skipped, if he/she was not skipped before

    public boolean skip(Player player) {
        if (player.isBlocked()) {
            return false;
        }
        player.setBlocked(true);
        return true;
    }
    //a Gameround is over, if someone has 0 cards in his hand (and no nice-try was played)
    // or in case of the time-bomb event, if the 3 rounds are played

    private boolean isRoundOver() {
        return (getHandSizes().containsValue(0) || this.remainingTurns == 0);
    }
    private void onRoundOver() {
        this.game.endGameRound();
    }

    public void playerLostConnection(Player player) {
        if (player == this.currentPlayer) {
            this.timer.cancel();
            prepareNewTurn();
        }
        sendGameState();
    }

    private Player getPlayerByIdentity(String identity) {
        for (Player p : listOfPlayers) {
            if (p.getIdentity().equals(identity)) {
                return p;
            }
        }
        return null;
    }
}