package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

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

        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < this.listOfPlayers.size(); i++) {
            Player toPlayer = this.listOfPlayers.get((i + 1) % this.listOfPlayers.size());
            for (Card card : allCards.get(i)) {
                toPlayer.pushCardToHand(card);
            }
        }

        this.gameService.sendAnimationSpeed(this.gameRound.getLobbyId(), 500);
        this.gameRound.sendCompleteGameState();
        this.gameRound.finishTurn();
    }

    public String getMessage() {
        return "Oh no! Everything is shaken up! Good luck with your new cards!";
    }
}
