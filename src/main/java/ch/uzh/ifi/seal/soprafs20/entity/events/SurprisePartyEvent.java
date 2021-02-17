package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.TurnDuration;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

public class SurprisePartyEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;

    public SurprisePartyEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
    }

    public String getName() {
        return "surprise-party";
    }

    public void performEvent() {
        this.gameService.sendEventActionResponse(this.gameRound.getLobbyId(), this.getName());
        if (gameRound.getTurnDuration() == TurnDuration.NORMAL) {
            this.gameService.sendTimer(this.gameRound.getLobbyId(), 30);
            this.gameRound.startSurprisePartyTimer(30);
        } else if (gameRound.getTurnDuration() == TurnDuration.LONG) {
            this.gameService.sendTimer(this.gameRound.getLobbyId(), 60);
            this.gameRound.startSurprisePartyTimer(60);
        }

    }

    public String getMessage() {
        return "Surprise another player by gifting them one of your cards!";
    }
}
