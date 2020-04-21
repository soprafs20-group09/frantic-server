package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;

import java.util.ArrayList;
import java.util.Random;

public class ExchangeAction implements Action {

    private Player initiator;
    private Player target;
    private int[] exchangeCards; // cards the initiator gives to the target


    public ExchangeAction(Player initiator, Player target, int[] cards) {
        this.initiator = initiator;
        this.target = target;
        if (cards.length > 1 && cards[0] < cards[1]) {
            int temp = cards[0];
            cards[0] = cards[1];
            cards[1] = temp;
        }
        this.exchangeCards = cards;
    }

    public Player getInitiator() {
        return this.initiator;
    }

    public Player[] getTargets() {
        return new Player[]{this.target};
    }

    public boolean isCounterable() {
        return true;
    }

    @Override
    public Chat getChat() {
        return null;
    }

    public void perform() {
        ArrayList<Card> initiatorCards = new ArrayList<Card>();
        for (int i : exchangeCards) {
            Card toPush = this.initiator.peekCard(i);
            if (toPush.getValue() != Value.FUCKYOU) {
                toPush = this.initiator.popCard(i);
                initiatorCards.add(toPush);
            }
        }

        if (this.target.getHandSize() < 2) {
            Card toInitiator = this.target.popCard(0);
            this.initiator.pushCardToHand(toInitiator);
        }
        else {
            // gernerate random cards to be pushed to the initiator
            int random1 = FranticUtils.random.nextInt(target.getHandSize());
            int random2 = FranticUtils.random.nextInt(target.getHandSize());
            while (random1 == random2) {
                random2 = FranticUtils.random.nextInt(target.getHandSize());
            }

            // remove card with bigger index first
            if (random1 > random2) {
                Card toInitiator = this.target.popCard(random1);
                this.initiator.pushCardToHand(toInitiator);
                toInitiator = this.target.popCard(random2);
                this.initiator.pushCardToHand(toInitiator);
            }
            else {
                Card toInitiator = this.target.popCard(random2);
                this.initiator.pushCardToHand(toInitiator);
                toInitiator = this.target.popCard(random1);
                this.initiator.pushCardToHand(toInitiator);
            }
        }

        for (Card c : initiatorCards) {
            this.target.pushCardToHand(c);
        }

    }
}
