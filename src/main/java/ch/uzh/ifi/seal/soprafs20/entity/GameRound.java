package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.actions.*;
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
        startTurnTimer(30);
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

    public void playCard(String identity, int index) {
        Player player = getPlayerByIdentity(identity);
        if (player != null) {
            Card uppermostCard = getRelevantCardOnDiscardPile();
            Card cardToPlay = player.peekCard(index);
            if (uppermostCard == null || cardToPlay == null || (!cardToPlay.isPlayableOn(uppermostCard) && (uppermostCard.getValue() != Value.FUCKYOU))) {
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
                    if (cardToPlay.getValue() == Value.FUCKYOU) {
                        finishTurn();
                    }
                    else if (cardToPlay.getValue() == Value.SECONDCHANCE) {
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
        if (player != null && player == currentPlayer && !this.hasCurrentPlayerMadeMove) {
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
        this.gameService.sendDrawAnimation(this.lobbyId, amount);
        this.gameService.sendHand(this.lobbyId, player);
    }

    //if the fuck-you card is the uppermost card, then the second card is considered to evaluate
    private Card getRelevantCardOnDiscardPile() {
        Card card = this.discardPile.peek();
        if (card == null) {
            return null;
        }
        if (card.getValue() == Value.FUCKYOU && this.discardPile.size() > 1) {
            return this.discardPile.peekSecond();
        }
        return this.discardPile.peek();
    }

    public void storeSkipAction(String identity, String username) {
        Player initiator = getPlayerByIdentity(identity);
        Player target = getPlayerByUsername(username);
        this.currentAction = new SkipAction(initiator, target);
        timer.cancel();
        prepareCounterAttack();
    }

    public void storeGiftAction(String identity, int[] cards, String username) {
        Player initiator = getPlayerByIdentity(identity);
        Player target = getPlayerByUsername(username);
        this.currentAction = new GiftAction(initiator, target, cards);
        timer.cancel();
        prepareCounterAttack();
    }

    public void storeExchangeAction(String identity, int[] cards, String username) {
        Player initiator = getPlayerByIdentity(identity);
        Player target = getPlayerByUsername(username);
        this.currentAction = new ExchangeAction(initiator, target, cards);
        timer.cancel();
        prepareCounterAttack();
    }

    public void storeFantasticAction(String identity, int value, Color color) {
        Player initiator = getPlayerByIdentity(identity);
        if (color == null) {
            this.currentAction = new FantasticAction(initiator, value, (DiscardPile) this.discardPile);
        }
        else {
            this.currentAction = new FantasticAction(initiator, color, (DiscardPile) this.discardPile);
        }
        timer.cancel();
        performAction();
    }

    public void storeFantasticFourAction(String identity, int value, Color color, Map<String, Integer> players) {
        Player initiator = getPlayerByIdentity(identity);
        Map<Player, Integer> distribution = new HashMap<>();
        for (Map.Entry<String, Integer> entry : players.entrySet()) {
            distribution.put(getPlayerByIdentity(entry.getKey()), entry.getValue());
        }
        if (color == null) {
            this.currentAction = new FantasticFourAction(initiator, distribution, value,
                    (DiscardPile) this.discardPile, (DrawStack) this.drawStack);
        }
        else {
            this.currentAction = new FantasticFourAction(initiator, distribution, color,
                    (DiscardPile) this.discardPile, (DrawStack) this.drawStack);
        }
        timer.cancel();
        prepareCounterAttack();
    }

    public void storeEqualityAction(String identity, Color color, String username) {
        Player initiator = getPlayerByIdentity(identity);
        Player target = getPlayerByUsername(username);
        this.currentAction = new EqualityAction(initiator, target, color, (DiscardPile) this.discardPile, (DrawStack) this.drawStack);
        timer.cancel();
        prepareCounterAttack();
    }

    private void performAction() {
        this.currentAction.perform();
        Player initiator = currentAction.getInitiator();
        Player[] targets = currentAction.getTargets();
        sendGameState();
        this.gameService.sendHand(this.lobbyId, initiator);
        for (Player target : targets) {
            this.gameService.sendHand(this.lobbyId, target);
        }
        finishTurn();
    }

    private void prepareCounterAttack() {
        for (Player player : listOfPlayers) {
            List<Integer> cards = player.hasCounterAttack();
            if (!cards.isEmpty()) {
                //TODO: Send counter attack opportunity
            }
        }
        startCounterAttackTimer(5);
    }

    private void performEvent() {
        Event event = this.events.remove(0);
        //TODO: Send event information to clients
        event.performEvent();
    }

    private int[] getPlayableCards(Player player) {
        return player.getPlayableCards(getRelevantCardOnDiscardPile());
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

    public void startTurnTimer(int seconds) {
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

    public void startCounterAttackTimer(int seconds) {
        int milliseconds = seconds * 1000;
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                performAction();
            }
        };
        this.timer.schedule(timerTask, milliseconds);
    }

    private Player getPlayerByIdentity(String identity) {
        for (Player p : listOfPlayers) {
            if (p.getIdentity().equals(identity)) {
                return p;
            }
        }
        return null;
    }

    private Player getPlayerByUsername(String username) {
        for (Player p : listOfPlayers) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }
        return null;
    }
}