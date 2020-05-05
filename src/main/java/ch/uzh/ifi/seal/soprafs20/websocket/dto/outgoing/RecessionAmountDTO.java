package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class RecessionAmountDTO {

    private int amount;

    public RecessionAmountDTO(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
