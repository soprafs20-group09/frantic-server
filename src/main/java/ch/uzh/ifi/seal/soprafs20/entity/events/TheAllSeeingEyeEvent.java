package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.TurnDuration;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

public class TheAllSeeingEyeEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;

    public TheAllSeeingEyeEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
    }

    public String getName() {
        return "the-all-seeing-eye";
    }

    public void performEvent() {
        this.gameRound.setShowCards(true);
        if (this.gameRound.getTurnDuration() == TurnDuration.NORMAL) {
            this.gameService.sendTimer(this.gameRound.getLobbyId(), 30);
            this.gameRound.startAllSeeingEyeTimer(30);
        } else if (this.gameRound.getTurnDuration() == TurnDuration.LONG) {
            this.gameService.sendTimer(this.gameRound.getLobbyId(), 60);
            this.gameRound.startAllSeeingEyeTimer(60);
        }
    }

    public String getMessage() {
        return "You can't run! You can't hide! The all-seeing eye is here! Take a good look at everyone's cards!";
    }
}
