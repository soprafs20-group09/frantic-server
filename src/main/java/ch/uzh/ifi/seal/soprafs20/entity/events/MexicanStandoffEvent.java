package ch.uzh.ifi.seal.soprafs20.entity.events;

public class MexicanStandoffEvent implements Event {
    public String getName(){
        return "mexican-standoff";
    };
    public void performEvent() {};
    public String getMessage() {return "";};
}
