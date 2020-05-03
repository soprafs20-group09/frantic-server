package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Pile;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;

import java.util.ArrayList;
import java.util.List;

public class CharityEvent implements Event {

    private final List<Player> listOfPlayers;
    private final Player initiator;

    public CharityEvent(List<Player> listOfPlayers, Player currentPlayer) {
        this.listOfPlayers = listOfPlayers;
        this.initiator = currentPlayer;
    }

    public String getName() {
        return "charity";
    }

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:charity", this.getMessage()));

        int maxCards = 0;
        for (Player player : this.listOfPlayers) {
            if (player.getHandSize() > maxCards) {
                maxCards = player.getHandSize();
            }
        }
        List<Player> maxCardsPlayers = new ArrayList<>();
        for (Player player : this.listOfPlayers) {
            if (player.getHandSize() == maxCards) {
                maxCardsPlayers.add(player);
            }
        }

        int numOfPlayers = this.listOfPlayers.size();
        int initiatorIndex = this.listOfPlayers.indexOf(this.initiator);
        for (int i = 1; i <= numOfPlayers; i++) {
            Player playerOfInterest = this.listOfPlayers.get((initiatorIndex + i) % numOfPlayers);
            if (!maxCardsPlayers.contains(playerOfInterest)) {
                for (Player target : maxCardsPlayers) {
                    if (target.getHandSize() > 0) {
                        int random = FranticUtils.random.nextInt(target.getHandSize());
                        playerOfInterest.pushCardToHand(target.popCard(random));

                        chat.add(new Chat("event", "avatar:" + playerOfInterest.getUsername(),
                                playerOfInterest.getUsername() + " drew a card from " + target.getUsername()));
                    }
                }
            }
        }
        return chat;
    }

    public String getMessage() {
        return "How noble of you! You pick a card from the player with the most cards!";
    }
}
