package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.TurnDuration;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.List;

public class RecessionEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;
    private int amount;

    public RecessionEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
        this.amount = 1;
    }

    public String getName() {
        return "recession";
    }

    public void performEvent() {
        Player currentPlayer = this.gameRound.getCurrentPlayer();
        int numOfPlayers = this.listOfPlayers.size();
        int initiatorIndex = this.listOfPlayers.indexOf(currentPlayer);
        for (int i = 1; i <= numOfPlayers; i++) {
            Player player = this.listOfPlayers.get((initiatorIndex + i) % numOfPlayers);
            this.gameService.sendRecession(this.gameRound.getLobbyId(), player, Math.min(this.amount, player.getHandSize()));
            this.amount++;
        }
        if (this.gameRound.getTurnDuration() == TurnDuration.NORMAL) {
            this.gameService.sendTimer(this.gameRound.getLobbyId(), 30);
            this.gameRound.startRecessionTimer(30);
        } else if (this.gameRound.getTurnDuration() == TurnDuration.LONG) {
            this.gameService.sendTimer(this.gameRound.getLobbyId(), 60);
            this.gameRound.startRecessionTimer(60);
        }

    }

    public String getMessage() {
        return "Card Stocks are going down! Dispose one or two or three ...";
    }
}
