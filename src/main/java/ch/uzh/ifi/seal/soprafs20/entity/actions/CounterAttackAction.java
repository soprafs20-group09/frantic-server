package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.DiscardPile;
import ch.uzh.ifi.seal.soprafs20.entity.DrawStack;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

public class CounterAttackAction implements Action {
    private Player initiator;
    private Player[] targets;
    private Color color;
    private DiscardPile discardPile;
    private DrawStack drawStack;

    public CounterAttackAction(Player initiator, Color color, DiscardPile discardPile) {
        this.initiator = initiator;
        this.color = color;
        this.discardPile = discardPile;
    }

    @Override
    public void perform() {
        this.discardPile.push(new Card(this.color, Type.WISH, Value.COLORWISH, false, 0));
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
