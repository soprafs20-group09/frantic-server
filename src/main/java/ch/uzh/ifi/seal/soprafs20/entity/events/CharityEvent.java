package ch.uzh.ifi.seal.soprafs20.entity.events;

public class CharityEvent implements Event {
    public String getName(){
        return "charity";
    };
    public void performEvent() {};
    public String getMessage() {
        return "How noble of you! You pick a card from the player with the most cards!";
    };
}
