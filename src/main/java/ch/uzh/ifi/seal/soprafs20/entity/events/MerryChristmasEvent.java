package ch.uzh.ifi.seal.soprafs20.entity.events;

public class MerryChristmasEvent implements Event {
    public String getName(){
        return "merry-christmas";
    };
    public void performEvent() {};
    public String getMessage() {return "Merry christmas everyone! It's that time of the year again. Give presents to your loved ones!";};
}
