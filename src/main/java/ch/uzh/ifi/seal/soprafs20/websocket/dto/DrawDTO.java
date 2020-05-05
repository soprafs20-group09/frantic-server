package ch.uzh.ifi.seal.soprafs20.websocket.dto;

public class DrawDTO {

    private int amount;

    public DrawDTO() {}

    public DrawDTO(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
