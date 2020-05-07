package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class MarketEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;

    public MarketEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
    }

    public String getName() {
        return "market";
    }

    public void performEvent() {

    }

    public String getMessage() {
        return "Choose one of these incredibly awesome cards for the great price of only 0$!";
    }
}
