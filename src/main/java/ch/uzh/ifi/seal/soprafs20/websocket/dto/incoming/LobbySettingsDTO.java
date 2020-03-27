package ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming;

public class LobbySettingsDTO {

    private String lobbyName;

    private String duration;

    private Boolean publicLobby;

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Boolean getPublicLobby() {
        return publicLobby;
    }

    public void setPublicLobby(Boolean publicLobby) {
        this.publicLobby = publicLobby;
    }
}
