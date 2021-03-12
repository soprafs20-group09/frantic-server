package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.TurnDuration;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

public class SurprisePartyEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final int seconds;

    public SurprisePartyEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.seconds = gameRound.getTurnDuration().getValue();
    }

    public String getName() {
        return "surprise-party";
    }

    public void performEvent() {
        this.gameService.sendEventActionResponse(this.gameRound.getLobbyId(), this.getName());
        this.gameService.sendTimer(this.gameRound.getLobbyId(), seconds);
        if (gameRound.getTurnDuration() != TurnDuration.OFF) {
            this.gameRound.startSurprisePartyTimer(seconds);
        }

    }

    public String getMessage() {
        return "Surprise another player by gifting them one of your cards!";
    }
}
