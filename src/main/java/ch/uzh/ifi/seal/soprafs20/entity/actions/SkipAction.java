package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.entity.Player;

public class SkipAction implements Action{

    private Player initiator;
    private Player target;

    public SkipAction(Player initiator, Player target){
        this.initiator = initiator;
        this.target = target;
    }

    @Override
    public void perform() {
        if (!this.target.isBlocked()){
            this.target.setBlocked(true);
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
