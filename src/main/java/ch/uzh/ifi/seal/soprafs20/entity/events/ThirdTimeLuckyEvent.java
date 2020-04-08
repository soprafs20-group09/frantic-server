package ch.uzh.ifi.seal.soprafs20.entity.events;

public class ThirdTimeLuckyEvent implements Event {
    public String getName(){
        return "third-time-lucky";
    };
    public void performEvent() {};
    public String getMessage() {return "";};
}
