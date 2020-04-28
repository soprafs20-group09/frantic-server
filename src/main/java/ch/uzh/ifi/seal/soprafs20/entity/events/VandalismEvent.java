package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Pile;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

import java.util.List;

public class VandalismEvent implements Event {

    private final List<Player> listOfPlayers;
    private Pile<Card> discardPile;

    public VandalismEvent(List<Player> playerList, Pile<Card> discardPile) {
        this.listOfPlayers = playerList;
        this.discardPile = discardPile;
    }

    public String getName() {
        return "vandalism";
    }

    public void performEvent() {

        int index = 1;
        Card relevant = this.discardPile.peekN(index);
        while (relevant.getColor().equals(Color.BLACK) || relevant.getColor().equals(Color.MULTICOLOR)) {
            relevant = this.discardPile.peekN(index++);
        }

        for (Player player : this.listOfPlayers) {
            for (int i = player.getHandSize() - 1; i >= 0; i--) {
                if (player.peekCard(i).getColor().equals(relevant.getColor())) {
                    this.discardPile.push(player.popCard(i));
                }
            }
        }
    }

    public String getMessage() {
        return "Let all your anger out! You can discard all cards of the last played color! Do it! Just smash them on the discard Pile!";
    }
}
