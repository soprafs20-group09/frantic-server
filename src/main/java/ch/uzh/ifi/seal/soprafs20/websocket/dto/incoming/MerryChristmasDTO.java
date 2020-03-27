package ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming;

public class MerryChristmasDTO {

    private GiveCardsToPlayerDTO[] targets;

    public GiveCardsToPlayerDTO[] getTargets() {
        return targets;
    }

    public void setTargets(GiveCardsToPlayerDTO[] targets) {
        this.targets = targets;
    }
}
