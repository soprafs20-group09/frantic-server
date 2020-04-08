package ch.uzh.ifi.seal.soprafs20.entity.events;

public class RecessionEvent implements Event {
    public String getName(){
        return "recession";
    };
    public void performEvent() {};
    public String getMessage() {return "";};
}
