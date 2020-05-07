package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TheAllSeeingEyeEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final int seconds;

    public TheAllSeeingEyeEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        seconds = 30;
    }

    public String getName() {
        return "the-all-seeing-eye";
    }

    public void performEvent() {
        this.gameRound.setShowCards(true);
        this.gameService.sendTimer(this.gameRound.getLobbyId(), seconds);

        this.gameRound.startAllSeeingEyeTimer(seconds);
    }

    public String getMessage() {
        return "You can't run! You can't hide! The all-seeing eye is here! Take a good look at everyone cards!";
    }
}
