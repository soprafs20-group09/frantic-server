package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;

import java.util.ArrayList;
import java.util.List;

public class CharityEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;
    private final Player initiator;

    public CharityEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
        this.initiator = gameRound.getCurrentPlayer();
    }

    public String getName() {
        return "charity";
    }

    public void performEvent() {
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
        this.gameService.sendChatMessage(this.gameRound.getLobbyId(), chat);
        this.gameRound.sendCompleteGameState();
        this.gameRound.finishTurn();
    }

    public String getMessage() {
        return "How noble of you! You pick a card from the player with the most cards!";
    }
}
