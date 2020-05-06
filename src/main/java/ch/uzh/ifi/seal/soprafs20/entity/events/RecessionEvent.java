package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class RecessionEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;
    private final Player currentPlayer;
    private int amount;

    public RecessionEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
        this.currentPlayer = gameRound.getCurrentPlayer();
        this.amount = 1;
    }

    public String getName() {
        return "recession";
    }

    public void performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:recession", this.getMessage()));
        this.gameService.sendChatMessage(this.gameRound.getLobbyId(), chat);

        int numOfPlayers = this.listOfPlayers.size();
        int initiatorIndex = this.listOfPlayers.indexOf(this.currentPlayer);
        for (int i = 1; i <= numOfPlayers; i++) {
            Player player = this.listOfPlayers.get((initiatorIndex + i) % numOfPlayers);
            this.gameService.sendRecession(this.gameRound.getLobbyId(), player, this.amount);
            if (amount == 1) {
                chat.add(new Chat("event", "event:recession", player.getUsername() + " discards " + amount + " card."));
            }
            else {
                chat.add(new Chat("event", "event:recession", player.getUsername() + " discards " + amount + " cards."));
            }
            amount++;
        }
    }

    public String getMessage() {
        return "One, two, three, ... Since you are the 3rd to discard, you can discard 3 cards!";
    }
}
