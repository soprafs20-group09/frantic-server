package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;

import java.util.ArrayList;
import java.util.List;

public class FantasticAction implements Action {

    private final Player initiator;
    private final Value wishedValue;
    private Color wishedColor;
    private Player[] targets;
    private final DiscardPile discardPile;

    public FantasticAction(Player initiator, int value, DiscardPile pile) {
        this.initiator = initiator;
        this.wishedValue = Value.values()[value - 1];
        this.wishedColor = Color.NONE;
        this.discardPile = pile;
    }

    public FantasticAction(Player initiator, Color wishedColor, DiscardPile pile) {
        this.initiator = initiator;
        if (wishedColor != Color.BLACK) {
            this.wishedColor = wishedColor;
        }
        this.wishedValue = Value.NONE;
        this.discardPile = pile;
    }

    @Override
    public List<Chat> perform() {
        List<Chat> chat = new ArrayList<>();
        Card wish = new Card(this.wishedColor, Type.WISH, this.wishedValue);
        discardPile.push(wish);
        if (this.wishedColor != Color.NONE) {
            chat.add(new EventChat("special:fantastic", this.initiator.getUsername()
                    + " wished " + FranticUtils.getStringRepresentation(this.wishedColor) + "."));
        }
        else {
            chat.add(new EventChat("special:fantastic", this.initiator.getUsername()
                    + " wished " + FranticUtils.getStringRepresentation(this.wishedValue) + "."));
        }
        return chat;
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
