package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.List;

public class ThirdTimeLuckyEvent implements Event {

    private final GameRound gameRound;
    private final List<Player> listOfPlayers;

    public ThirdTimeLuckyEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.listOfPlayers = gameRound.getListOfPlayers();
    }

    public String getName() {
        return "third-time-lucky";
    }

    public void performEvent() {
        for (Player player : this.listOfPlayers) {
            this.gameRound.drawCardFromStack(player, 3);
        }

        this.gameRound.sendCompleteGameState();
        this.gameRound.finishTurn();
    }

    public String getMessage() {
        return "Three cards for everyone!";
    }
}
