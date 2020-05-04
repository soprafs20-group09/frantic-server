package ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming;

import java.util.Map;

public class MerryChristmasDTO {

    private Map<String, Integer[]> targets;

    public Map<String, Integer[]> getTargets() {
        return targets;
    }

    public void setTargets(Map<String, Integer[]> targets) {
        this.targets = targets;
    }
}
