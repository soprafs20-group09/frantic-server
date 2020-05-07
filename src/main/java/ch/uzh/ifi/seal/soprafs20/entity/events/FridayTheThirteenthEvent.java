package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class FridayTheThirteenthEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;

    public FridayTheThirteenthEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
    }

    public String getName() {
        return "friday-the-13th";
    }

    public void performEvent() {
        this.gameRound.finishTurn();
    }

    public String getMessage() {
        return "It’s Friday, the Thirteenth. A hook-handed murderer is among us! But just in the movies, it’s a totally boring, normal Friday, nothing weird happens. The game round continues without further ado.";
    }
}
