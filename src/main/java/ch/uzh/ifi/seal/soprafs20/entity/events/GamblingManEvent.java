package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.TurnDuration;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;

import java.util.ArrayList;
import java.util.List;

public class GamblingManEvent implements Event {

    private final GameRound gameRound;
    private final GameService gameService;
    private final List<Player> listOfPlayers;
    private final Pile<Card> discardPile;
    private final int seconds;

    public GamblingManEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        this.gameService = gameRound.getGameService();
        this.listOfPlayers = gameRound.getListOfPlayers();
        this.discardPile = gameRound.getDiscardPile();
        this.seconds = gameRound.getTurnDuration().getValue();
    }

    public String getName() {
        return "gambling-man";
    }

    public void performEvent() {
        int index = 1;
        Color relevant = this.discardPile.peekN(index).getColor();
        while (relevant.equals(Color.BLACK) || relevant.equals(Color.MULTICOLOR) || relevant.equals(Color.NONE)) {
            if (this.discardPile.size() > index) {
                relevant = this.discardPile.peekN(++index).getColor();
            }
            else {
                Chat chat = new EventChat("event:gambling-man", "There is no gambling today.");
                this.gameService.sendChatMessage(this.gameRound.getLobbyId(), chat);
                this.gameRound.finishTurn();
                return;
            }
        }
        for (Player player : this.listOfPlayers) {
            List<Integer> cards = new ArrayList<>();
            for (int i = 0; i < player.getHandSize(); i++) {
                Card card = player.peekCard(i);
                if (card.getType().equals(Type.NUMBER) && card.getColor().equals(relevant)) {
                    cards.add(i);
                }
            }
            if (!cards.isEmpty()) {
                Integer randomChoice = cards.get(FranticUtils.random.nextInt(cards.size()));
                this.gameRound.setGamblingManMap(player, randomChoice);

                int[] playable = cards.stream().mapToInt(i -> i).toArray();
                this.gameService.sendGamblingMan(this.gameRound.getLobbyId(), player, playable);
            }
            // no such color
            else {
                this.gameRound.addEventResponses(player);
                this.gameRound.drawCardFromStack(player, 2);
            }
        }
        if (this.gameRound.getEventResponsesSize() < this.listOfPlayers.size() - 1) {
            if (this.gameRound.getTurnDuration() != TurnDuration.OFF) {
                this.gameService.sendTimer(this.gameRound.getLobbyId(), seconds);
                this.gameRound.startGamblingManTimer(seconds);
            }
        }
        else {
            Chat chat = new EventChat("event:gambling-man", "Too few players were able to gamble.");
            this.gameService.sendChatMessage(this.gameRound.getLobbyId(), chat);
            this.gameRound.clearEventResponses();
            this.gameRound.finishTurn();
        }
    }

    public String getMessage() {
        return "It's time to gamble! Choose a number card of the last played color. The player with the highest digit has to take all of them. So choose wisely!";
    }
}
