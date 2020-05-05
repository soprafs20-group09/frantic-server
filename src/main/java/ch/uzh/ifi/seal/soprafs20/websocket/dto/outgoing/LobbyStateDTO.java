package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.LobbySettingsDTO;

public class LobbyStateDTO {

    private LobbyPlayerDTO[] players;

    private LobbySettingsDTO settings;

    public LobbyStateDTO() {}

    public LobbyStateDTO(LobbyPlayerDTO[] players, LobbySettingsDTO settings) {
        this.players = players;
        this.settings = settings;
    }

    public LobbyPlayerDTO[] getPlayers() {
        return players;
    }

    public void setPlayers(LobbyPlayerDTO[] players) {
        this.players = players;
    }

    public LobbySettingsDTO getSettings() {
        return settings;
    }

    public void setSettings(LobbySettingsDTO settings) {
        this.settings = settings;
    }
}

