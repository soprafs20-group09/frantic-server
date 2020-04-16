package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.DiscardPile;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

public class FantasticAction implements Action {

    private Player initiator;
    private Value wishedValue;
    private Color wishedColor;
    private Player[] targets;
    private DiscardPile discardPile;

    public FantasticAction(Player initiator, int value, DiscardPile pile){
        this.initiator = initiator;
        this.wishedValue = Value.values()[value-1];
        this.wishedColor = Color.MULTICOLOR;
        this.discardPile = pile;
    }

    public FantasticAction(Player initiator, Color wishedColor, DiscardPile pile) {
        this.initiator = initiator;
        if (wishedColor != Color.BLACK) {
            this.wishedColor = wishedColor;
        }
        this.wishedValue = Value.FANTASTIC;
        this.discardPile = pile;
    }

    @Override
    public void perform() {
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
        return false;
    }
}
