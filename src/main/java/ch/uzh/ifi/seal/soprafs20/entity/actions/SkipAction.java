package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SkipAction implements Action {

    private final Player initiator;
    private final Player target;

    public SkipAction(Player initiator, Player target) {
        this.initiator = initiator;
        this.target = target;
    }

    @Override
    public List<Chat> perform() {
        List<Chat> chat = new ArrayList<>();
        if (!this.target.isBlocked()) {
            this.target.setBlocked(true);
        }
        chat.add(new Chat("event", "special:skip", this.initiator.getUsername()
                + " skipped " + this.target.getUsername() + "."));
        return chat;
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
