package ch.uzh.ifi.seal.soprafs20.entity.events;

public class TimeBombEvent implements Event {
    public String getName(){
        return "time-bomb";
    };
    public void performEvent() {};
    public String getMessage() {return "";};
}
