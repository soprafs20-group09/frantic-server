package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class MerryChristmasEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final int seconds;

    public MerryChristmasEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.seconds = 30;
    }

    public String getName() {
        return "merry-christmas";
    }

    public void performEvent() {
        this.gameService.sendEventActionResponse(this.gameRound.getLobbyId(), this.getName());
        this.gameService.sendTimer(this.gameRound.getLobbyId(), seconds);
        this.gameRound.startMerryChristmasTimer(seconds);
    }

    public String getMessage() {
        return "Merry christmas everyone! It's that time of the year again. Give presents to your loved ones!";
    }
}
