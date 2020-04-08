package ch.uzh.ifi.seal.soprafs20.entity.events;

public class DoomsdayEvent implements Event {
    public String getName(){
        return "doomsday";
    };
    public void performEvent() {};
    public String getMessage() {return "";};
}
