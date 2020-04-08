package ch.uzh.ifi.seal.soprafs20.entity.events;

public class VandalismEvent implements Event {
    public String getName(){
        return "vandalism";
    };
    public void performEvent() {};
    public String getMessage() {return "";};
}
