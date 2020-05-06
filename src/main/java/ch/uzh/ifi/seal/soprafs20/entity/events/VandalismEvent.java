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
    private Pile<Card> discardPile;

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

        int index = 1;
        Card relevant = this.discardPile.peekN(index);
        while (relevant.getColor().equals(Color.BLACK) || relevant.getColor().equals(Color.MULTICOLOR)) {
            relevant = this.discardPile.peekN(index++);
        }

        for (Player player : this.listOfPlayers) {
            for (int i = player.getHandSize() - 1; i >= 0; i--) {
                if (player.peekCard(i).getColor().equals(relevant.getColor())) {
                    player.popCard(i);
                }
            }
        }
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:vandalism", this.getMessage()));

        this.gameService.sendChatMessage(this.gameRound.getLobbyId(), chat);
        this.gameRound.sendCompleteGameState();
        this.gameRound.finishTurn();
    }

    public String getMessage() {
        return "Let all your anger out! You can discard all cards of the last played color! Do it! Just smash them on the discard Pile!";
    }
}
