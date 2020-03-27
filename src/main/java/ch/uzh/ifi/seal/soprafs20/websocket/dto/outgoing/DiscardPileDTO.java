package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class DiscardPileDTO {

    private String type;

    private String value;

    private String color;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
