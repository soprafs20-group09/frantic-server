package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

import java.util.Arrays;
import java.util.Collections;

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
        Arrays.sort(gifts);
        for (int i = this.gifts.length - 1; i >= 0; i--) {
            if (this.initiator.peekCard(this.gifts[i]).getValue() != Value.FUCKYOU) {
                target.pushCardToHand(this.initiator.popCard(this.gifts[i]));
            }
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

    @Override
    public Chat getChat() {
        return new Chat("event", "gift", this.initiator.getUsername()
                + " gifted " + this.target.getUsername() + " 2 cards.");
    }
}
