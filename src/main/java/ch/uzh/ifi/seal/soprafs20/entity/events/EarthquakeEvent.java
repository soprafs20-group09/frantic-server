package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;

    public EarthquakeEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
    }

    public String getName() {
        return "earthquake";
    }

    public void performEvent() {
        List<Chat> chat = new ArrayList<>();

        List<List<Card>> allCards = new ArrayList<>();
        for (int i = 0; i < this.listOfPlayers.size(); i++) {
            Player player = this.listOfPlayers.get(i);
            allCards.add(new ArrayList<>());
            for (int j = player.getHandSize() - 1; j >= 0; j--) {
                allCards.get(i).add(player.popCard(j));
            }
        }
        this.gameService.sendAnimationSpeed(this.gameRound.getLobbyId(), 0);
        this.gameRound.sendCompleteGameState();

        FranticUtils.wait(500);

        for (int i = 0; i < this.listOfPlayers.size(); i++) {
            Player fromPlayer = this.listOfPlayers.get((i) % this.listOfPlayers.size());
            Player toPlayer = this.listOfPlayers.get((i + 1) % this.listOfPlayers.size());
            for (Card card : allCards.get(i)) {
                toPlayer.pushCardToHand(card);
            }
            chat.add(new Chat("event", "event:earthquake", fromPlayer.getUsername() + " gave all cards to " + toPlayer.getUsername() + "."));
        }

        this.gameService.sendChatMessage(this.gameRound.getLobbyId(), chat);
        this.gameService.sendAnimationSpeed(this.gameRound.getLobbyId(), 500);
        this.gameRound.sendCompleteGameState();
        this.gameRound.finishTurn();
    }

    public String getMessage() {
        return "Oh no! Everything is shaken up! Good luck with the cards of your neighbor!";
    }
}
