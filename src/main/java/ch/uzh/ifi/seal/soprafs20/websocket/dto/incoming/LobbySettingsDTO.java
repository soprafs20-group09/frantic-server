package ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;

public class LobbySettingsDTO {

    private String lobbyName;

    private GameLength duration;

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
}
