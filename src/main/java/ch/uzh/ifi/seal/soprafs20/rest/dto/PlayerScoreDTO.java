package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class PlayerScoreDTO {

    private String username;

    private int score;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
