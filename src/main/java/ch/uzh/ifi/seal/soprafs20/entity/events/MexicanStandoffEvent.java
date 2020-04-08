package ch.uzh.ifi.seal.soprafs20.entity.events;

public class MexicanStandoffEvent implements Event {
    public String getName(){
        return "mexican-standoff";
    };
    public void performEvent() {};
    public String getMessage() {return "Show your skills off! Everyone gets rid of their cards and gets three new ones! Who can finish first?";};
}
