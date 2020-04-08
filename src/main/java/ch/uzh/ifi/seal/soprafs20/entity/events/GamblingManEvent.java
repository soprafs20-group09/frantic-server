package ch.uzh.ifi.seal.soprafs20.entity.events;

public class GamblingManEvent implements Event {
    public String getName(){
        return "gambling-man";
    };
    public void performEvent() {};
    public String getMessage() {return "";};
}
