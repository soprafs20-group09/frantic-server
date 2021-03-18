package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FantasticFourAction implements Action {

    private final Player initiator;
    private final Player[] targets;
    private final Map<Player, Integer> cardDistribution;
    private final Value wishedValue;
    private final Color wishedColor;
    private final DiscardPile discardPile;
    private final DrawStack drawStack;

    public FantasticFourAction(Player initiator, Map<Player, Integer> distribution, int value, DiscardPile discardPile, DrawStack drawStack) {
        this.initiator = initiator;
        ArrayList<Player> players = new ArrayList<>(distribution.keySet());
        this.cardDistribution = distribution;
        this.targets = players.toArray(new Player[0]);
        this.wishedValue = Value.values()[value - 1];
        this.wishedColor = Color.NONE;
        this.discardPile = discardPile;
        this.drawStack = drawStack;
    }

    public FantasticFourAction(Player initiator, Map<Player, Integer> distribution, Color wishedColor, DiscardPile discardPile, DrawStack drawStack) {
        this.initiator = initiator;
        ArrayList<Player> players = new ArrayList<>(distribution.keySet());
        this.targets = players.toArray(new Player[0]);
        this.cardDistribution = distribution;
        this.wishedValue = Value.NONE;
        this.wishedColor = wishedColor;
        this.discardPile = discardPile;
        this.drawStack = drawStack;
    }


    @Override
    public List<Chat> perform() {
        List<Chat> chat = new ArrayList<>();
        // distribute cards
        for (Map.Entry<Player, Integer> target : cardDistribution.entrySet()) {
            int cardsDrawn = 0;
            while (cardsDrawn < target.getValue() && this.drawStack.size() > 0) {
                target.getKey().pushCardToHand(drawStack.pop());
                cardsDrawn++;
            }
            chat.add(new EventChat("special:fantastic-four",
                    target.getKey().getUsername() + " drew " + cardsDrawn + (cardsDrawn == 1 ? " card." : " cards.")));
        }
        // make a wish
        Card wish = new Card(this.wishedColor, Type.WISH, this.wishedValue);
        discardPile.push(wish);
        if (this.wishedColor != Color.NONE) {
            chat.add(new EventChat("special:fantastic-four", this.initiator.getUsername()
                    + " wished " + FranticUtils.getStringRepresentation(this.wishedColor) + "."));
        }
        else {
            chat.add(new EventChat("special:fantastic-four", this.initiator.getUsername()
                    + " wished " + FranticUtils.getStringRepresentation(this.wishedValue) + "."));
        }
        return chat;
    }

    @Override
    public Player[] getTargets() {
        return this.targets;
    }

    @Override
    public Player getInitiator() {
        return this.initiator;
    }

    @Override
    public boolean isCounterable() {
        return true;
    }
}
