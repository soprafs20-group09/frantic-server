package ch.uzh.ifi.seal.soprafs20.entity.events;

public class MarryChristmasEvent implements Event {
    public String getName(){
        return "merry-christmas";
    };
    public void performEvent() {};
    public String getMessage() {return "";};
}
