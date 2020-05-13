package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

import java.util.Map;

public class EndGameDTO {

    private Map<String, Integer> players;

    private Map<String, Integer> changes;

    private String icon;

    private String message;

    public EndGameDTO(Map<String, Integer> players, Map<String, Integer> changes, String icon, String message) {
        this.players = players;
        this.changes = changes;
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
