package ch.uzh.ifi.seal.soprafs20.entity.events;

public class EarthquakeEvent implements Event {
    public String getName(){
        return "earthquake";
    };
    public void performEvent() {};
    public String getMessage() {return "Oh no! Everything is shaken up! Good luck with your new cards!";};
}
