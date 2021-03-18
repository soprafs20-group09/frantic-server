package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.EventChat;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExchangeAction implements Action {

    private final Player initiator;
    private final Player target;
    private final int[] exchangeCards; // cards the initiator gives to the target


    public ExchangeAction(Player initiator, Player target, int[] cards) {
        this.initiator = initiator;
        this.target = target;
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

    public List<Chat> perform() {
        List<Chat> chat = new ArrayList<>();
        ArrayList<Card> initiatorCards = new ArrayList<>();
        boolean twoCardsExchanged = exchangeCards.length == 2;
        Arrays.sort(this.exchangeCards);
        for (int i = this.exchangeCards.length - 1; i >= 0; i--) {
            Card toPush = this.initiator.peekCard(this.exchangeCards[i]);
            if (toPush.getValue() != Value.FUCKYOU) {
                toPush = this.initiator.popCard(this.exchangeCards[i]);
                initiatorCards.add(toPush);
            }
        }

        if (this.target.getHandSize() < 2) {
            Card toInitiator = this.target.popCard(0);
            this.initiator.pushCardToHand(toInitiator);
            twoCardsExchanged = false;
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

        chat.add(new EventChat("special:exchange", this.initiator.getUsername()
                + " exchanged " + (twoCardsExchanged ? "2" : "") + " cards with " + this.target.getUsername() + "."));
        return chat;
    }
}
