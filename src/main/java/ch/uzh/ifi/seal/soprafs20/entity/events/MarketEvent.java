package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class MarketEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;
    private final Pile<Card> drawStack;
    private final Player currentPlayer;
    private final int seconds;

    public MarketEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
        this.drawStack = gameRound.getDrawStack();
        this.currentPlayer = gameRound.getCurrentPlayer();
        this.seconds = 15;
    }

    public String getName() {
        return "market";
    }

    public void performEvent() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < this.listOfPlayers.size(); i++) {
            if (this.drawStack.size() > 0) {
                cards.add(this.drawStack.pop());
            }
            else {
                this.gameRound.onRoundOver();
                return;
            }
        }
        // send packet to first player
        int numOfPlayers = this.listOfPlayers.size();
        int initiatorIndex = this.listOfPlayers.indexOf(currentPlayer);

        Player firstPlayer = this.listOfPlayers.get((initiatorIndex + 1) % numOfPlayers);
        this.gameService.sendMarketWindow(this.gameRound.getLobbyId(), firstPlayer, cards);
        this.gameRound.setMarketList(cards);
        this.gameService.sendTimer(this.gameRound.getLobbyId(), seconds);
        this.gameRound.startMarketTimer(seconds, firstPlayer);
    }

    public String getMessage() {
        return "Choose one of these incredibly awesome cards for the great price of only 0$!";
    }
}
