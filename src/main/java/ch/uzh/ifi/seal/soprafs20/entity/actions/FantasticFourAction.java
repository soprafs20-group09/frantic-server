package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.DiscardPile;
import ch.uzh.ifi.seal.soprafs20.entity.DrawStack;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

import java.util.ArrayList;
import java.util.Map;

public class FantasticFourAction implements Action {

    private Player initiator;
    private Player[] targets;
    private Map<Player, Integer> cardDistribution;
    private Value wishedValue;
    private Color wishedColor;
    private DiscardPile discardPile;
    private DrawStack drawStack;

    public FantasticFourAction(Player initiator, Map<Player, Integer> distribution, int value, DiscardPile discardPile, DrawStack drawStack){
        this.initiator = initiator;
        ArrayList<Player> players = new ArrayList<>(distribution.keySet());
        this.cardDistribution  = distribution;
        this.targets = players.toArray(new Player[players.size()]);
        this.wishedValue = Value.values()[value-1];
        this.wishedColor = Color.MULTICOLOR;
        this.discardPile = discardPile;
        this.drawStack = drawStack;
    }

    public FantasticFourAction(Player initiator, Map<Player, Integer> distribution, Color wishedColor, DiscardPile discardPile, DrawStack drawStack){
        this.initiator = initiator;
        ArrayList<Player> players = new ArrayList<>(distribution.keySet());
        this.targets = players.toArray(new Player[players.size()]);
        this.cardDistribution  = distribution;
        this.wishedValue = Value.FANTASTICFOUR;
        this.wishedColor = wishedColor;
        this.discardPile = discardPile;
        this.drawStack = drawStack;
    }


    @Override
    public void perform() {
        // distribute cards
        for (Map.Entry<Player, Integer> target : cardDistribution.entrySet()) {
            for (int i = 0; i < target.getValue(); i++){
                Card c = drawStack.pop();
                target.getKey().pushCardToHand(c);
            }
        }
        // make a wish
        Card wish = new Card(this.wishedColor, Type.WISH, this.wishedValue);
        discardPile.push(wish);

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
