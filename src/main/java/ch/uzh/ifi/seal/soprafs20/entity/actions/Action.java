package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.entity.Player;

public interface Action {
    void perform();
    Player[] getTargets();
    Player getInitiator();
    boolean isCounterable();
}
