package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

import java.util.Map;

public class EndRoundDTO {

    private Map<String, Integer> players;

    private Map<String, Integer> changes;

    private int pointLimit;

    private int seconds;

    private String icon;

    private String message;

    public EndRoundDTO(Map<String, Integer> players, Map<String, Integer> changes, int pointLimit, int seconds, String icon, String message) {
        this.players = players;
        this.changes = changes;
        this.pointLimit = pointLimit;
        this.seconds = seconds;
        this.icon = icon;
        this.message = message;
    }

    public Map<String, Integer> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, Integer> players) {
        this.players = players;
    }

    public Map<String, Integer> getChanges() {
        return changes;
    }

    public void setChanges(Map<String, Integer> changes) {
        this.changes = changes;
    }

    public int getPointLimit() {
        return this.pointLimit;
    }

    public void setPointLimit(int pointLimit) {
        this.pointLimit = pointLimit;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
