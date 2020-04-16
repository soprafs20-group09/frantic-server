package ch.uzh.ifi.seal.soprafs20.entity.cards;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;

public class ExchangeCard extends Card {


    public ExchangeCard(Color c, int orderKey){
        super(c, Type.SPECIAL, Value.EXCHANGE, true, orderKey );
    }

    public void performAction() {
        // player of this card gives another player two cards of his choice from his hand and in exchange gets two random cards from his opponent.
    }
}
