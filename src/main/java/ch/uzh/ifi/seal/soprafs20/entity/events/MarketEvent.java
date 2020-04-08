package ch.uzh.ifi.seal.soprafs20.entity.events;

public class MarketEvent implements Event {
    public String getName(){
        return "market";
    };
    public void performEvent() {};
    public String getMessage() {return "";};
}
