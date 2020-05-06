package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Pile;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class RecessionEvent implements Event {

    private String lobbyId;
    private final List<Player> listOfPlayers;
    private Player currentPlayer;
    private int amount;

    private final GameService gameService;

    public RecessionEvent(String lobbyId, Player currentPlayer, List<Player> listOfPlayers, GameService gameService) {
        this.lobbyId = lobbyId;
        this.currentPlayer = currentPlayer;
        this.listOfPlayers = listOfPlayers;
        this.gameService = gameService;
        this.amount = 1;
    }

    public String getName() {
        return "recession";
    }

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:recession", this.getMessage()));

        int numOfPlayers = this.listOfPlayers.size();
        int initiatorIndex = this.listOfPlayers.indexOf(this.currentPlayer);
        for (int i = 1; i <= numOfPlayers; i++) {
            Player player = this.listOfPlayers.get((initiatorIndex + i) % numOfPlayers);
            this.gameService.sendRecession(this.lobbyId, player, this.amount);
            if (amount == 1) {
                chat.add(new Chat("event", "event:recession", player.getUsername() + " discards " + amount + " card."));
            }
            else {
                chat.add(new Chat("event", "event:recession", player.getUsername() + " discards " + amount + " cards."));
            }
            amount++;
        }
        return chat;
    }

    public String getMessage() {
        return "Card Stocks are going down! Dispose one or two or three ...";
    }
}
