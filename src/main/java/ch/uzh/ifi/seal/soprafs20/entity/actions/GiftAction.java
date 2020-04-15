package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class GiftAction implements Action {
    private Player initiator;
    private Player target;
    private Integer[] gifts;

    public GiftAction(Player initiator, Player target, Integer[] gifts) {
        this.initiator = initiator;
        this.target = target;
        this.gifts = gifts;
    }

    @Override
    public void perform() {
        Arrays.sort(this.gifts, Collections.reverseOrder());
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
