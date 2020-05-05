package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class RegisteredDTO {

    private String username;

    private String lobbyId;

    public RegisteredDTO(String username, String lobbyId) {
        this.username = username;
        this.lobbyId = lobbyId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }
}
