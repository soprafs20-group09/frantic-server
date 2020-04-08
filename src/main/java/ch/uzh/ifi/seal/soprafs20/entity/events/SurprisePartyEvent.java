package ch.uzh.ifi.seal.soprafs20.entity.events;

public class SurprisePartyEvent implements Event {
    public String getName(){
        return "surprise-party";
    };
    public void performEvent() {};
    public String getMessage() {return "";};
}
