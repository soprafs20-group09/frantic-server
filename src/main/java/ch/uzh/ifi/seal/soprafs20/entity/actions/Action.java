package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

import java.util.List;

public interface Action {
    List<Chat> perform();

    Player[] getTargets();

    Player getInitiator();

    boolean isCounterable();
}
