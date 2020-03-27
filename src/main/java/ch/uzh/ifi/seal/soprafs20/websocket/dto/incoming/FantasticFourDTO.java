package ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming;

import java.util.Map;

public class FantasticFourDTO {

    private Map<String, Integer> players;

    private String color;

    private int number;

    public Map<String, Integer> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, Integer> players) {
        this.players = players;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
