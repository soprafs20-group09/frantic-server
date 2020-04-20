package ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming;

import ch.uzh.ifi.seal.soprafs20.constant.Color;

import java.util.Map;

public class FantasticFourDTO {

    private Map<String, Integer> targets;

    private Color color;

    private int number;

    public Map<String, Integer> getTargets() {
        return targets;
    }

    public void setTargets(Map<String, Integer> targets) {
        this.targets = targets;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
