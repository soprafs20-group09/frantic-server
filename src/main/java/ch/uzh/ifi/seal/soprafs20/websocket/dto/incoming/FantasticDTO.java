package ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming;

import ch.uzh.ifi.seal.soprafs20.constant.Color;

public class FantasticDTO {

    private Color color;

    private int number;

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
