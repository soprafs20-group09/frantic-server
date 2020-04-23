package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

import java.util.Map;

public class EndRoundDTO {

    private Map<String, Integer> players;
    private int pointLimit;

    public Map<String, Integer> getPlayers() {
        return players;
    }

    public int getPointLimit() {
        return this.pointLimit;
    }

    public void setPlayers(Map<String, Integer> players) {
        this.players = players;
    }
}
