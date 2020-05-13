package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TornadoEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;
    private final List<Card> tornadoList;

    public TornadoEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
        this.tornadoList = new ArrayList<>();
    }

    public String getName() {
        return "tornado";
    }

    public void performEvent() {
        // collect cards
        for (Player player : this.listOfPlayers) {
            while (player.getHandSize() > 0) {
                this.tornadoList.add(player.popCard());
            }
        }
        Collections.shuffle(this.tornadoList);
        this.gameService.sendAnimationSpeed(this.gameRound.getLobbyId(), 0);
        this.gameRound.sendCompleteGameState();

        // redistribute cards
        int i = 0;
        while (!this.tornadoList.isEmpty()) {
            this.listOfPlayers.get(i).pushCardToHand(this.tornadoList.remove(0));
            i = ++i % this.listOfPlayers.size();
        }
        this.gameService.sendAnimationSpeed(this.gameRound.getLobbyId(), 500);
        this.gameRound.sendCompleteGameState();

        this.gameRound.finishTurn();
    }

    public String getMessage() {
        return "Oh no! A tornado whirled all the cards around! Looks like we have to redistribute them!";
    }
}
