package ch.uzh.ifi.seal.soprafs20.entity.events;

public class ExpansionEvent implements Event {
    public String getName(){
        return "expansion";
    };
    public void performEvent() {};
    public String getMessage() {return "";};
}
