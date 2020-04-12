package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;
import ch.uzh.ifi.seal.soprafs20.entity.events.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;

import java.util.*;

public class Game {

    private String lobbyId;
    private GameRound currentGameRound;
    private GameLength gameDuration;
    private List<Player> listOfPlayers;
    private int maxPoints;
    private Player firstPlayer;
    private List<Player> winners;
    private List<Event> events;
    private Timer timer;

    private GameService gameService;

    public Game(String lobbyId, GameLength gameDuration) {
        this.gameService = GameService.getInstance();
        this.lobbyId = lobbyId;
        this.gameDuration = gameDuration;
        this.listOfPlayers = PlayerService.getInstance().getPlayersInLobby(lobbyId);
        this.firstPlayer = listOfPlayers.get(0);
        this.maxPoints = calculateMaxPoints();
        this.events = new ArrayList<>();
    }

    public GameRound getCurrentGameRound() {
        return this.currentGameRound;
    }

    public void startGame() {
        initEvents();
        shuffleEvents();
        this.currentGameRound = new GameRound(this, this.lobbyId, this.listOfPlayers, this.firstPlayer, this.events);
        this.currentGameRound.startGameRound();
    }

    private void startNewGameRound() {
        shuffleEvents();
        this.gameService.sendStartGameRound(this.lobbyId);
        this.currentGameRound = new GameRound(this, this.lobbyId, this.listOfPlayers, this.firstPlayer, this.events);
        this.currentGameRound.startGameRound();
    }

    public void endGameRound() {
        updatePoints();
        removeCardsFromHands();
        if (!gameOver()) {
            //TODO: Send end of round package
            startTimer(15, false);
        }
        else {
            //TODO: Send end of game package
            startTimer(15, true);
        }
    }

    private void updatePoints() {
        int maxPoints = 0;
        Player playerWithMaxPoints = this.firstPlayer; //to make sure playerWithMaxPoints is initialized in all cases
        for (Player player : this.listOfPlayers) {
            int playersPoints = player.calculatePoints();
            player.setPoints(player.getPoints() + playersPoints);
            if (playersPoints >= maxPoints) {
                maxPoints = playersPoints;
                playerWithMaxPoints = player;
            }
        }
        setFirstPlayer(playerWithMaxPoints);
    }

    //Removes all cards from the players hands
    private void removeCardsFromHands() {
        for (Player player : this.listOfPlayers) {
            player.clearHand();
        }
    }

    private Map<String, Integer> getScores() {
        Map<String, Integer> mappedPlayers = new HashMap<>();
        for (Player player : this.listOfPlayers) {
            int points = player.getPoints();
            mappedPlayers.put(player.getUsername(), points);
        }
        return mappedPlayers;
    }

    private boolean gameOver() {
        Map<String, Integer> scores = getScores();
        if (Collections.max(scores.values()) >= this.maxPoints) {
            calculateWinners(scores);
            return true;
        }
        return false;
    }

    private void calculateWinners(Map<String, Integer> scores) {
        //Calculate smallest number of points some player has
        int minPoints = Collections.min(scores.values());

        //Add all players with minPoints to winners-list
        for (Player player : this.listOfPlayers) {
            if (player.getPoints() == minPoints) {
                this.winners.add(player);
            }
        }
    }

    //The first player is the player to the right of the player who shuffles the cards
    private void setFirstPlayer(Player playerWhoShuffleCards) {
        int playersIndex = this.listOfPlayers.indexOf(playerWhoShuffleCards);
        playersIndex = (playersIndex + 1) % this.listOfPlayers.size();
        this.firstPlayer = this.listOfPlayers.get(playersIndex);
    }

    private int calculateMaxPoints() {
        int numOfPlayers = this.listOfPlayers.size();
        if (numOfPlayers <= 4) {
            if (this.gameDuration == GameLength.SHORT) {
                return 137;
            }
            else if (this.gameDuration == GameLength.MEDIUM) {
                return 154;
            }
            else {
                return 179;
            }
        }
        else {
            if (this.gameDuration == GameLength.SHORT) {
                return 113;
            }
            else if (this.gameDuration == GameLength.MEDIUM) {
                return 137;
            }
            else {
                return 154;
            }
        }
    }

    //If a player loses connection he/she is removed from the listOfPlayers
    public void playerLostConnection(Player player) {
        //in case the connection is lost during initialization
        if (this.currentGameRound != null) {
            this.currentGameRound.playerLostConnection(player);
        }
        this.listOfPlayers.remove(player);
    }

    public void startTimer(int seconds, boolean gameOver) {
        int milliseconds = seconds * 1000;
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (gameOver) {
                    //TODO: End all Websocket connections of a lobby
                }
                else {
                    startNewGameRound();
                }
            }
        };
        this.timer.schedule(timerTask, milliseconds);
    }

    private void shuffleEvents() {
        Collections.shuffle(this.events);
    }

    private void initEvents() {
        //initialize all Events
        Event charity = new CharityEvent();
        Event communism = new CommunismEvent();
        Event doomsday = new DoomsdayEvent();
        Event earthquake = new EarthquakeEvent();
        Event expansion = new ExpansionEvent();
        Event finishLine = new FinishLineEvent();
        Event fridayTheThirteenth = new FridayTheThirteenthEvent();
        Event gamblingMan = new GamblingManEvent();
        Event market = new MarketEvent();
        Event matingSeason = new MatingSeasonEvent();
        Event merryChristmas = new MerryChristmasEvent();
        Event mexicanStandoff = new MexicanStandoffEvent();
        Event recession = new RecessionEvent();
        Event robinHood = new RobinHoodEvent();
        Event surpriseParty = new SurprisePartyEvent();
        Event theAllSeeingEye = new TheAllSeeingEyeEvent();
        Event thirdTimeLucky = new ThirdTimeLuckyEvent();
        Event timeBomb = new TimeBombEvent();
        Event tornado = new TornadoEvent();
        Event vandalism = new VandalismEvent();

        //add them to the list of all events
        this.events.add(charity);
        this.events.add(communism);
        this.events.add(doomsday);
        this.events.add(earthquake);
        this.events.add(expansion);
        this.events.add(finishLine);
        this.events.add(fridayTheThirteenth);
        this.events.add(gamblingMan);
        this.events.add(market);
        this.events.add(merryChristmas);
        this.events.add(matingSeason);
        this.events.add(mexicanStandoff);
        this.events.add(recession);
        this.events.add(robinHood);
        this.events.add(surpriseParty);
        this.events.add(theAllSeeingEye);
        this.events.add(thirdTimeLucky);
        this.events.add(timeBomb);
        this.events.add(tornado);
        this.events.add(vandalism);
    }
}
