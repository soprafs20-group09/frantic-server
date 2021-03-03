package ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;
import ch.uzh.ifi.seal.soprafs20.constant.TurnDuration;

public class LobbySettingsDTO {

    private String lobbyName;

    private GameLength gameDuration;

    private TurnDuration turnDuration;

    private DurationItem[] gameDurationItems;

    private DurationItem[] turnDurationItems;

    private Boolean publicLobby;

    public LobbySettingsDTO() {
        this.gameDurationItems = setGameDurationItems();
        this.turnDurationItems = setTurnDurationItems();
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public GameLength getGameDuration() {
        return gameDuration;
    }

    public void setGameDuration(GameLength gameDuration) {
        this.gameDuration = gameDuration;
    }

    public TurnDuration getTurnDuration() {
        return turnDuration;
    }

    public void setTurnDuration(TurnDuration turnDuration) {
        this.turnDuration = turnDuration;
    }

    public Boolean getPublicLobby() {
        return publicLobby;
    }

    public void setPublicLobby(Boolean publicLobby) {
        this.publicLobby = publicLobby;
    }

    public DurationItem[] setGameDurationItems() {
        String[] names = new String[]{"Short", "Medium", "Long"};
        String[] values = new String[]{"SHORT", "MEDIUM", "LONG"};

        DurationItem[] items = new DurationItem[3];

        for (int i = 0; i < names.length; i++) {
            DurationItem d = new DurationItem();
            d.name = names[i];
            d.value = values[i];
            items[i] = d;
        }
        return items;
    }

    public DurationItem[] getGameDurationItems() {
        return gameDurationItems;
    }

    public DurationItem[] setTurnDurationItems() {
        String[] names = new String[]{"Normal", "Long", "Off"};
        String[] values = new String[]{"NORMAL", "LONG", "OFF"};

        DurationItem[] items = new DurationItem[3];

        for (int i = 0; i < names.length; i++) {
            DurationItem d = new DurationItem();
            d.name = names[i];
            d.value = values[i];
            items[i] = d;
        }
        return items;
    }

    public DurationItem[] getTurnDurationItems() {
        return turnDurationItems;
    }


    private static class DurationItem {

        private String name;

        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
