package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class ThirdTimeLuckyEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;

    public ThirdTimeLuckyEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
    }

    public String getName() {
        return "third-time-lucky";
    }

    public void performEvent() {
        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:third-time-lucky", this.getMessage()));
        this.gameService.sendChatMessage(this.gameRound.getLobbyId(), chat);

        for (Player player : this.listOfPlayers) {
            this.gameRound.drawCardFromStack(player, 3);
        }

        this.gameRound.sendCompleteGameState();
        this.gameRound.finishTurn();
    }

    public String getMessage() {
        return "Three cards for everyone!";
    }
}
