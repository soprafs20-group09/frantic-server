package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Pile;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class MarketEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;
    private final Pile<Card> drawStack;
    private final int seconds;

    public MarketEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
        this.drawStack = gameRound.getDrawStack();
        this.seconds = 15;
    }

    public String getName() {
        return "market";
    }

    public void performEvent() {
        Player currentPlayer = this.gameRound.getCurrentPlayer();
        List<Card> cards = new ArrayList<>();
        List<Boolean> disabled = new ArrayList<>();
        for (int i = 0; i < this.listOfPlayers.size(); i++) {
            if (this.drawStack.size() > 0) {
                cards.add(this.drawStack.pop());
                disabled.add(false);
            }
            else {
                this.gameRound.onRoundOver(true);
                return;
            }
        }
        Card[] cardArray = cards.toArray(new Card[0]);
        Boolean[] disabledArray = disabled.toArray(new Boolean[0]);
        // send packet to first player
        int numOfPlayers = this.listOfPlayers.size();
        int initiatorIndex = this.listOfPlayers.indexOf(currentPlayer);

        Player firstPlayer = this.listOfPlayers.get((initiatorIndex + 1) % numOfPlayers);
        this.gameService.sendAttackTurn(this.gameRound.getLobbyId(), firstPlayer.getUsername());
        this.gameService.sendMarketWindow(this.gameRound.getLobbyId(), firstPlayer, cardArray, disabledArray);
        this.gameRound.setMarketList(cardArray, disabledArray);
        this.gameService.sendTimer(this.gameRound.getLobbyId(), seconds);
        this.gameRound.startMarketTimer(seconds, firstPlayer);
    }

    public String getMessage() {
        return "Choose one of these incredibly awesome cards for the great price of only 0$!";
    }
}
