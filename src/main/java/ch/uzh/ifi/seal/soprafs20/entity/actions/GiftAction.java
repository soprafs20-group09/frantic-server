package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.entity.Player;

public class GiftAction implements Action {
    private Player initiator;
    private Player target;
    private int[] gifts;

    public GiftAction(Player initiator, Player target, int[] gifts) {
        this.initiator = initiator;
        this.target = target;
        this.gifts = gifts;
    }

    @Override
    public void perform() {
        for (int gift : this.gifts) {
            target.pushCardToHand(this.initiator.popCard(gift));
        }
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
