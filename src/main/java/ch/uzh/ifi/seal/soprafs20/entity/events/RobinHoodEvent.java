package ch.uzh.ifi.seal.soprafs20.entity.events;

public class RobinHoodEvent implements Event {
    public String getName(){
        return "robin-hood";
    };
    public void performEvent() {};
    public String getMessage() {return "";};
}
