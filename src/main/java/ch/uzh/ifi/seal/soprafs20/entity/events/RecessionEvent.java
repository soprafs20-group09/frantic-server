package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RecessionEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;
    private final Player currentPlayer;
    private int amount;
    private final int seconds;

    public RecessionEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
        this.currentPlayer = gameRound.getCurrentPlayer();
        this.amount = 1;
        this.seconds = 30;
    }

    public String getName() {
        return "recession";
    }

    public void performEvent() {
        int numOfPlayers = this.listOfPlayers.size();
        int initiatorIndex = this.listOfPlayers.indexOf(this.currentPlayer);
        for (int i = 1; i <= numOfPlayers; i++) {
            Player player = this.listOfPlayers.get((initiatorIndex + i) % numOfPlayers);
            this.gameService.sendRecession(this.gameRound.getLobbyId(), player, Math.min(this.amount, player.getHandSize()));
            this.amount++;
        }
        this.gameService.sendTimer(this.gameRound.getLobbyId(), seconds);
        this.gameRound.startRecessionTimer(seconds);
    }

    public String getMessage() {
        return "Card Stocks are going down! Dispose one or two or three ...";
    }
}
