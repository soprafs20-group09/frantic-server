package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.DiscardPile;
import ch.uzh.ifi.seal.soprafs20.entity.DrawStack;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

public class EqualityAction implements Action {
    private Player initiator;
    private Player target;
    private Color color;
    private DiscardPile discardPile;
    private DrawStack drawStack;

    public EqualityAction(Player initiator, Player target, Color color, DiscardPile discardPile, DrawStack drawStack) {
        this.initiator = initiator;
        this.target = target;
        this.color = color;
        this.discardPile = discardPile;
        this.drawStack = drawStack;
    }

    @Override
    public void perform() {
        while (this.initiator.getHandSize() > this.target.getHandSize()) {
            this.target.pushCardToHand(drawStack.pop());
        }
        discardPile.pop();
        discardPile.push(new Card(this.color, Type.WISH, Value.COLORWISH, false, 0));
    }

    @Override
    public Player[] getTargets() {
        return new Player[]{this.target};
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
