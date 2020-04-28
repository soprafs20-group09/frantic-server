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
    private Pile<Card> discardPile;
    private int amount;

    private final GameService gameService;

    public RecessionEvent(String lobbyId, Player currentPlayer, List<Player> listOfPlayers, Pile<Card> discardPile, GameService gameService) {
        this.lobbyId = lobbyId;
        this.currentPlayer = currentPlayer;
        this.listOfPlayers = listOfPlayers;
        this.discardPile = discardPile;
        this.gameService = gameService;
        this.amount = 1;
    }

    public String getName() {
        return "recession";
    }

    public List<Chat> performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:recession", this.getMessage()));

        Player p = this.currentPlayer;
        List<Player> orderedListOfPlayers = new ArrayList<>();
        orderedListOfPlayers.add(p);
        for (int i = 0; i < this.listOfPlayers.size(); i++) {
            p = nextPlayer(p);
            orderedListOfPlayers.add(p);
        }

        for (Player player : orderedListOfPlayers) {
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

    private Player nextPlayer(Player prev) {
        int playersIndex = this.listOfPlayers.indexOf(prev);
        playersIndex = (playersIndex + 1) % this.listOfPlayers.size();
        return this.listOfPlayers.get(playersIndex);
    }

    //TODO: make it dynamic
    public String getMessage() {
        return "One, two, three, ... Since you are the 3rd to discard, you can discard 3 cards!";
    }
}
