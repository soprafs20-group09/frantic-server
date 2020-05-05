package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

import java.util.Map;

public class EndRoundDTO {

    private Map<String, Integer> players;

    private int pointLimit;

    public EndRoundDTO(Map<String, Integer> players, int pointLimit) {
        this.players = players;
        this.pointLimit = pointLimit;
    }

    public Map<String, Integer> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, Integer> players) {
        this.players = players;
    }

    public int getPointLimit() {
        return this.pointLimit;
    }

    public void setPointLimit(int pointLimit) {
        this.pointLimit = pointLimit;
    }
}
