package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.actions.Action;
import ch.uzh.ifi.seal.soprafs20.entity.events.Event;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;

import java.util.*;

public class GameRound {

    private Game game;
    private String lobbyId;
    private List<Player> listOfPlayers;
    private Player currentPlayer;
    private boolean hasCurrentPlayerMadeMove;
    private Timer timer;
    private boolean timeBomb; // indicates if the timeBomb-event is currently running
    private int turnNumber;
    private int remainingTurns;
    private List<Event> events;
    private Pile<Card> drawStack;
    private Pile<Card> discardPile;
    private Action currentAction;

    private final GameService gameService;


    public GameRound(Game game, String lobbyId, List<Player> listOfPlayers, Player firstPlayer, List<Event> events) {
        this.game = game;
        this.lobbyId = lobbyId;
        this.listOfPlayers = listOfPlayers;
        this.currentPlayer = firstPlayer;
        this.gameService = GameService.getInstance();
        this.turnNumber = 0;
        this.remainingTurns = -1; //indicates that there is no limit
        this.events = events;
        this.currentAction = null;
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

    private void sendInitialGameState() {
        for (Player player : this.listOfPlayers) {
            this.gameService.sendHand(this.lobbyId, player);
        }
        sendGameState();
    }

    private void sendGameState() {
        this.gameService.sendGameState(this.lobbyId, this.discardPile.peek(), this.listOfPlayers);
    }

    public void startGameRound() {
        initializeGameRound();
        sendInitialGameState();
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

    public void finishTurn() {
        if (!this.hasCurrentPlayerMadeMove) {
            drawCardFromStack(this.currentPlayer, 1);
            this.gameService.sendChatPlayerMessage(this.lobbyId, "drew card", currentPlayer.getUsername());
        }
        this.timer.cancel();
        //return empty playable cards after turn finished
        this.gameService.sendPlayableCards(this.lobbyId, this.currentPlayer, new int[0]);
        prepareNewTurn();
    }

    private void finishSecondChance() {
        this.hasCurrentPlayerMadeMove = false;
        this.gameService.sendHand(this.lobbyId, this.currentPlayer);
        sendGameState();
        this.gameService.sendPlayableCards(this.lobbyId, this.currentPlayer, this.getPlayableCards(this.currentPlayer));
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
        if (player != null) {
            Card uppermostCard = this.discardPile.peek();
            Card cardToPlay = player.peekCard(index);
            if (cardToPlay == null || !cardToPlay.isPlayable(uppermostCard)) {
                return;
            }
            if (player == this.currentPlayer) {
                cardToPlay = player.popCard(index);
                this.discardPile.push(cardToPlay);
                this.gameService.sendHand(this.lobbyId, player);
                this.hasCurrentPlayerMadeMove = true;

                if (cardToPlay.getType() == Type.NUMBER) {
                    this.gameService.sendChatPlayerMessage(this.lobbyId, "played " + FranticUtils.getStringRepresentationOfNumberCard(cardToPlay), player.getUsername());
                    finishTurn();
                }
                else if (cardToPlay.getType() == Type.SPECIAL) {
                    this.gameService.sendChatPlayerMessage(this.lobbyId, "played " + FranticUtils.getStringRepresentation(cardToPlay.getValue()), player.getUsername());
                    if (cardToPlay.getValue() == Value.SECONDCHANCE) {
                        finishSecondChance();
                    }
                    else {
                        this.gameService.sendActionResponse(this.lobbyId, player, cardToPlay);
                    }
                }
            }
            else {
                // needed later for Counter attack & nice try
            }
        }
    }

    // in a turn, the current player can choose to draw a card
    public void currentPlayerDrawCard(String identity) {
        Player player = getPlayerByIdentity(identity);
        if (player == currentPlayer && !this.hasCurrentPlayerMadeMove) {
            drawCardFromStack(this.currentPlayer, 1);
            this.gameService.sendChatPlayerMessage(this.lobbyId, "drew card", currentPlayer.getUsername());
            this.gameService.sendPlayableCards(this.lobbyId, this.currentPlayer, getPlayableCards(this.currentPlayer));
            this.hasCurrentPlayerMadeMove = true;
        }
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
    }

    private void performEvent() {
        Event event = this.events.remove(0);
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
        this.listOfPlayers.remove(getPlayerByIdentity(player.getIdentity()));
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