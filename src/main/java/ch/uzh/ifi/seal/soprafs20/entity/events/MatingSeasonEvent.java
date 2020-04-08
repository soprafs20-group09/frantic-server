package ch.uzh.ifi.seal.soprafs20.entity.events;

public class MatingSeasonEvent implements Event {
    public String getName(){
        return "mating-season";
    };
    public void performEvent() {};
    public String getMessage() {return "";};
}
