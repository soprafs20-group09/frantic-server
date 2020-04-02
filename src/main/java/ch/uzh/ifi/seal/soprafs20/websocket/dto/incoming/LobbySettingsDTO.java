package ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;

public class LobbySettingsDTO {

    private String lobbyName;

    private GameLength duration;

    private DurationItem[] durationItems = setDurationItems();

    private Boolean publicLobby;

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public GameLength getDuration() {
        return duration;
    }

    public void setDuration(GameLength duration) {
        this.duration = duration;
    }

    public Boolean getPublicLobby() {
        return publicLobby;
    }

    public void setPublicLobby(Boolean publicLobby) {
        this.publicLobby = publicLobby;
    }

    public DurationItem[] setDurationItems() {
        return new DurationItem[]{
                new DurationItem("Short", "short"),
                new DurationItem("Medium", "medium"),
                new DurationItem("Long", "long")
        };
    }



    private static class DurationItem {

        public DurationItem(String name, String value) {
        }
    }
}
