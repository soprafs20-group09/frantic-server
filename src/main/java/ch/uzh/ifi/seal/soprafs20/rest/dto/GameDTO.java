package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.entity.Player;

import java.util.List;

public class GameDTO {

    private String lobbyId;

    private int roundCount;

    private List<PlayerScoreDTO> playerScores;

    public String getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }

    public List<PlayerScoreDTO> getPlayerScores() {
        return playerScores;
    }

    public void setPlayerScores(List<PlayerScoreDTO> playerScores) {
        this.playerScores = playerScores;
    }
}
