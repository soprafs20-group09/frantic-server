package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class ExpansionEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;
    private Player currentPlayer;

    public ExpansionEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
    }

    public String getName() {
        return "expansion";
    }

    public void performEvent() {
        this.currentPlayer = this.gameRound.getCurrentPlayer();

        int numOfPlayers = this.listOfPlayers.size();
        int initiatorIndex = this.listOfPlayers.indexOf(currentPlayer);

        for (int i = 1; i <= numOfPlayers; i++) {
            Player playerOfInterest = this.listOfPlayers.get((initiatorIndex + i) % numOfPlayers);
            this.gameRound.drawCardFromStack(playerOfInterest, i);
        }

        this.gameRound.sendCompleteGameState();
        this.gameRound.finishTurn();
    }

    public String getMessage() {
        return "Cards are selling like hot cakes! Grab one or two or three ...";
    }
}
