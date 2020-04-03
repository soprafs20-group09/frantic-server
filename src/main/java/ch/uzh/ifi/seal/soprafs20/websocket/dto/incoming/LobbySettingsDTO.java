package ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;

public class LobbySettingsDTO {

    private String lobbyName;

    private GameLength duration;

    private DurationItem[] durationItems;

    private Boolean publicLobby;

    public LobbySettingsDTO() {
        this.durationItems = setDurationItems();
    }

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

    public DurationItem[] getDurationItems() {
        return durationItems;
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
