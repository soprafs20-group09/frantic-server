package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class VandalismEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;
    private final Pile<Card> discardPile;

    public VandalismEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
        this.discardPile = gameRound.getDiscardPile();
    }

    public String getName() {
        return "vandalism";
    }

    public void performEvent() {
        List<Chat> chat = new ArrayList<>();

        int index = 1;
        Card relevant = this.discardPile.peekN(index);
        while (relevant.getColor().equals(Color.BLACK) || relevant.getColor().equals(Color.MULTICOLOR)) {
            if (this.discardPile.size() > index) {
                relevant = this.discardPile.peekN(++index);
            }
            else {
                this.gameRound.finishTurn();
                return;
            }
        }

        for (Player player : this.listOfPlayers) {
            int discardedCards = 0;
            for (int i = player.getHandSize() - 1; i >= 0; i--) {
                if (player.peekCard(i).getColor().equals(relevant.getColor())) {
                    player.popCard(i);
                    discardedCards++;
                }
            }
            if (discardedCards > 0) {
                chat.add(new EventChat("avatar:" + player.getUsername(),
                        player.getUsername() + " discarded " + discardedCards + (discardedCards == 1 ? " card." : " cards.")));
            }
        }

        if (chat.size() == 0) {
            chat.add(new EventChat("event:vandalism", "All spray cans are empty. No vandalism today."));
        }

        this.gameService.sendChatMessage(this.gameRound.getLobbyId(), chat);
        this.gameRound.sendCompleteGameState();
        this.gameRound.finishTurn();
    }

    public String getMessage() {
        return "Let all your anger out! You can discard all cards of the last played color! Do it! Just smash them on the discard Pile!";
    }
}
