package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class MexicanStandoffEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;

    public MexicanStandoffEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
    }

    public String getName() {
        return "mexican-standoff";
    }

    public void performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:mexican-standoff", this.getMessage()));
        this.gameService.sendChatMessage(this.gameRound.getLobbyId(), chat);

        this.gameService.sendAnimationSpeed(this.gameRound.getLobbyId(), 0);
        for (Player player : this.listOfPlayers) {
            for (int i = player.getHandSize() - 1; i >= 0; i--) {
                player.popCard(i);
            }
            this.gameService.sendHand(this.gameRound.getLobbyId(), player);
        }

        this.gameService.sendAnimationSpeed(this.gameRound.getLobbyId(), 500);
        for (Player player : this.listOfPlayers) {
            this.gameRound.drawCardFromStack(player, 3);
        }

        this.gameRound.sendCompleteGameState();
        this.gameRound.finishTurn();
    }

    public String getMessage() {
        return "Show your skills off! Everyone gets rid of their cards and gets three new ones! Who can finish first?";
    }
}
