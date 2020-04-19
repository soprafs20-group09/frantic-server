package ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming;

import ch.uzh.ifi.seal.soprafs20.constant.Color;

public class EqualityDTO {

    private String target;

    private Color color;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
