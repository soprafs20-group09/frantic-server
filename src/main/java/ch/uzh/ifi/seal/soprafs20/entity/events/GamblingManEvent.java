package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class GamblingManEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;
    private final Pile<Card> discardPile;

    public GamblingManEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
        this.discardPile = gameRound.getDiscardPile();
    }

    public String getName() {
        return "gambling-man";
    }

    public void performEvent() {
        int index = 1;
        Color relevant = this.discardPile.peekN(index).getColor();
        while (relevant.equals(Color.BLACK) || relevant.equals(Color.MULTICOLOR)) {
            relevant = this.discardPile.peekN(index++).getColor();
        }
        for (Player player : this.listOfPlayers) {
            List<Integer> cards = new ArrayList<>();
            for (int i = 0; i < player.getHandSize(); i++) {
                if (player.peekCard(i).getColor().equals(relevant)) {
                    cards.add(i);
                }
            }
            if (!cards.isEmpty()) {
                int[] playable = cards.stream().mapToInt(i -> i).toArray();
                this.gameService.sendGamblingMan(this.gameRound.getLobbyId(), player, playable);
            }
            // no such color
            else {
                this.gameRound.drawCardFromStack(player, 2);
            }
        }
    }

    public String getMessage() {
        return "It's time to gamble! Choose a number card of the last played color. The player with the highest digit has to take all of them. So choose wisely!";
    }
}
