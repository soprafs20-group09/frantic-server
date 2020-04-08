package ch.uzh.ifi.seal.soprafs20.entity.events;

public class TornadoEvent implements Event {
    public String getName(){
        return "tornado";
    };
    public void performEvent() {};
    public String getMessage() {return "";};
}
