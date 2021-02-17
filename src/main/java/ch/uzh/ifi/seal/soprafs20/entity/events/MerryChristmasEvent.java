package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.TurnDuration;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.List;

public class MerryChristmasEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;

    public MerryChristmasEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
    }

    public String getName() {
        return "merry-christmas";
    }

    public void performEvent() {

        int maxHand = 0;
        for (Player player : this.listOfPlayers) {
            int hand = player.getHandSize();
            if (hand > maxHand) {
                maxHand = hand;
            }
        }
        int baseValue = 20 + (maxHand % 20) * 2;
        int seconds = this.gameRound.getTurnDuration() == TurnDuration.NORMAL ? baseValue : baseValue * 2;

        this.gameService.sendEventActionResponse(this.gameRound.getLobbyId(), this.getName());
        if (this.gameRound.getTurnDuration() != TurnDuration.INFINITE) {
            this.gameService.sendTimer(this.gameRound.getLobbyId(), seconds);
            this.gameRound.startMerryChristmasTimer(seconds);
        }
    }

    public String getMessage() {
        return "Merry christmas everyone! It's that time of the year again. Give presents to your loved ones!";
    }
}
