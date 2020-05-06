package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatingSeasonEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;
    private final Player initiator;

    public MatingSeasonEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
        this.initiator = gameRound.getCurrentPlayer();
    }

    public String getName() {
        return "mating-season";
    }

    public void performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:mating-season", this.getMessage()));

        int numOfPlayers = this.listOfPlayers.size();
        int initiatorIndex = this.listOfPlayers.indexOf(this.initiator);

        for (int p = 1; p <= numOfPlayers; p++) {
            Map<Value, Integer> mappedValues = new HashMap<>();
            Player playerOfInterest = this.listOfPlayers.get((initiatorIndex + p) % numOfPlayers);

            for (int i = 0; i < playerOfInterest.getHandSize(); i++) {
                Card card = playerOfInterest.peekCard(i);
                Value cardValue = card.getValue();
                if (cardValue.ordinal() < 9) {
                    int count = mappedValues.getOrDefault(cardValue, 0);
                    mappedValues.put(cardValue, count + 1);
                }
            }

            int discardedCards = 0;
            for (int i = playerOfInterest.getHandSize() - 1; i >= 0; i--) {
                Card card = playerOfInterest.peekCard(i);
                Value cardValue = card.getValue();
                if (mappedValues.containsKey(cardValue) && mappedValues.get(cardValue) > 1) {
                    playerOfInterest.popCard(i);
                    discardedCards++;
                }
            }

            if (discardedCards > 0) {
                chat.add(new Chat("event", "avatar:" + playerOfInterest.getUsername(),
                        playerOfInterest.getUsername() + " discarded " + discardedCards + " cards"));
            }
        }
        this.gameService.sendChatMessage(this.gameRound.getLobbyId(), chat);
        this.gameRound.sendCompleteGameState();
        this.gameRound.finishTurn();
    }

    public String getMessage() {
        return "It's valentines day! Well, at least for your cards! Discard numeral pairs, triples and so on.";
    }
}
